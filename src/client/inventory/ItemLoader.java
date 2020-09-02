package client.inventory;

import constants.GameConstants;
import database.DBConPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import server.MapleItemInformationProvider;
import tools.FileoutputUtil;
import tools.Pair;

public enum ItemLoader {

    INVENTORY("inventoryitems", "inventoryequipment", 0, "characterid"),
    STORAGE("inventoryitems", "inventoryequipment", 1, "accountid"),
    CASHSHOP("csitems", "csequipment", 2, "accountid"),
    HIRED_MERCHANT("hiredmerchitems", "hiredmerchequipment", 5, "packageid"),
    PACKAGE("dueyitems", "dueyequipment", 6, "packageid");
    private final int value;
    private final String table, table_equip, arg;

    private ItemLoader(String table, String table_equip, int value, String arg) {
        this.table = table;
        this.table_equip = table_equip;
        this.value = value;
        this.arg = arg;
    }

    public int getValue() {
        return value;
    }

    //does not need connection con to be auto commit
    public Map<Long, Pair<Item, MapleInventoryType>> loadItems(boolean login, int id) throws SQLException {
        Map<Long, Pair<Item, MapleInventoryType>> items = new LinkedHashMap<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM `");
            query.append(table);
            query.append("` LEFT JOIN `");
            query.append(table_equip);
            query.append("` USING (`inventoryitemid`) WHERE `type` = ?");
            query.append(" AND `");
            query.append(arg);
            query.append("` = ?");

            if (login) {
                query.append(" AND `inventorytype` = ");
                query.append(MapleInventoryType.EQUIPPED.getType());
            }
            ps = con.prepareStatement(query.toString());
            ps.setInt(1, value);
            ps.setInt(2, id);
            rs = ps.executeQuery();
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            while (rs.next()) {
                if (!ii.itemExists(rs.getInt("itemid"))) { //EXPENSIVE
                    continue;
                }
                MapleInventoryType mit = MapleInventoryType.getByType(rs.getByte("inventorytype"));

                if (mit.equals(MapleInventoryType.EQUIP) || mit.equals(MapleInventoryType.EQUIPPED)) {
                    Equip equip = new Equip(rs.getInt("itemid"), rs.getShort("position"), rs.getInt("uniqueid"), rs.getShort("flag"));
                    if (!login && equip.getPosition() != -55) { //monsterbook
                        equip.setQuantity((short) 1);
                        equip.setInventoryId(rs.getLong("inventoryitemid"));
                        equip.setOwner(rs.getString("owner"));
                        equip.setExpiration(rs.getLong("expiredate"));
                        equip.setUpgradeSlots(rs.getByte("upgradeslots"));
                        equip.setLevel(rs.getByte("level"));
                        equip.setStr(rs.getShort("str"));
                        equip.setDex(rs.getShort("dex"));
                        equip.setInt(rs.getShort("int"));
                        equip.setLuk(rs.getShort("luk"));
                        equip.setHp(rs.getShort("hp"));
                        equip.setMp(rs.getShort("mp"));
                        equip.setWatk(rs.getShort("watk"));
                        equip.setMatk(rs.getShort("matk"));
                        equip.setWdef(rs.getShort("wdef"));
                        equip.setMdef(rs.getShort("mdef"));
                        equip.setAcc(rs.getShort("acc"));
                        equip.setAvoid(rs.getShort("avoid"));
                        equip.setHands(rs.getShort("hands"));
                        equip.setSpeed(rs.getShort("speed"));
                        equip.setJump(rs.getShort("jump"));
                        equip.setViciousHammer(rs.getByte("ViciousHammer"));
                        equip.setItemEXP(rs.getLong("itemEXP"));
                        equip.setGMLog(rs.getString("GM_Log"));
                        equip.setDurability(rs.getInt("durability"));
                        equip.setEnhance(rs.getByte("enhance"));
                        equip.setPotential1(rs.getInt("potential1"));
                        equip.setPotential2(rs.getInt("potential2"));
                        equip.setPotential3(rs.getInt("potential3"));
                        equip.setBonusPotential1(rs.getInt("potential4"));
                        equip.setBonusPotential2(rs.getInt("potential5"));
                        equip.setBonusPotential3(rs.getInt("potential6"));
                        equip.setFusionAnvil(rs.getInt("fusionAnvil"));
                        equip.setSocket1(rs.getInt("socket1"));
                        equip.setSocket2(rs.getInt("socket2"));
                        equip.setSocket3(rs.getInt("socket3"));
                        equip.setGiftFrom(rs.getString("sender"));
                        equip.setIncSkill(rs.getInt("incSkill"));
                        equip.setPVPDamage(rs.getShort("pvpDamage"));
                        equip.setCharmEXP(rs.getShort("charmEXP"));
                        equip.setEnhanctBuff(rs.getByte("enhanctBuff"));
                        equip.setReqLevel(rs.getByte("reqLevel"));
                        equip.setYggdrasilWisdom(rs.getByte("yggdrasilWisdom"));
                        equip.setFinalStrike(rs.getByte("finalStrike") > 0);
                        equip.setBossDamage(rs.getByte("bossDamage"));
                        equip.setIgnorePDR(rs.getByte("ignorePDR"));
                        equip.setTotalDamage(rs.getByte("totalDamage"));
                        equip.setAllStat(rs.getByte("allStat"));
                        equip.setKarmaCount(rs.getByte("karmaCount"));
                        if (equip.getCharmEXP() < 0) { //has not been initialized yet
                            equip.setCharmEXP(((Equip) ii.getEquipById(equip.getItemId())).getCharmEXP());
                        }
                        if (equip.getUniqueId() > -1) {
                            if (GameConstants.isEffectRing(rs.getInt("itemid"))) {
                                MapleRing ring = MapleRing.loadFromDb(equip.getUniqueId(), mit.equals(MapleInventoryType.EQUIPPED));
                                if (ring != null) {
                                    equip.setRing(ring);
                                }
                            } else if (equip.getItemId() / 10000 == 166) {
                                MapleAndroid ring = MapleAndroid.loadFromDb(equip.getItemId(), equip.getUniqueId());
                                if (ring != null) {
                                    equip.setAndroid(ring);
                                }
                            }
                        }
                    }
                    items.put(rs.getLong("inventoryitemid"), new Pair<>(equip.copy(), mit));
                } else {
                    Item item = new Item(rs.getInt("itemid"), rs.getShort("position"), rs.getShort("quantity"), rs.getShort("flag"), rs.getInt("uniqueid"));
                    item.setOwner(rs.getString("owner"));
                    item.setInventoryId(rs.getLong("inventoryitemid"));
                    item.setExpiration(rs.getLong("expiredate"));
                    item.setGMLog(rs.getString("GM_Log"));
                    item.setGiftFrom(rs.getString("sender"));
                    //item.setExp(rs.getInt("exp")); //TODO: test
                    if (GameConstants.isPet(item.getItemId())) {
                        if (item.getUniqueId() > -1) {
                            MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(), item.getPosition());
                            if (pet != null) {
                                item.setPet(pet);
                            }
                        } else {
                            //O_O hackish fix
                            item.setPet(MaplePet.createPet(item.getItemId(), MapleInventoryIdentifier.getInstance()));
                        }
                    }
                    items.put(rs.getLong("inventoryitemid"), new Pair<>(item.copy(), mit));
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return items;
    }

    public void saveItems(List<Pair<Item, MapleInventoryType>> items, int id) throws SQLException {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            saveItems(items, con, id);
        }
    }

