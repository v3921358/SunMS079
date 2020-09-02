/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.wztosql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.Pair;
import tools.StringUtil;

/**
 *
 * @author 7
 */
public class MonsterDropCreator {

    private static final int COMMON_ETC_RATE = 600000;
    private static final int SUPER_BOSS_ITEM_RATE = 300000;
    private static final int POTION_RATE = 20000;
    private static final int ARROWS_RATE = 25000;
    private static final int lastmonstercardid = 2388070;
    private static boolean addFlagData = false;
    protected static String monsterQueryData = "drop_data";
    protected static List<Pair<Integer, String>> itemNameCache = new ArrayList();
    protected static List<Pair<Integer, MobInfo>> mobCache = new ArrayList();
    protected static Map<Integer, Boolean> bossCache = new HashMap();

    public static void main(String[] args) throws FileNotFoundException, IOException, NotBoundException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException {
        if (System.getProperty("wzpath") == null) {
            System.setProperty("wzpath", "");
        }
        MapleData data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "wz/String.wz")).getData("MonsterBook.img");

        System.out.println("准备提取数据!");
        System.out.println("按任意键继续...");
        System.console().readLine();

        long currtime = System.currentTimeMillis();
        addFlagData = Boolean.parseBoolean(args[0]);

        System.out.println("载入: 物品名称.");
        getAllItems();
        System.out.println("载入: 怪物数据.");
        getAllMobs();

        StringBuilder sb = new StringBuilder();
        FileOutputStream out = new FileOutputStream("mobDrop.sql", true);

        /*for (Map.Entry e : getDropsNotInMonsterBook().entrySet()) {
         boolean first = true;

         sb.append("INSERT INTO ").append(monsterQueryData).append(" VALUES ");
         for (Object monsterdrop : (List)e.getValue()) {
         int itemid = (Integer)monsterdrop;
         int monsterId = ((Integer) e.getKey());
         int rate = getChance(itemid, monsterId, bossCache.containsKey(Integer.valueOf(monsterId)));

         if (rate <= 100000) {
         switch (monsterId) {
         case 9400121:
         rate *= 5;
         break;
         case 9400112:
         case 9400113:
         case 9400300:
         rate *= 10;
         }

         }
         for (int i = 0; i < multipleDropsIncrement(itemid, monsterId); i++) {
         if (first) {
         sb.append("(DEFAULT, ");
         first = false;
         } else {
         sb.append(", (DEFAULT, ");
         }
         sb.append(monsterId).append(", ");
         if (addFlagData) {
         sb.append("'', ");
         }
         sb.append(itemid).append(", ");
         sb.append("1, 1,");
         sb.append("0, ");
         int num = IncrementRate(itemid, i);
         sb.append(num == -1 ? rate : num);
         sb.append(")");
         first = false;
         }
         sb.append("\n\r");
         sb.append("-- Name : ");
         retriveNLogItemName(sb, itemid);
         sb.append("\n\r");
         }
         sb.append(";");
         sb.append("\n\r");

         out.write(sb.toString().getBytes());
         sb.delete(0, 2147483647);
         }*/
        System.out.println("载入: Drops from String.wz/MonsterBook.img.");
        for (MapleData dataz : data.getChildren()) {
            int monsterId = Integer.parseInt(dataz.getName());
            int idtoLog = monsterId;
            boolean first = true;

            if (monsterId == 9400408) {
                idtoLog = 9400409;
            }
            if (dataz.getChildByPath("reward").getChildren().size() > 0) {
                sb.append("INSERT INTO ").append(monsterQueryData).append(" VALUES ");
                for (MapleData drop : dataz.getChildByPath("reward")) {
                    int itemid = MapleDataTool.getInt(drop);
                    int rate = getChance(itemid, idtoLog, bossCache.containsKey(Integer.valueOf(idtoLog)));

                    for (Pair Pair : mobCache) {
                        if (((Integer) Pair.getLeft()) == monsterId) {
                            if ((((MobInfo) Pair.getRight()).getBoss() <= 0) || (rate > 100000)) {
                                break;
                            }

                            if (((MobInfo) Pair.getRight()).rateItemDropLevel() == 2) {
                                rate *= 10;
                                break;
                            }
                            if (((MobInfo) Pair.getRight()).rateItemDropLevel() == 3) {
                                switch (monsterId) {
                                    case 8810018:
                                        rate *= 48;
                                    case 8800002:
                                        rate *= 45;
                                        break;
                                    default:
                                        rate *= 30;
                                }

                            }
                            switch (monsterId) {
                                case 8860010:
                                case 9400265:
                                case 9400270:
                                case 9400273:
                                    rate *= 10;
                                    break;
                                case 9400294:
                                    rate *= 24;
                                    break;
                                case 9420522:
                                    rate *= 29;
                                    break;
                                case 9400409:
                                    rate *= 35;
                                    break;
                                case 9400287:
                                    rate *= 60;
                                    break;
                                default:
                                    rate *= 10;
                            }

                        }
                    }

                    for (int i = 0; i < multipleDropsIncrement(itemid, idtoLog); i++) {
                        if (first) {
                            sb.append("(DEFAULT, ");
                            first = false;
                        } else {
                            sb.append(", (DEFAULT, ");
                        }
                        sb.append(idtoLog).append(", ");
                        if (addFlagData) {
                            sb.append("'', ");
                        }
                        sb.append(itemid).append(", ");
                        sb.append("1, 1,");
                        sb.append("0, ");
                        int num = IncrementRate(itemid, i);
                        sb.append(num == -1 ? rate : num);
                        sb.append(")");
                        first = false;
                    }
                    sb.append("\n\r");
                    sb.append("-- Name : ");
                    retriveNLogItemName(sb, itemid);
                    sb.append("\n\r");
                }
                sb.append(";");
            }
            sb.append("\n\r");

            out.write(sb.toString().getBytes());
            sb.delete(0, 2147483647);
        }

        System.out.println("载入: 怪物书数据.");
        StringBuilder SQL = new StringBuilder();
        StringBuilder bookName = new StringBuilder();
        for (Pair Pair : itemNameCache) {
            if ((((Integer) Pair.getLeft()) >= 2380000) && (((Integer) Pair.getLeft()) <= lastmonstercardid)) {
                bookName.append((String) Pair.getRight());

                if (bookName.toString().contains(" Card")) {
                    bookName.delete(bookName.length() - 5, bookName.length());
                }
                for (Pair Pair_ : mobCache) {
                    if (((MobInfo) Pair_.getRight()).getName().equalsIgnoreCase(bookName.toString())) {
                        int rate = 1000;
                        if (((MobInfo) Pair_.getRight()).getBoss() > 0) {
                            rate *= 25;
                        }
                        SQL.append("INSERT INTO ").append(monsterQueryData).append(" VALUES ");
                        SQL.append("(DEFAULT, ");
                        SQL.append(Pair_.getLeft()).append(", ");
                        if (addFlagData) {
                            sb.append("'', ");
                        }
                        SQL.append(Pair.getLeft()).append(", ");
                        SQL.append("1, 1,");
                        SQL.append("0, ");
                        SQL.append(rate);
                        SQL.append(");\n\r");
                        SQL.append("-- 物品名 : ").append((String) Pair.getRight()).append("\n\r");
                        break;
                    }
                }
                bookName.delete(0, 2147483647);
            }
        }
        /*System.out.println("载入: 怪物卡数据.");
         SQL.append("\n\r");
         int i = 1;
         int lastmonsterbookid = 0;
         for (Pair Pair : itemNameCache) {
         if ((((Integer) Pair.getLeft()).intValue() >= 2380000) && (((Integer) Pair.getLeft()).intValue() <= lastmonstercardid)) {
         bookName.append((String) Pair.getRight());

         if (bookName.toString().contains(" Card")) {
         bookName.delete(bookName.length() - 5, bookName.length());
         }
         if (((Integer) Pair.getLeft()).intValue() != lastmonsterbookid) {
         for (Pair Pair_ : mobCache) {
         if (((MobInfo) Pair_.getRight()).getName().equalsIgnoreCase(bookName.toString())) {
         SQL.append("INSERT INTO ").append("monstercarddata").append(" VALUES (");
         SQL.append(i).append(", ");
         SQL.append(Pair.getLeft());
         SQL.append(", ");
         SQL.append(Pair_.getLeft()).append(");\n\r");
         lastmonsterbookid = ((Integer) Pair.getLeft()).intValue();
         i++;
         break;
         }
         }
         bookName.delete(0, 2147483647);
         }
         }
         }
         out.write(SQL.toString().getBytes());
         out.close();*/
        long time = System.currentTimeMillis() - currtime;
        time /= 1000L;

        System.out.println("Time taken : " + time);
    }

    private static void retriveNLogItemName(StringBuilder sb, int id) {
        for (Pair Pair : itemNameCache) {
            if (((Integer) Pair.getLeft()) == id) {
                sb.append((String) Pair.getRight());
                return;
            }
        }
        sb.append("MISSING STRING, ID : ");
        sb.append(id);
    }

    private static int IncrementRate(int itemid, int times) {
        if (times == 0) {
            if ((itemid == 1002357) || (itemid == 1002926) || (itemid == 1002927)) {
                return 999999;
            }
            if (itemid == 1122000) {
                return 999999;
            }
            if (itemid == 1002972) {
                return 999999;
            }
        } else if (times == 1) {
            if ((itemid == 1002357) || (itemid == 1002926) || (itemid == 1002927)) {
                return 999999;
            }
            if (itemid == 1122000) {
                return 999999;
            }
            if (itemid == 1002972) {
                return SUPER_BOSS_ITEM_RATE;
            }
        } else if (times == 2) {
            if ((itemid == 1002357) || (itemid == 1002926) || (itemid == 1002927)) {
                return SUPER_BOSS_ITEM_RATE;
            }
            if (itemid == 1122000) {
                return SUPER_BOSS_ITEM_RATE;
            }
        } else if (times == 3) {
            if ((itemid == 1002357) || (itemid == 1002926) || (itemid == 1002927)) {
                return SUPER_BOSS_ITEM_RATE;
            }
        } else if ((times == 4) && ((itemid == 1002357) || (itemid == 1002926) || (itemid == 1002927))) {
            return SUPER_BOSS_ITEM_RATE;
        }

        return -1;
    }

    private static int multipleDropsIncrement(int itemid, int mobid) {
        switch (itemid) {
            case 1002357:
            case 1002390:
            case 1002430:
            case 1002926:
            case 1002927:
                return 5;
            case 1122000:
                return 4;
            case 4021010:
                return 7;
            case 1002972:
                return 2;
            case 4000172:
                if (mobid == 7220001) {
                    return 8;
                }
                return 1;
            case 4000000:
            case 4000003:
            case 4000005:
            case 4000016:
            case 4000018:
            case 4000019:
            case 4000021:
            case 4000026:
            case 4000029:
            case 4000031:
            case 4000032:
            case 4000033:
            case 4000043:
            case 4000044:
            case 4000073:
            case 4000074:
            case 4000113:
            case 4000114:
            case 4000115:
            case 4000117:
            case 4000118:
            case 4000119:
            case 4000166:
            case 4000167:
            case 4000195:
            case 4000268:
            case 4000269:
            case 4000270:
            case 4000283:
            case 4000284:
            case 4000285:
            case 4000289:
            case 4000298:
            case 4000329:
            case 4000330:
            case 4000331:
            case 4000356:
            case 4000364:
            case 4000365:
                if ((mobid == 2220000) || (mobid == 3220000) || (mobid == 3220001) || (mobid == 4220000) || (mobid == 5220000) || (mobid == 5220002) || (mobid == 5220003) || (mobid == 6220000) || (mobid == 4000119) || (mobid == 7220000) || (mobid == 7220002) || (mobid == 8220000) || (mobid == 8220002) || (mobid == 8220003)) {
                    return 3;
                }
                return 1;
        }
        return 1;
    }

    private static int getChance(int id, int mobid, boolean boss) {
        switch (id / 10000) {
            case 100:
                switch (id) {
                    case 1002357:
                    case 1002390:
                    case 1002430:
                    case 1002905:
                    case 1002906:
                    case 1002926:
                    case 1002927:
                    case 1002972:
                        return SUPER_BOSS_ITEM_RATE;
                }
                return 1500;
            case 103:
                switch (id) {
                    case 1032062:
                        return 100;
                }
                return 1000;
            case 105:
            case 109:
                switch (id) {
                    case 1092049:
                        return 100;
                }
                return 700;
            case 104:
            case 106:
            case 107:
                switch (id) {
                    case 1072369:
                        return SUPER_BOSS_ITEM_RATE;
                }
                return 800;
            case 108:
            case 110:
                return 1000;
            case 112:
                switch (id) {
                    case 1122000:
                        return SUPER_BOSS_ITEM_RATE;
                    case 1122011:
                    case 1122012:
                        return 800000;
                }
            case 130:
            case 131:
            case 132:
            case 137:
                switch (id) {
                    case 1372049:
                        return 999999;
                }
                return 700;
            case 138:
            case 140:
            case 141:
            case 142:
            case 144:
                return 700;
            case 133:
            case 143:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
                return 500;
            case 204:
                switch (id) {
                    case 2049000:
                        return 150;
                }
                return 300;
            case 205:
                return 50000;
            case 206:
                return 30000;
            case 228:
                return 30000;
            case 229:
                switch (id) {
                    case 2290096:
                        return 800000;
                    case 2290125:
                        return 100000;
                }
                return 500;
            case 233:
                switch (id) {
                    case 2330007:
                        return 50;
                }
                return 500;
            case 400:
                switch (id) {
                    case 4000021:
                        return 50000;
                    case 4001094:
                        return 999999;
                    case 4001000:
                        return 5000;
                    case 4000157:
                        return 100000;
                    case 4001023:
                    case 4001024:
                        return 999999;
                    case 4000244:
                    case 4000245:
                        return 2000;
                    case 4001005:
                        return 5000;
                    case 4001006:
                        return 10000;
                    case 4000017:
                    case 4000082:
                        return 40000;
                    case 4000446:
                    case 4000451:
                    case 4000456:
                        return 10000;
                    case 4000459:
                        return POTION_RATE;
                    case 4000030:
                        return 60000;
                    case 4000339:
                        return 70000;
                    case 4000313:
                    case 4007000:
                    case 4007001:
                    case 4007002:
                    case 4007003:
                    case 4007004:
                    case 4007005:
                    case 4007006:
                    case 4007007:
                    case 4031456:
                        return 100000;
                    case 4001126:
                        return 500000;
                }
                switch (id / 1000) {
                    case 4000:
                    case 4001:
                        return COMMON_ETC_RATE;
                    case 4003:
                        return 200000;
                    case 4004:
                    case 4006:
                        return 10000;
                    case 4005:
                        return 1000;
                }

            case 401:
            case 402:
                switch (id) {
                    case 4020009:
                        return 5000;
                    case 4021010:
                        return SUPER_BOSS_ITEM_RATE;
                }
                return 9000;
            case 403:
                switch (id) {
                    case 4032024:
                        return 50000;
                    case 4032181:
                        return boss ? 999999 : SUPER_BOSS_ITEM_RATE;
                    case 4032025:
                    case 4032155:
                    case 4032156:
                    case 4032159:
                    case 4032161:
                    case 4032163:
                        return COMMON_ETC_RATE;
                    case 4032166:
                    case 4032167:
                    case 4032168:
                        return 10000;
                    case 4032151:
                    case 4032158:
                    case 4032164:
                    case 4032180:
                        return 2000;
                    case 4032152:
                    case 4032153:
                    case 4032154:
                        return 4000;
                }
                return 300;
            case 413:
                return 6000;
            case 416:
                return 6000;
        }
        switch (id / 1000000) {
            case 1:
                return 999999;
            case 2:
                switch (id) {
                    case 2000004:
                    case 2000005:
                        return boss ? 999999 : POTION_RATE;
                    case 2000006:
                        return boss ? 999999 : mobid == 9420540 ? 50000 : POTION_RATE;
                    case 2022345:
                        return boss ? 999999 : 3000;
                    case 2012002:
                        return 6000;
                    case 2020013:
                    case 2020015:
                        return boss ? 999999 : POTION_RATE;
                    case 2060000:
                    case 2060001:
                    case 2061000:
                    case 2061001:
                        return ARROWS_RATE;
                    case 2070000:
                    case 2070001:
                    case 2070002:
                    case 2070003:
                    case 2070004:
                    case 2070008:
                    case 2070009:
                    case 2070010:
                        return 500;
                    case 2070005:
                        return 400;
                    case 2070006:
                    case 2070007:
                        return 200;
                    case 2070012:
                    case 2070013:
                        return 1500;
                    case 2070019:
                        return 100;
                    case 2210006:
                        return 999999;
                }
                return POTION_RATE;
            case 3:
                switch (id) {
                    case 3010007:
                    case 3010008:
                        return 500;
                }
                return 2000;
        }
        System.out.println("未处理的数据, ID : " + id);
        return 999999;
    }

    private static void getAllItems() {
        MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "wz/String.wz"));

        List itemPairs = new ArrayList();

        MapleData itemsData = data.getData("Cash.img");
        for (MapleData itemFolder : itemsData.getChildren()) {
            int itemId = Integer.parseInt(itemFolder.getName());
            String itemName = MapleDataTool.getString("name", itemFolder, "NO-NAME");
            itemPairs.add(new Pair(itemId, itemName));
        }

        itemsData = data.getData("Consume.img");
        for (MapleData itemFolder : itemsData.getChildren()) {
            int itemId = Integer.parseInt(itemFolder.getName());
            String itemName = MapleDataTool.getString("name", itemFolder, "NO-NAME");
            itemPairs.add(new Pair(itemId, itemName));
        }

        itemsData = data.getData("Eqp.img").getChildByPath("Eqp");
        for (MapleData eqpType : itemsData.getChildren()) {
            for (MapleData itemFolder : eqpType.getChildren()) {
                int itemId = Integer.parseInt(itemFolder.getName());
                String itemName = MapleDataTool.getString("name", itemFolder, "NO-NAME");
                itemPairs.add(new Pair(itemId, itemName));
            }
        }

        itemsData = data.getData("Etc.img").getChildByPath("Etc");
        for (MapleData itemFolder : itemsData.getChildren()) {
            int itemId = Integer.parseInt(itemFolder.getName());
            String itemName = MapleDataTool.getString("name", itemFolder, "NO-NAME");
            itemPairs.add(new Pair(itemId, itemName));
        }

        itemsData = data.getData("Ins.img");
        for (MapleData itemFolder : itemsData.getChildren()) {
            int itemId = Integer.parseInt(itemFolder.getName());
            String itemName = MapleDataTool.getString("name", itemFolder, "NO-NAME");
            itemPairs.add(new Pair(itemId, itemName));
        }

        itemsData = data.getData("Pet.img");
        for (MapleData itemFolder : itemsData.getChildren()) {
            int itemId = Integer.parseInt(itemFolder.getName());
            String itemName = MapleDataTool.getString("name", itemFolder, "NO-NAME");
            itemPairs.add(new Pair(itemId, itemName));
        }
        itemNameCache.addAll(itemPairs);
    }

    public static void getAllMobs() {
        List itemPairs = new ArrayList();
        MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "wz/String.wz"));
        MapleDataProvider mobData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "wz/Mob.wz"));
        MapleData mob = data.getData("Mob.img");

        for (MapleData itemFolder : mob.getChildren()) {
            int id = Integer.parseInt(itemFolder.getName());
            try {
                MapleData monsterData = mobData.getData(StringUtil.getLeftPaddedStr(Integer.toString(id) + ".img", '0', 11));
                int boss = id == 8810018 ? 1 : MapleDataTool.getIntConvert("boss", monsterData.getChildByPath("info"), 0);

                if (boss > 0) {
                    bossCache.put(id, true);
                }

                MobInfo mobInfo = new MobInfo(boss, MapleDataTool.getIntConvert("rareItemDropLevel", monsterData.getChildByPath("info"), 0), MapleDataTool.getString("name", itemFolder, "NO-NAME"));

                itemPairs.add(new Pair(id, mobInfo));
            } catch (Exception fe) {
            }
        }
        mobCache.addAll(itemPairs);
    }

    public static class MobInfo {

        public int boss;
        public int rareItemDropLevel;
        public String name;

        public MobInfo(int boss, int rareItemDropLevel, String name) {
            this.boss = boss;
            this.rareItemDropLevel = rareItemDropLevel;
            this.name = name;
        }

        public int getBoss() {
            return boss;
        }

        public int rateItemDropLevel() {
            return rareItemDropLevel;
        }

        public String getName() {
            return name;
        }
    }

}
