package handling.login;

import constants.GameConstants;
import constants.ServerConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.Triple;

public class LoginInformationProvider {

    public enum JobType {

        UltimateAdventurer(-1, 0, 100000000, true, true, false, false, true, false, false, false),
        Resistance(-1, 3000, 931000000, true, true, false, false, true, false, false, false),
        Adventurer(1, 0, ServerConfig.mxjcs, false, true, false, false, false, false, false, false),
        Cygnus(0, 1000, ServerConfig.qstcs, false, true, false, false, false, true, false, false),
        Aran(2, 2000, ServerConfig.zscs, true, true, false, false, true, false, false, false),
        Evan(4, 2001, 900010000, true, true, false, false, true, false, false, false),//evan starter map - need to test tutorial
        Mercedes(5, 2002, 910150000, false, false, false, false, false, false, false, false),//101050000 - 910150000
        Demon(6, 3001, 931050310, false, false, true, false, false, false, false, false),
        Phantom(7, 2003, 915000000, false, true, false, false, false, true, false, false),
        DualBlade(8, 0, 103050900, false, true, false, false, false, false, false, false),
        Mihile(9, 5000, 913070000, true, true, false, false, true, false, false, false),
        Luminous(10, 2004, 101000000, false, true, false, false, false, true, false, false),//Ellinia atm TODO tutorial
        Kaiser(11, 6000, 0, false, true, false, false, false, false, false, false),
        AngelicBuster(12, 6001, 940011000, false, true, false, false, false, false, false, false),//400000000 - 940011000 - town now TODO tutorial
        Cannoneer(13, 0, 0, false, true, false, false, false, false, false, false),
        Xenon(14, 3002, 931050920, true, true, true, false, false, false, false, false),
        Zero(15, 10112, 100000000, false, true, false, false, false, true, false, false),//321000000 = zero starter map
        Jett(16, 0, 552000050, false, false, false, false, false, true, false, false),//End map for tutorial
        Hayato(17, 4001, 807000000, true, true, false, true, false, false, false, false),//half stater map TODO real tutorial
        Kanna(18, 4002, 807040000, true, true, false, true, false, false, false, false),
        BeastTamer(19, 11212, 866135000, false, true, true, false, false, false, true, true);//custom tutorial as original tut is borinq
        public int type, id, map;
        public boolean hairColor, skinColor, faceMark, hat, bottom, cape, ears, tail;

        private JobType(int type, int id, int map, boolean hairColor, boolean skinColor, boolean faceMark, boolean hat, boolean bottom, boolean cape, boolean ears, boolean tail) {
            this.type = type;
            this.id = id;
            this.map = map;//map
            this.hairColor = hairColor;
            this.skinColor = skinColor;
            this.faceMark = faceMark;
            this.hat = hat;
            this.bottom = bottom;
            this.cape = cape;
            this.ears = ears;
            this.tail = tail;
        }

        public static JobType getByType(int g) {
            if (g == JobType.Cannoneer.type) {
                return JobType.Adventurer;
            }
            for (JobType e : JobType.values()) {
                if (e.type == g) {
                    return e;
                }
            }
            return null;
        }

        public static JobType getById(int g) {
            if (g == JobType.Adventurer.id) {
                return JobType.Adventurer;
            }
            if (g == 508) {
                return JobType.Jett;
            }
            for (JobType e : JobType.values()) {
                if (e.id == g) {
                    return e;
                }
            }
            return null;
        }
    }
    private final static LoginInformationProvider instance = new LoginInformationProvider();
    protected final List<String> ForbiddenName = new ArrayList<>();
    //gender, val, job
    protected final Map<Triple<Integer, Integer, Integer>, List<Integer>> makeCharInfo = new HashMap<>();
    //0 = eyes 1 = hair 2 = haircolor 3 = skin 4 = top 5 = bottom 6 = shoes 7 = weapon

    public static LoginInformationProvider getInstance() {
        return instance;
    }

    protected LoginInformationProvider() {
        final MapleDataProvider prov = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/Etc.wz"));
        MapleData nameData = prov.getData("ForbiddenName.img");
        for (final MapleData data : nameData.getChildren()) {
            ForbiddenName.add(MapleDataTool.getString(data));
        }
        nameData = prov.getData("Curse.img");
        for (final MapleData data : nameData.getChildren()) {
            ForbiddenName.add(MapleDataTool.getString(data).split(",")[0]);
        }
        final MapleData infoData = prov.getData("MakeCharInfo.img");
        final MapleData data = infoData.getChildByPath("Info");
        for (MapleData dat : infoData) {
            try {
                final int type;
                if (dat.getName().equals("000_1")) {
                    type = JobType.DualBlade.type;
                } else {
                    type = JobType.getById(Integer.parseInt(dat.getName())).type;
                }
                for (MapleData d : dat) {
                    int val;
                    if (d.getName().contains("female")) {
                        val = 1;
                    } else if (d.getName().contains("male")) {
                        val = 0;
                    } else {
                        continue;
                    }
                    for (MapleData da : d) {
                        int index;
                        Triple<Integer, Integer, Integer> key;
                        index = Integer.parseInt(da.getName());
                        key = new Triple<>(val, index, type);
                        List<Integer> our = makeCharInfo.get(key);
                        if (our == null) {
                            our = new ArrayList<>();
                            makeCharInfo.put(key, our);
                        }
                        for (MapleData dd : da) {
                            if (dd.getName().equalsIgnoreCase("color")) {
                                for (MapleData dda : dd) {
                                    for (MapleData ddd : dda) {
                                        our.add(MapleDataTool.getInt(ddd, -1));
                                    }
                                }
                            } else {
                                try {
                                    our.add(MapleDataTool.getInt(dd, -1));
                                } catch (Exception ex) { //probably like color
                                    for (MapleData dda : dd) {
                                        for (MapleData ddd : dda) {
                                            our.add(MapleDataTool.getInt(ddd, -1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (NumberFormatException | NullPointerException e) {
            }
        }
        /*final MapleData uA = infoData.getChildByPath("UltimateAdventurer");
         for (MapleData dat : uA) {
         final Triple<Integer, Integer, Integer> key = new Triple<>(-1, Integer.parseInt(dat.getName()), JobType.UltimateAdventurer.type);
         List<Integer> our = makeCharInfo.get(key);
         if (our == null) {
         our = new ArrayList<>();
         makeCharInfo.put(key, our);
         }
         for (MapleData d : dat) {
         our.add(MapleDataTool.getInt(d, -1));
         }
         }*/
    }

    public static boolean isExtendedSpJob(int jobId) {
        return GameConstants.isSeparatedSp(jobId);
    }

    public final boolean isForbiddenName(final String in) {
        for (final String name : ForbiddenName) {
            if (in.toLowerCase().contains(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public final boolean isEligibleItem(final int gender, final int val, final int job, final int item) {
        if (item < 0) {
            return false;
        }
        final Triple<Integer, Integer, Integer> key = new Triple<>(gender, val, job);
        final List<Integer> our = makeCharInfo.get(key);
        if (our == null) {
            return false;
        }
        return our.contains(item);
    }
}