    public void saveItems(List<Pair<Item, MapleInventoryType>> items, final Connection con, int id) throws SQLException {
        PreparedStatement ps = null;
        PreparedStatement pse = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append("DELETE FROM `");
            query.append(table);
            query.append("` WHERE `type` = ? AND `");
            query.append(arg);
            query.append("` = ?");

            ps = con.prepareStatement(query.toString());
            ps.setInt(1, value);
            ps.setInt(2, id);
            ps.executeUpdate();
            ps.close();
            if (items == null) {
                return;
            }
            StringBuilder query_2 = new StringBuilder("INSERT INTO `");
            query_2.append(table);
            query_2.append("` (");
            query_2.append(arg);
            query_2.append(", itemid, inventorytype, position, quantity, owner, GM_Log, uniqueid, expiredate, flag, `type`, sender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps = con.prepareStatement(query_2.toString(), Statement.RETURN_GENERATED_KEYS);
            String valueStr = "";
            int values = 44;
            for (int i = 0; i < values; i++) {
                if (i == (values - 1)) {
                    valueStr += "?";
                } else {
                    valueStr += "?, ";
                }
            }
            pse = con.prepareStatement("INSERT INTO " + table_equip + " VALUES (DEFAULT, " + valueStr + ")");
            final Iterator<Pair<Item, MapleInventoryType>> iter = items.iterator();
            Pair<Item, MapleInventoryType> pair;
            while (iter.hasNext()) {
                pair = iter.next();
                Item item = pair.getLeft();
                MapleInventoryType mit = pair.getRight();
                if (item.getPosition() == -55) {
                    continue;
                }
                ps.setInt(1, id);
                ps.setInt(2, item.getItemId());
                ps.setInt(3, mit.getType());
                ps.setInt(4, item.getPosition());
                ps.setInt(5, item.getQuantity());
                ps.setString(6, item.getOwner());
                ps.setString(7, item.getGMLog());
                if (item.getPet() != null) { //expensif?
                    //item.getPet().saveToDb();
                    ps.setInt(8, Math.max(item.getUniqueId(), item.getPet().getUniqueId()));
                } else {
                    ps.setInt(8, item.getUniqueId());
                }
                ps.setLong(9, item.getExpiration());
                ps.setShort(10, item.getFlag());
                ps.setByte(11, (byte) value);
                ps.setString(12, item.getGiftFrom());

                ps.executeUpdate();
                final long iid;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        rs.close();
                        continue;
                    }
                    iid = rs.getLong(1);
                }

                item.setInventoryId(iid);
                if (mit.equals(MapleInventoryType.EQUIP) || mit.equals(MapleInventoryType.EQUIPPED)) {
                    Equip equip = (Equip) item;
                    int i = 0;
                    pse.setLong(++i, iid);
                    pse.setInt(++i, equip.getUpgradeSlots());
                    pse.setInt(++i, equip.getLevel());
                    pse.setInt(++i, equip.getStr());
                    pse.setInt(++i, equip.getDex());
                    pse.setInt(++i, equip.getInt());
                    pse.setInt(++i, equip.getLuk());
                    pse.setInt(++i, equip.getHp());
                    pse.setInt(++i, equip.getMp());
                    pse.setInt(++i, equip.getWatk());
                    pse.setInt(++i, equip.getMatk());
                    pse.setInt(++i, equip.getWdef());
                    pse.setInt(++i, equip.getMdef());
                    pse.setInt(++i, equip.getAcc());
                    pse.setInt(++i, equip.getAvoid());
                    pse.setInt(++i, equip.getHands());
                    pse.setInt(++i, equip.getSpeed());
                    pse.setInt(++i, equip.getJump());
                    pse.setInt(++i, equip.getViciousHammer());
                    pse.setLong(++i, equip.getItemEXP());
                    pse.setInt(++i, equip.getDurability());
                    pse.setByte(++i, equip.getEnhance());
                    pse.setInt(++i, equip.getPotential1());
                    pse.setInt(++i, equip.getPotential2());
                    pse.setInt(++i, equip.getPotential3());
                    pse.setInt(++i, equip.getBonusPotential1());
                    pse.setInt(++i, equip.getBonusPotential2());
                    pse.setInt(++i, equip.getBonusPotential3());
                    pse.setInt(++i, equip.getFusionAnvil());
                    pse.setInt(++i, equip.getSocket1());
                    pse.setInt(++i, equip.getSocket2());
                    pse.setInt(++i, equip.getSocket3());
                    pse.setInt(++i, equip.getIncSkill());
                    pse.setShort(++i, equip.getCharmEXP());
                    pse.setShort(++i, equip.getPVPDamage());
                    pse.setByte(++i, equip.getEnhanctBuff());
                    pse.setByte(++i, equip.getReqLevel());
                    pse.setByte(++i, equip.getYggdrasilWisdom());
                    pse.setByte(++i, (byte) (equip.getFinalStrike() ? 1 : 0));
                    pse.setByte(++i, equip.getBossDamage());
                    pse.setByte(++i, equip.getIgnorePDR());
                    pse.setByte(++i, equip.getTotalDamage());
                    pse.setByte(++i, equip.getAllStat());
                    pse.setByte(++i, equip.getKarmaCount());
                    pse.executeUpdate();
                }
            }
            pse.close();
            ps.close();
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (pse != null) {
                pse.close();
            }
        }

    }
}
