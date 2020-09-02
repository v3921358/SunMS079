/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tools;

import database.DBConPool;
import handling.SendPacketOpcode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import tools.data.MaplePacketLittleEndianWriter;

public class CustomPlayerRankings {

    private static final CustomPlayerRankings instance = new CustomPlayerRankings();
    private final List<CustomRankingInfo> ranks = new LinkedList<>();
    private final int max = Integer.MAX_VALUE; //Max characters on ranks

    public static CustomPlayerRankings getInstance() {
        return instance;
    }

    public void load() {
        if (ranks.isEmpty()) {
            reload();
        }
    }

    public List<CustomRankingInfo> getRank() {
        return ranks;
    }

    public void reload() {
        ranks.clear();
        PreparedStatement ps;
        ResultSet rs;
        short rank = 1;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            ps = con.prepareStatement("SELECT * FROM characters WHERE `gm` = 0 ORDER BY `level` DESC LIMIT " + max);
            rs = ps.executeQuery();
            while (rs.next()) {
                final CustomRankingInfo rankfield = new CustomRankingInfo(rank, rs.getString("name"), rs.getInt("level"));
                ranks.add(rankfield);
                rank++;
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error handling custom rankings: " + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public static class CustomRankingInfo {

        private final String name;
        private final short rank;
        private final int level;

        public CustomRankingInfo(short rank, String name, int level) {
            this.rank = rank;
            this.name = name;
            this.level = level;
        }

        public short getRank() {
            return rank;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }
    }

    public byte[] customRanks(int npcid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
        mplew.write(0x50);
        mplew.writeInt(npcid);
        mplew.writeInt(getRank().size());
        for (CustomPlayerRankings.CustomRankingInfo info : getRank()) {
            mplew.writeShort(info.getRank());
            mplew.writeMapleAsciiString(info.getName());
            mplew.writeInt(info.getLevel());
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }

        return mplew.getPacket();
    }

    /*public static byte[] customRanks(int npcid, List<CustomPlayerRankings.CustomRankingInfo> all) {
     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

     mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
     mplew.write(80);
     mplew.writeInt(npcid);
     mplew.writeInt(all.size());
     for (CustomPlayerRankings.CustomRankingInfo info : all) {
     mplew.writeShort(info.getRank());
     mplew.writeMapleAsciiString(info.getName());
     mplew.writeInt(info.getLevel());
     mplew.writeInt(0);
     mplew.writeInt(0);
     mplew.writeInt(0);
     mplew.writeInt(0);
     }

     return mplew.getPacket();
     }*/
}
