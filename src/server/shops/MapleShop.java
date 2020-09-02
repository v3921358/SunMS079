package server.shops;

import client.MapleClient;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import constants.GameConstants;
import custom.MoonlightRevamp;
import custom.MoonlightRevamp.MoonlightShop;
import database.DBConPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.life.MapleLifeFactory;
import server.life.MapleNPC;
import tools.FileoutputUtil;
import tools.Pair;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class MapleShop {

    private static final Set<Integer> rechargeableItems = new LinkedHashSet();
    private final int id;
    private final int npcId;
    private final List<MapleShopItem> items = new LinkedList();
    private final List<Pair<Integer, String>> ranks = new ArrayList();

    static {
        for (int i = 2070000; i <= 2070018; i++) {
            rechargeableItems.add(i);
        }
        rechargeableItems.remove(2070014);
        rechargeableItems.remove(2070015);
        rechargeableItems.remove(2070016);
        rechargeableItems.remove(2070017);
        rechargeableItems.remove(2070018);

        for (int i = 2330000; i <= 2330005; i++) {
            rechargeableItems.add(i);
        }
        rechargeableItems.add(2331000);
        rechargeableItems.add(2332000);
    }

    public MapleShop(int id, int npcId) {
        this.id = id;
        this.npcId = npcId;
    }

    public void addItem(MapleShopItem item) {
        item.getItemId();
        this.items.add(item);
    }

    public List<MapleShopItem> getItems() {
        return this.items;
    }

    public void sendShop(MapleClient c) {
        MapleNPC npc = MapleLifeFactory.getNPC(getNpcId());
        if (npc == null || npc.getName().equals("MISSINGNO")) {
            c.getPlayer().dropMessage(1, "商店" + id + "找不到此代码为" + getNpcId() + "的Npc");
            return;
        } else {
            if (c.getPlayer().isAdmin()) {
                c.getPlayer().dropMessage("您已建立与商店" + id + "的连接");
            }
        }
        c.getPlayer().setShop(this);
        c.getSession().write(CField.NPCPacket.getNPCShop(getNpcId(), this, c));
    }

    public void sendShop(MapleClient c, int customNpc) {
        MapleNPC npc = MapleLifeFactory.getNPC(customNpc);
        if (npc == null || npc.getName().equals("MISSINGNO")) {
            c.getPlayer().dropMessage(1, "商店" + id + "找不到此代码为" + customNpc + "的Npc");
            return;
        } else {
            if (c.getPlayer().isAdmin()) {
                c.getPlayer().dropMessage("您已建立与商店" + id + "的连接");
            }
        }
        c.getPlayer().setShop(this);
        c.getSession().write(CField.NPCPacket.getNPCShop(customNpc, this, c));
    }

    public void buy(MapleClient c, short slot, int itemId, short quantity) {
        if ((itemId / 10000 == 190) && (!GameConstants.isMountItemAvailable(itemId, c.getPlayer().getJob()))) {
            c.getPlayer().dropMessage(1, "您无法够买这个道具。");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
//        int x = 0;
//        int index = -1;
//        for (Item i : c.getPlayer().getRebuy()) {
//            if (i.getItemId() == itemId) {
//                index = x;
//                break;
//            }
//            x++;
//        }
//        if (index >= 0) { 
//            Item i = c.getPlayer().getRebuy().get(index);
//            int price = (int) Math.max(Math.ceil(ii.getPrice(itemId) * (GameConstants.isRechargable(itemId) ? 1 : i.getQuantity())), 0.0D);
//            if ((price >= 0) && (c.getPlayer().getMeso() >= price)) {
//                if (MapleInventoryManipulator.checkSpace(c, itemId, i.getQuantity(), i.getOwner())) {
//                    c.getPlayer().gainMeso(-price, false);
//                    MapleInventoryManipulator.addbyItem(c, i);
//                    c.getPlayer().getRebuy().remove(index);
//                    c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, x));
//                } else {
//                    c.getPlayer().dropMessage(1, "你的背包已满.");
//                    c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
//                }
//            } else {
//                c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
//            }
//
//            return;
//        }
        MapleShopItem item = findById(itemId);
        //    MapleShopItem item = findById(slot);
        // quantity *= item.getQuantity();
        if (item == null) {
            System.out.println("不存在的道具");
        }
        if ((item != null) && (item.getPrice() > 0) && (item.getReqItem() == 0)) {
            if (item.getRank() >= 0) {
                boolean passed = true;
                int y = 0;
                for (Pair i : getRanks()) {
                    if ((c.getPlayer().haveItem(((Integer) i.left), 1, true, true)) && (item.getRank() >= y)) {
                        passed = true;
                        break;
                    }
                    y++;
                }
                if (!passed) {
                    c.getPlayer().dropMessage(1, "你需要一个更高的排名.");
                    c.getSession().write(CWvsContext.enableActions());
                    return;
                }
            }
            int price = GameConstants.isRechargable(itemId) ? item.getPrice() : item.getPrice() * quantity;
            if ((price >= 0) && (c.getPlayer().getMeso() >= price)) {
                if (MapleInventoryManipulator.checkSpace(c, itemId, quantity, "")) {
                    c.getPlayer().gainMeso(-price, false);
                    if (GameConstants.isPet(itemId)) {
                        MapleInventoryManipulator.addById(c, itemId, (short) (quantity * item.getQuantity()), "", MaplePet.createPet(itemId, MapleInventoryIdentifier.getInstance()), -1, false, "Bought from shop " + id + ", " + npcId + " on " + FileoutputUtil.CurrentReadable_Date());
                    } else {
                        if (GameConstants.isRechargable(itemId)) {
                            quantity = ii.getSlotMax(item.getItemId());
                        }
                        MapleInventoryManipulator.addById(c, itemId, (short) (quantity * item.getQuantity()), "Bought from shop " + this.id + ", " + this.npcId + " on " + FileoutputUtil.CurrentReadable_Date());
                    }
                } else {
                    c.getPlayer().dropMessage(1, "您的背包是满的，请整理下背包。");
                }
                c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
            }
        } else if ((item != null) && (item.getReqItem() > 0) && (quantity == 1) && (c.getPlayer().haveItem(item.getReqItem(), item.getReqItemQ(), false, true))) {
            if (MapleInventoryManipulator.checkSpace(c, itemId, quantity, "")) {
                MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(item.getReqItem()), item.getReqItem(), item.getReqItemQ(), false, false);
                if (GameConstants.isPet(itemId)) {
                    MapleInventoryManipulator.addById(c, itemId, (short) (quantity * item.getQuantity()), "", MaplePet.createPet(itemId, MapleInventoryIdentifier.getInstance()), -1, false, "Bought from shop " + id + ", " + npcId + " on " + FileoutputUtil.CurrentReadable_Date());
                } else {
                    if (GameConstants.isRechargable(itemId)) {
                        quantity = ii.getSlotMax(item.getItemId());
                    }
                    MapleInventoryManipulator.addById(c, itemId, quantity, "Bought from shop " + this.id + ", " + this.npcId + " on " + FileoutputUtil.CurrentReadable_Date());
                }
            } else {
                c.getPlayer().dropMessage(1, "您的背包是满的，请整理下背包。");
            }
            c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
        }
    }

    public void sell(MapleClient c, MapleInventoryType type, byte slot, short quantity) {
        if ((quantity == 65535) || (quantity == 0)) {
            quantity = 1;
        }
        Item item = c.getPlayer().getInventory(type).getItem(slot);
        if (item == null) {
            return;
        }

        if ((GameConstants.isThrowingStar(item.getItemId())) || (GameConstants.isBullet(item.getItemId()))) {
            quantity = item.getQuantity();
        }

        short iQuant = item.getQuantity();
        if (iQuant == 65535) {
            iQuant = 1;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if ((ii.cantSell(item.getItemId())) || (GameConstants.isPet(item.getItemId()))) {
            return;
        }

        List<Item> listRebuy = new ArrayList<>();
        if ((quantity <= iQuant) && (iQuant > 0)) {
            if (GameConstants.GMS) {
//                if (item.getQuantity() == quantity) {
//                    if (c.getPlayer().getRebuy().size() < 10) {
//                        c.getPlayer().getRebuy().add(item.copy());
//                    } else if (c.getPlayer().getRebuy().size() == 10) {
//                        for (int i = 1; i < 10; i++) {
//                            listRebuy.add(c.getPlayer().getRebuy().get(i));
//                        }
//                        listRebuy.add(item.copy());
//                        c.getPlayer().setRebuy(listRebuy);
//                    } else {
//                        int x = c.getPlayer().getRebuy().size();
//                        for (int i = x - 10; i < x; i++) {
//                            listRebuy.add(c.getPlayer().getRebuy().get(i));
//                        }
//                        c.getPlayer().setRebuy(listRebuy);
//                    }
//                } else {
//                    c.getPlayer().getRebuy().add(item.copyWithQuantity(quantity));
//                }
            }
            MapleInventoryManipulator.removeFromSlot(c, type, slot, quantity, false);
            double price;
            if ((GameConstants.isThrowingStar(item.getItemId())) || (GameConstants.isBullet(item.getItemId()))) {
                price = ii.getPrice(item.getItemId());//ii.getWholePrice(item.getItemId()) / ii.getSlotMax(item.getItemId());
            } else {
                price = ii.getPrice(item.getItemId());
            }
            int recvMesos = (int) Math.max(Math.ceil(price * quantity), 0.0D);
            if ((GameConstants.isThrowingStar(item.getItemId())) || (GameConstants.isBullet(item.getItemId()))) {
                recvMesos = recvMesos + ii.getWholePrice(item.getItemId());
            }
            if ((price != -1.0D) && (recvMesos > 0)) {
                c.getPlayer().gainMeso(recvMesos, false);
            }
            c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0x8, this, c, -1));
        }
    }

    public void recharge(MapleClient c, byte slot) {
        Item item = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);

        if ((item == null) || ((!GameConstants.isThrowingStar(item.getItemId())) && (!GameConstants.isBullet(item.getItemId())))) {
            return;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        short slotMax = ii.getSlotMax(item.getItemId());
        int skill = GameConstants.getMasterySkill(c.getPlayer().getJob());

        if (skill != 0) {
            slotMax = (short) (slotMax + c.getPlayer().getTotalSkillLevel(SkillFactory.getSkill(skill)) * 10);
        }
        if (item.getQuantity() < slotMax) {
            int price = (int) Math.round(ii.getPrice(item.getItemId()) * (slotMax - item.getQuantity()));
            if (c.getPlayer().getMeso() >= price) {
                item.setQuantity(slotMax);
                c.getSession().write(CWvsContext.InventoryPacket.updateInventorySlot(MapleInventoryType.USE, item, false));
                c.getPlayer().gainMeso(-price, false, false);
                c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0x8, this, c, -1));
            } else {
                c.getPlayer().dropMessage(1, "您的金币不足。");
            }
        }
    }

    protected MapleShopItem findById(int itemId) {
        for (MapleShopItem item : this.items) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }
        return null;
    }

    protected MapleShopItem findBySlot(short slot) {
        for (MapleShopItem item : this.items) {
            if (item.getSlot() == slot) {
                return item;
            }
        }
        return null;
    }

    public static MapleShop createFromDB(int id, boolean isShopId) {
        MapleShop ret = null;

        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement ps = con.prepareStatement(isShopId ? "SELECT * FROM shops WHERE shopid = ?" : "SELECT * FROM shops WHERE npcid = ?");
            int shopId, npcId = 0;
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                shopId = rs.getInt("shopid");
                npcId = rs.getInt("npcid");
                MapleNPC CheckNpc = MapleLifeFactory.getNPC(npcId);
                if (CheckNpc == null || CheckNpc.getName().equalsIgnoreCase("MISSINGNO")) {
                    npcId = 11000;
                    System.out.println("商店: " + shopId + " 的 NPC: " + rs.getInt("npcid") + " 不存在");
                }
                ret = new MapleShop(shopId, npcId);
                rs.close();
                ps.close();
            } else {
                rs.close();
                ps.close();
                return null;
            }
            ps = con.prepareStatement("SELECT * FROM shopitems WHERE shopid = ? ORDER BY position ASC");
            ps.setInt(1, shopId);
            rs = ps.executeQuery();
            List<Integer> recharges = new ArrayList<Integer>(rechargeableItems);
            while (rs.next()) {
                if (GameConstants.isThrowingStar(rs.getInt("itemid")) || GameConstants.isBullet(rs.getInt("itemid"))) {
                    MapleShopItem starItem = new MapleShopItem((short) 1, rs.getInt("itemid"), rs.getInt("price"));
                    ret.addItem(starItem);
                    if (rechargeableItems.contains(starItem.getItemId())) {
                        recharges.remove(Integer.valueOf(starItem.getItemId()));
                    }
                } else {
                    ret.addItem(new MapleShopItem((short) 1000, rs.getInt("itemid"), rs.getInt("price")));
                }
            }
            /*  ps = con.prepareStatement("SELECT * FROM shopitems WHERE shopid = ? ORDER BY position ASC");
             ps.setInt(1, shopId);
             rs = ps.executeQuery();
             List<Integer> recharges = new ArrayList(rechargeableItems);
             while (rs.next()) {
             if (ii.itemExists(rs.getInt("itemid"))) {
             if ((GameConstants.isThrowingStar(rs.getInt("itemid"))) || (GameConstants.isBullet(rs.getInt("itemid")))) {
             MapleShopItem starItem = new MapleShopItem(rs.getShort("buyable"), ii.getSlotMax(rs.getInt("itemid")), rs.getInt("itemid"), rs.getInt("price"), (short) rs.getInt("position"), rs.getInt("reqitem"), rs.getInt("reqitemq"), rs.getByte("rank"), rs.getInt("category"), rs.getInt("minLevel"), rs.getInt("expiration"), false);
             ret.addItem(starItem);
             if (rechargeableItems.contains(starItem.getItemId())) {
             recharges.remove(Integer.valueOf(starItem.getItemId()));
             }
             } else {
             ret.addItem(new MapleShopItem(rs.getShort("buyable"), rs.getShort("quantity"), rs.getInt("itemid"), rs.getInt("price"), (short) rs.getInt("position"), rs.getInt("reqitem"), rs.getInt("reqitemq"), rs.getByte("rank"), rs.getInt("category"), rs.getInt("minLevel"), rs.getInt("expiration"), false)); //todo potential
             }
             }
             }*/
            for (Integer recharge : recharges) {
                ret.addItem(new MapleShopItem((short) 1, ii.getSlotMax(recharge), recharge, 0, (short) 0, 0, 0, (byte) 0, 0, 0, 0, false));
            }
            rs.close();
            ps.close();

            ps = con.prepareStatement("SELECT * FROM shopranks WHERE shopid = ? ORDER BY rank ASC");
            ps.setInt(1, shopId);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (ii.itemExists(rs.getInt("itemid"))) {
                    ret.ranks.add(new Pair(rs.getInt("itemid"), rs.getString("name")));
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Could not load shop" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        return ret;
    }

    public static MapleShop moonlightShop(int npc) { //No ranks/rechargable items for now
        MapleShop moonlight = new MapleShop(MoonlightRevamp.shopId, npc);
        for (MoonlightShop item : MoonlightShop.values()) {
            moonlight.addItem(new MapleShopItem((short) item.getBuyable(), (short) 1, item.getItemId(), item.getPrice(), (short) 0, item.getReqItem(), item.getReqItemQ(), item.getRank(), item.getCategory(), item.getMinLevel(), item.getExpiration(), false));
        }
        return moonlight;
    }

    public int getNpcId() {
        return this.npcId;
    }

    public int getId() {
        return this.id;
    }

    public List<Pair<Integer, String>> getRanks() {
        return this.ranks;
    }
}
