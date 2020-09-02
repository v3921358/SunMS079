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
package handling.channel;

import database.DBConPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import tools.FileoutputUtil;

/**
 * 道场排行榜.
 */
public class MapleDojoRanking {

    private static final MapleDojoRanking instance = new MapleDojoRanking();
    private final List<DojoRankingInfo> ranks = new LinkedList<>();

    public static MapleDojoRanking getInstance() {
        return instance;
    }

    public void load() {
        if (ranks.isEmpty()) {
            reload();
        }
    }

    public List<DojoRankingInfo> getRank() {
        return ranks;
    }

    private void reload() {
        ranks.clear();
        PreparedStatement ps;
        ResultSet rs;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            ps = con.prepareStatement("SELECT * FROM dojorankings ORDER BY `rank` DESC LIMIT 50");
            rs = ps.executeQuery();
            while (rs.next()) {
                final DojoRankingInfo rank = new DojoRankingInfo(rs.getShort("rank"), rs.getString("name"), rs.getLong("time"));
                ranks.add(rank);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error handling dojo rankings: " + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public static class DojoRankingInfo {

        private final String name;
        private final short rank;
        private final long time;

        public DojoRankingInfo(short rank, String name, long time) {
            this.rank = rank;
            this.name = name;
            this.time = time;
        }

        public short getRank() {
            return rank;
        }

        public String getName() {
            return name;
        }

        public long getTime() {
            return time;
        }
    }
}
