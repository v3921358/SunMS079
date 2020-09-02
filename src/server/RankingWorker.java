package server;

import database.DBConPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import tools.FileoutputUtil;

public class RankingWorker {

    private final static Map<Integer, List<RankingInformation>> rankings = new HashMap<>();
    private final static Map<String, Integer> jobCommands = new HashMap<>();

    public static Integer getJobCommand(final String job) {
        return jobCommands.get(job);
    }

    public static Map<String, Integer> getJobCommands() {
        return jobCommands;
    }

    public static List<RankingInformation> getRankingInfo(final int job) {
        return rankings.get(job);
    }

    public static void run() {
        //System.out.println("Loading Rankings::");
        //long startTime = System.currentTimeMillis();
        loadJobCommands();
        try {
            //Connection con = DatabaseConnection.getConnection();
            updateRanking();
        } catch (Exception ex) {
            System.err.println("Could not update rankings" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        }
        //System.out.println("Done loading Rankings in " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds :::"); //keep
    }

    private static void updateRanking() throws Exception {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            StringBuilder sb = new StringBuilder("SELECT c.id, c.job, c.exp, c.level, c.name, c.jobRank, c.rank, c.fame");
            sb.append(" FROM characters AS c LEFT JOIN accounts AS a ON c.accountid = a.id WHERE c.gm = 0 AND a.banned = 0 AND c.level >= 30");
            sb.append(" ORDER BY c.level DESC , c.exp DESC , c.fame DESC , c.rank ASC");
            PreparedStatement ps;
            try (PreparedStatement charSelect = con.prepareStatement(sb.toString()); ResultSet rs = charSelect.executeQuery()) {
                ps = con.prepareStatement("UPDATE characters SET jobRank = ?, jobRankMove = ?, rank = ?, rankMove = ? WHERE id = ?");
                int rank = 0;
                final Map<Integer, Integer> rankMap = new LinkedHashMap<>();
                for (int i : jobCommands.values()) {
                    rankMap.put(i, 0); //job to rank
                    rankings.put(i, new ArrayList<RankingInformation>());
                }
                while (rs.next()) {
                    int job = rs.getInt("job");
                    if (!rankMap.containsKey(job / 100)) { //not supported.
                        continue;
                    }
                    int jobRank = rankMap.get(job / 100) + 1;
                    rankMap.put(job / 100, jobRank);
                    rank++;
                    rankings.get(-1).add(new RankingInformation(rs.getString("name"), job, rs.getInt("level"), rs.getLong("exp"), rank, rs.getInt("fame")));
                    rankings.get(job / 100).add(new RankingInformation(rs.getString("name"), job, rs.getInt("level"), rs.getLong("exp"), jobRank, rs.getInt("fame")));
                    ps.setInt(1, jobRank);
                    ps.setInt(2, rs.getInt("jobRank") - jobRank);
                    ps.setInt(3, rank);
                    ps.setInt(4, rs.getInt("rank") - rank);
                    ps.setInt(5, rs.getInt("id"));
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            ps.close();
        } catch (SQLException e) {
            System.err.println("updateRanking" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public static void loadJobCommands() {
        //messy, cleanup
        jobCommands.put("all", -1);
        jobCommands.put("beginner", 0);
        jobCommands.put("warrior", 1);
        jobCommands.put("magician", 2);
        jobCommands.put("bowman", 3);
        jobCommands.put("thief", 4);
        jobCommands.put("pirate", 5);
        jobCommands.put("noblesse", 10);
        jobCommands.put("dawnwarrior", 11);
        jobCommands.put("blazewizard", 12);
        jobCommands.put("windarcher", 13);
        jobCommands.put("nightwalker", 14);
        jobCommands.put("thunderbreaker", 15);
        jobCommands.put("legend", 20);
        jobCommands.put("aran", 21);
        jobCommands.put("evan", 22);
        jobCommands.put("mercedes", 23);
        jobCommands.put("phantom", 24);
        jobCommands.put("luminous", 27);
        jobCommands.put("citizen", 30);
        jobCommands.put("battlemage", 32);
        jobCommands.put("wildhunter", 33);
        jobCommands.put("xenon", 36);
        jobCommands.put("mechanic", 35);
        jobCommands.put("hayato", 41);
        jobCommands.put("kanna", 42);
        jobCommands.put("mihile", 50);
        jobCommands.put("kaiser", 60);
        jobCommands.put("angelicbuster", 65);
    }

    public static class RankingInformation {

        public String toString;
        public int rank;

        public RankingInformation(String name, int job, int level, long exp, int rank, int fame) {
            this.rank = rank;
            final StringBuilder builder = new StringBuilder("Rank ");
            builder.append(rank);
            builder.append(" : ");
            builder.append(name);
            builder.append(" - Level ");
            builder.append(level);
            builder.append(" ");
            builder.append(MapleCarnivalChallenge.getJobNameById(job));
            builder.append(" | ");
            builder.append(exp);
            builder.append(" EXP, ");
            builder.append(fame);
            builder.append(" Fame");
            this.toString = builder.toString(); //Rank 1 : KiDALex - Level 200 Blade Master | 0 EXP, 30000 Fame
        }

        @Override
        public String toString() {
            return toString;
        }
    }
}
