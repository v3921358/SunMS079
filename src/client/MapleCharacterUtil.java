package client;

import constants.GameConstants;
import database.DBConPool;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tools.FileoutputUtil;
import tools.Triple;

public class MapleCharacterUtil {

    //private static final Pattern namePattern = Pattern.compile("[a-zA-Z0-9]{4,12}");
    //private static final Pattern petPattern = Pattern.compile("[a-zA-Z0-9]{4,12}");
    public static boolean canCreateChar(final String name, final boolean gm) {
        return getIdByName(name) == -1 && isEligibleCharName(name, gm);
    }

    public static boolean isEligibleCharName(final String name, final boolean gm) {
        if (name.getBytes().length > 13) {
            return false;
        }
        if (gm) {
            return true;
        }
        if (name.getBytes().length < 3) {
            return false;
        }
        for (String z : GameConstants.RESERVED) {
            if (name.contains(z)) {
                return false;
            }
        }
        return true;
    }

    public static boolean canChangePetName(final String name) {
        for (String z : GameConstants.RESERVED) {
            if (name.contains(z)) {
                return false;
            }
        }
        return true;
    }

    public static String makeMapleReadable(final String in) {
        String wui = in.replace('I', 'i');
        wui = wui.replace('l', 'L');
        wui = wui.replace("rn", "Rn");
        wui = wui.replace("vv", "Vv");
        wui = wui.replace("VV", "Vv");
        return wui;
    }

    public static int getIdByName(final String name) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            final int id;
            try (PreparedStatement ps = con.prepareStatement("SELECT id FROM characters WHERE name = ?")) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        rs.close();
                        ps.close();
                        return -1;
                    }
                    id = rs.getInt("id");
                }
            }

            return id;
        } catch (SQLException e) {
            System.err.println("error 'getIdByName' " + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        return -1;
    }

    // -2 = An unknown error occured
    // -1 = Account not found on database
    // 0 = You do not have a second password set currently.
    // 1 = The password you have input is wrong
    // 2 = Password Changed successfully
    public static int Change_SecondPassword(final int accid, final String password, final String newpassword) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * from accounts where id = ?");
            ps.setInt(1, accid);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    rs.close();
                    ps.close();
                    return -1;
                }
                String secondPassword = rs.getString("2ndpassword");
                final String salt2 = rs.getString("salt2");
                if (secondPassword != null && salt2 != null) {
                    secondPassword = LoginCrypto.rand_r(secondPassword);
                } else if (secondPassword == null && salt2 == null) {
                    rs.close();
                    ps.close();
                    return 0;
                }
                if (!check_ifPasswordEquals(secondPassword, password, salt2)) {
                    rs.close();
                    ps.close();
                    return 1;
                }
            }
            ps.close();

            String SHA1hashedsecond;
            try {
                SHA1hashedsecond = LoginCryptoLegacy.encodeSHA1(newpassword);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                return -2;
            }
            ps = con.prepareStatement("UPDATE accounts set 2ndpassword = ?, salt2 = ? where id = ?");
            ps.setString(1, SHA1hashedsecond);
            ps.setString(2, null);
            ps.setInt(3, accid);

            if (!ps.execute()) {
                ps.close();
                return 2;
            }
            ps.close();
            return -2;
        } catch (SQLException e) {
            System.err.println("error 'getIdByName' " + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
            return -2;
        }
    }

    private static boolean check_ifPasswordEquals(final String passhash, final String pwd, final String salt) {
        // Check if the passwords are correct here. :B
        if (LoginCryptoLegacy.isLegacyPassword(passhash) && LoginCryptoLegacy.checkPassword(pwd, passhash)) {
            // Check if a password upgrade is needed.
            return true;
        } else if (salt == null && LoginCrypto.checkSha1Hash(passhash, pwd)) {
            return true;
        } else if (LoginCrypto.checkSaltedSha512Hash(passhash, pwd, salt)) {
            return true;
        }
        return false;
    }

    //id accountid gender
    public static Triple<Integer, Integer, Integer> getInfoByName(String name, int world) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            Triple<Integer, Integer, Integer> id;
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE name = ? AND world = ?")) {
                ps.setString(1, name);
                ps.setInt(2, world);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        rs.close();
                        ps.close();
                        return null;
                    }
                    id = new Triple<>(rs.getInt("id"), rs.getInt("accountid"), rs.getInt("gender"));
                }
            }
            return id;
        } catch (SQLException e) {
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        return null;
    }

    public static void setNXCodeUsed(String name, String code) throws SQLException {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("UPDATE nxcode SET `user` = ?, `valid` = 0 WHERE code = ?")) {
                ps.setString(1, name);
                ps.setString(2, code);
                ps.execute();
            }
        } catch (SQLException e) {
            System.err.println("setNXCodeUsed" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public static void sendNote(String to, String name, String msg, int fame) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO notes (`to`, `from`, `message`, `timestamp`, `gift`) VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, to);
                ps.setString(2, name);
                ps.setString(3, msg);
                ps.setLong(4, System.currentTimeMillis());
                ps.setInt(5, fame);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Unable to send note" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public static Triple<Boolean, Integer, Integer> getNXCodeInfo(String code) throws SQLException {
        Triple<Boolean, Integer, Integer> ret = null;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT `valid`, `type`, `item` FROM nxcode WHERE code LIKE ?")) {
                ps.setString(1, code);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ret = new Triple<>(rs.getInt("valid") > 0, rs.getInt("type"), rs.getInt("item"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("getNXCodeInfo" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        return ret;
    }
}
