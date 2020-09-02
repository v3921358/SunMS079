package handling.cashshop;

import database.DBConPool;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.PacketHelper;

public class CSMenuItem {

    private static final List<CSMenuItem> pictureItems = new LinkedList<>();

    public static void loadFromDb() {

        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (ResultSet rs = con.prepareStatement("SELECT * FROM cs_picture").executeQuery()) {
                while (rs.next()) {
                    pictureItems.add(new CSMenuItem(
                            rs.getInt("category"),
                            rs.getInt("subcategory"),
                            rs.getInt("parent"),
                            rs.getString("image"),
                            rs.getInt("sn"),
                            rs.getInt("itemid"),
                            rs.getByte("flag"),
                            rs.getInt("originalPrice"),
                            rs.getInt("salePrice"),
                            rs.getInt("quantity"),
                            rs.getInt("duration"),
                            rs.getInt("likes")));
                }
            }
        } catch (SQLException ex) {
            System.err.println("CSMenuItemloadFromDb" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        }

    }
    private int c, sc, p, i, sn, id, op, sp, qty, dur, likes;
    private final String img;
    private final byte flag;

    private CSMenuItem(int c, int sc, int p, String img, int sn, int id, byte flag, int op, int sp, int qty, int dur, int likes) {
        this.c = c;
        this.sc = sc;
        this.p = p;
        this.img = img;
        this.sn = sn;
        this.id = id;
        this.flag = flag;
        this.op = op;
        this.sp = sp;
        this.qty = qty;
        this.dur = dur;
        this.likes = likes;
    }

    public static void writeData(CSMenuItem csmi, MaplePacketLittleEndianWriter mplew) {
        mplew.writeInt(csmi.c);
        mplew.writeInt(csmi.sc);
        mplew.writeInt(csmi.p);
        mplew.writeMapleAsciiString(csmi.img); // TODO add check if cat != 4 write empty string
        mplew.writeInt(csmi.sn);
        mplew.writeInt(csmi.id);
        mplew.writeInt(1);
        mplew.writeInt(csmi.flag);
        mplew.writeInt(0);
        mplew.writeInt(0); // this one changes
        mplew.writeInt(csmi.op);
        mplew.write(HexTool.getByteArrayFromHexString("00 80 22 D6 94 EF C4 01")); // 1/1/2005
        mplew.writeLong(PacketHelper.MAX_TIME);
        mplew.write(HexTool.getByteArrayFromHexString("00 80 22 D6 94 EF C4 01")); // 1/1/2005
        mplew.writeLong(PacketHelper.MAX_TIME);
        mplew.writeInt(csmi.sp);
        mplew.writeInt(0);
        mplew.writeInt(csmi.qty);
        mplew.writeInt(csmi.dur);
        mplew.write(HexTool.getByteArrayFromHexString("01 00 01 00 01 00 00 00 01 00 02 00 00 00")); // flags maybe
        mplew.writeInt(csmi.likes);
        mplew.writeZeroBytes(20);
    }
}
