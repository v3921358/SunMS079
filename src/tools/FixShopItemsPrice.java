package tools;

import constants.ServerConstants;
import database.DBConPool;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import server.MapleItemInformationProvider;

/**
 *
 * @author Pungin
 */
public class FixShopItemsPrice {

    //private final Connection con = DatabaseConnection.getConnection();
    private static int times = 0;
    private static boolean init = false;
    private static List<Integer> list;

    private List<Integer> loadFromDB() {
        List<Integer> shopItemsId = new ArrayList<>();
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT itemid FROM shopitems ORDER BY itemid"); ResultSet rs = ps.executeQuery()) {
                int itemId = 0;
                while (rs.next()) {
                    if (itemId != rs.getInt("itemid")) {
                        itemId = rs.getInt("itemid");
                        //   System.out.println("商品道具ID:" + itemId);
                        shopItemsId.add(itemId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("无法载入商店" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        return shopItemsId;
    }

    private void changePrice(int itemId, int mode) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT shopid, price FROM shopitems WHERE itemid = ? ORDER BY price")) {
                ps.setInt(1, itemId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (mode == 1) {
                        while (rs.next()) {
                            if (ii.getPrice(itemId) != rs.getLong("price") && rs.getLong("price") != 0 && ii.getPrice(itemId) > 1) {
                                long newPirce = (long) ii.getPrice(itemId);
                                System.out.println("道具: " + MapleItemInformationProvider.getInstance().getName(itemId) + "道具ID: " + itemId + " 商店: " + rs.getInt("shopid") + " 价格: " + rs.getLong("price") + " 新价格:" + newPirce);
                                try (PreparedStatement pp = con.prepareStatement("UPDATE shopitems SET price = ? WHERE itemid = ? AND shopid = ?")) {
                                    pp.setLong(1, newPirce);
                                    pp.setInt(2, itemId);
                                    pp.setInt(3, rs.getInt("shopid"));
                                    pp.execute();
                                }
                                times++;
                            }
                        }
                    } else if (mode == 2) {
                        while (rs.next()) {
                            long newPirce = rs.getLong("price") * 2;
                            if (ii.isEquip(itemId)) {
                                System.out.println("道具: " + MapleItemInformationProvider.getInstance().getName(itemId) + "道具ID: " + itemId + " 商店: " + rs.getInt("shopid") + " 原价格: " + rs.getLong("price") + " 新价格:" + newPirce);
                                try (PreparedStatement pp = con.prepareStatement("UPDATE shopitems SET price = ? WHERE itemid = ? AND shopid = ?")) {
                                    pp.setLong(1, newPirce);
                                    pp.setInt(2, itemId);
                                    pp.setInt(3, rs.getInt("shopid"));
                                    pp.execute();
                                }
                                times++;
                            }
                            
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("处理商品, 道具ID:" + itemId + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public static void main(String[] args) {
        Main();
    }

    public static void Main() {
        int type = -1;
        while (type != 0) {

            do {
                System.out.println("請输入使用种类，0为离开本程序 1为修正价格异常 2为装备价格为目前2倍");
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    type = Integer.parseInt(br.readLine());
                } catch (Exception ex) {
                    type = 3;
                }
                if (type <= -1) {
                    type = 3;
                }
            } while (type != -1 && type != 0 && type != 1 && type != 2);
            if (type == 0) {
                System.exit(0);
            }
            Properties p = new Properties();
            try {
                p.load(new FileInputStream("settings.ini"));
            } catch (IOException ex) {
                System.out.println("载入设定党 settings.ini 失败");
                System.exit(0);
            }
            ServerConstants.SQL_PORT = p.getProperty("sql_port");
            ServerConstants.SQL_USER = p.getProperty("sql_user");
            ServerConstants.SQL_PASSWORD = p.getProperty("sql_password");
            ServerConstants.SQL_DATABASE = p.getProperty("sql_db");
            FixShopItemsPrice i = new FixShopItemsPrice();
            if (!init) {
                System.out.println("正在加载道具数据......");
                MapleItemInformationProvider.getInstance().runItems();
                init = true;
            }
            if (list == null) {
                System.out.println("正在读取商店内商品......");
                list = i.loadFromDB();
            }

            System.out.println("正在处理商店内商品价格......");
            times = 0;
            for (int ii : list) {
                i.changePrice(ii, type);
            }
            System.out.println("处理商品价格结束，共处理了" + times + "个道具。");
        }
    }
}
