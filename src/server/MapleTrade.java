package server;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.ServerConstants;
import handling.world.World;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import server.commands.CommandProcessor;
import tools.FileoutputUtil;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.PlayerShopPacket;

public class MapleTrade {

    private MapleTrade partner = null;
    private final List<Item> items = new LinkedList();
    private List<Item> exchangeItems;
    private int meso = 0;
    private int exchangeMeso = 0;
    private boolean locked = false;
    private boolean inTrade = false;
    private final WeakReference<MapleCharacter> chr;
    private final byte tradingslot;

    public MapleTrade(byte tradingslot, MapleCharacter chr) {
        this.tradingslot = tradingslot;
        this.chr = new WeakReference(chr);
    }

    public final void CompleteTrade() {
        if (this.exchangeItems != null) {
            List<Item> itemz = new LinkedList(this.exchangeItems);
            for (Item item : itemz) {
                short flag = item.getFlag();

                if (ItemFlag.KARMA_EQ.check(flag)) {
                    item.setFlag((short) (flag - ItemFlag.KARMA_EQ.getValue()));
                } else if (ItemFlag.KARMA_USE.check(flag)) {
                    item.setFlag((short) (flag - ItemFlag.KARMA_USE.getValue()));
                }
                MapleInventoryManipulator.addFromDrop(this.chr.get().getClient(), item, false);
            }
            this.exchangeItems.clear();
        }
        if (this.exchangeMeso > 0) {
            this.chr.get().gainMeso(this.exchangeMeso - GameConstants.getTaxAmount(this.exchangeMeso), false, false);
        }
        this.exchangeMeso = 0;

        this.chr.get().getClient().getSession().write(CField.InteractionPacket.TradeMessage(this.tradingslot, (byte) 8));
    }

    public final void cancel(MapleClient c, MapleCharacter chr) {
        cancel(c, chr, 0);
    }

    public final void cancel(MapleClient c, MapleCharacter chr, int unsuccessful) {
        if (this.items != null) {
            List<Item> itemz = new LinkedList(this.items);
            for (Item item : itemz) {
                MapleInventoryManipulator.addFromDrop(c, item, false);
            }
            this.items.clear();
        }
        if (this.meso > 0) {
            chr.gainMeso(this.meso, false, false);
        }
        this.meso = 0;

        c.getSession().write(CField.InteractionPacket.getTradeCancel(this.tradingslot, unsuccessful));
    }

    public final boolean isLocked() {
        return this.locked;
    }

    public final void setMeso(int meso) {
        if ((this.locked) || (this.partner == null) || (meso <= 0) || (this.meso + meso <= 0)) {
            return;
        }
        if (this.chr.get().getMeso() >= meso) {
            this.chr.get().gainMeso(-meso, false, false);
            this.meso += meso;
            this.chr.get().getClient().getSession().write(CField.InteractionPacket.getTradeMesoSet((byte) 0, this.meso));
            if (this.partner != null) {
                this.partner.getChr().getClient().getSession().write(CField.InteractionPacket.getTradeMesoSet((byte) 1, this.meso));
            }
        }
    }

    public final void addItem(Item item) {
        if ((this.locked) || (this.partner == null)) {
            return;
        }
        this.items.add(item);
        this.chr.get().getClient().getSession().write(CField.InteractionPacket.getTradeItemAdd((byte) 0, item));
        if (this.partner != null) {
            this.partner.getChr().getClient().getSession().write(CField.InteractionPacket.getTradeItemAdd((byte) 1, item));
        }
    }

    public final void chat(String message) throws Exception {
        if (!CommandProcessor.processCommand(chr.get().getClient(), message, ServerConstants.CommandType.TRADE)) {
            this.chr.get().dropMessage(-2, this.chr.get().getName() + " : " + message);
            if (this.partner != null) {
                this.partner.getChr().getClient().getSession().write(PlayerShopPacket.shopChat(this.chr.get().getName() + " : " + message, 1));
            }
        }
        if (this.chr.get().getClient().isMonitored()) {
            World.Broadcast.broadcastGMMessage(CWvsContext.broadcastMsg(6, this.chr.get().getName() + " said in trade with " + this.partner.getChr().getName() + ": " + message));
        } else if ((this.partner != null) && (this.partner.getChr() != null) && (this.partner.getChr().getClient().isMonitored())) {
            World.Broadcast.broadcastGMMessage(CWvsContext.broadcastMsg(6, this.chr.get().getName() + " said in trade with " + this.partner.getChr().getName() + ": " + message));
        }
    }

    public final void chatAuto(String message) {
        this.chr.get().dropMessage(-2, message);
        if (this.partner != null) {
            this.partner.getChr().getClient().getSession().write(PlayerShopPacket.shopChat(message, 1));
        }
        if (this.chr.get().getClient().isMonitored()) {
            World.Broadcast.broadcastGMMessage(CWvsContext.broadcastMsg(6, this.chr.get().getName() + " said in trade [Automated] with " + this.partner.getChr().getName() + ": " + message));
        } else if ((this.partner != null) && (this.partner.getChr() != null) && (this.partner.getChr().getClient().isMonitored())) {
            World.Broadcast.broadcastGMMessage(CWvsContext.broadcastMsg(6, this.chr.get().getName() + " said in trade [Automated] with " + this.partner.getChr().getName() + ": " + message));
        }
    }

    public final MapleTrade getPartner() {
        return this.partner;
    }

    public final void setPartner(MapleTrade partner) {
        if (this.locked) {
            return;
        }
        this.partner = partner;
    }

    public final MapleCharacter getChr() {
        return this.chr.get();
    }

    public final int getNextTargetSlot() {
        if (this.items.size() >= 9) {
            return -1;
        }
        int ret = 1;
        for (Item item : this.items) {
            if (item.getPosition() == ret) {
                ret++;
            }
        }
        return ret;
    }

    public boolean inTrade() {
        return this.inTrade;
    }

    public final boolean setItems(MapleClient c, Item item, byte targetSlot, int quantity) {
        int target = getNextTargetSlot();
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if ((this.partner == null) || (target == -1) || (GameConstants.isPet(item.getItemId())) || (isLocked()) || ((GameConstants.getInventoryType(item.getItemId()) == MapleInventoryType.EQUIP) && (quantity != 1))) {
            return false;
        }
        short flag = item.getFlag();
        if ((ItemFlag.UNTRADABLE.check(flag)) || (ItemFlag.LOCK.check(flag))) {
            c.getSession().write(CWvsContext.enableActions());
            return false;
        }
        if (((ii.isDropRestricted(item.getItemId())) || (ii.isAccountShared(item.getItemId())))
                && (!ItemFlag.KARMA_EQ.check(flag)) && (!ItemFlag.KARMA_USE.check(flag))) {
            c.getSession().write(CWvsContext.enableActions());
            return false;
        }

        Item tradeItem = item.copy();
        if ((GameConstants.isThrowingStar(item.getItemId())) || (GameConstants.isBullet(item.getItemId()))) {
            tradeItem.setQuantity(item.getQuantity());
            MapleInventoryManipulator.removeFromSlot(c, GameConstants.getInventoryType(item.getItemId()), item.getPosition(), item.getQuantity(), true);
        } else {
            tradeItem.setQuantity((short) quantity);
            MapleInventoryManipulator.removeFromSlot(c, GameConstants.getInventoryType(item.getItemId()), item.getPosition(), (short) quantity, true);
        }
        if (targetSlot < 0) {
            targetSlot = (byte) target;
        } else {
            for (Item itemz : this.items) {
                if (itemz.getPosition() == targetSlot) {
                    targetSlot = (byte) target;
                    break;
                }
            }
        }
        tradeItem.setPosition(targetSlot);
        addItem(tradeItem);
        return true;
    }

    private int check() {
        if (this.chr.get().getMeso() + this.exchangeMeso < 0L) {
            return 1;
        }

        if (this.exchangeItems != null) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            byte eq = 0;
            byte use = 0;
            byte setup = 0;
            byte etc = 0;
            byte cash = 0;
            for (Item item : this.exchangeItems) {
                switch (GameConstants.getInventoryType(item.getItemId())) {
                    case EQUIP:
                        eq = (byte) (eq + 1);
                        break;
                    case USE:
                        use = (byte) (use + 1);
                        break;
                    case SETUP:
                        setup = (byte) (setup + 1);
                        break;
                    case ETC:
                        etc = (byte) (etc + 1);
                        break;
                    case CASH:
                        cash = (byte) (cash + 1);
                }

                if ((ii.isPickupRestricted(item.getItemId())) && (this.chr.get().haveItem(item.getItemId(), 1, true, true))) {
                    return 2;
                }
            }
            if ((this.chr.get().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < eq) || (this.chr.get().getInventory(MapleInventoryType.USE).getNumFreeSlot() < use) || (this.chr.get().getInventory(MapleInventoryType.SETUP).getNumFreeSlot() < setup) || (this.chr.get().getInventory(MapleInventoryType.ETC).getNumFreeSlot() < etc) || (this.chr.get().getInventory(MapleInventoryType.CASH).getNumFreeSlot() < cash)) {
                return 1;
            }
        }

        return 0;
    }

    public static final void completeTrade(MapleCharacter c) {
        MapleTrade local = c.getTrade();
        MapleTrade partner = local.getPartner();

        if ((partner == null) || (local.locked)) {
            return;
        }
        local.locked = true;
        partner.getChr().getClient().getSession().write(CField.InteractionPacket.getTradeConfirmation());

        partner.exchangeItems = new LinkedList(local.items);
        partner.exchangeMeso = local.meso;
        if (local.getChr().getClient().getAccID() == partner.getChr().getClient().getAccID()) {
            local.getChr().ban("修改数据包 - 同帐号角色交易", true, true, false);
            partner.getChr().ban("修改数据包 - 同帐号角色交易", true, true, false);
            World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "[封号系统]" + local.chr.get().getName() + " 因为使用非法软件而被永久封号。"));
            local.getChr().getClient().getSession().close();
            partner.getChr().getClient().getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }
        if (partner.isLocked()) {
            int lz = local.check();
            int lz2 = partner.check();
            if ((lz == 0) && (lz2 == 0)) {
                local.CompleteTrade();
                partner.CompleteTrade();
                System.out.println("[交易] " + local.getChr().getName() + " 和 " + partner.getChr().getName() + " 交易完成。");
            } else {
                partner.cancel(partner.getChr().getClient(), partner.getChr(), lz == 0 ? lz2 : lz);
                local.cancel(c.getClient(), c, lz == 0 ? lz2 : lz);
            }
            partner.getChr().setTrade(null);
            c.setTrade(null);
        }
    }

    public static final void cancelTrade(MapleTrade Localtrade, MapleClient c, MapleCharacter chr) {
        Localtrade.cancel(c, chr);

        MapleTrade partner = Localtrade.getPartner();
        if ((partner != null) && (partner.getChr() != null)) {
            partner.cancel(partner.getChr().getClient(), partner.getChr());
            partner.getChr().setTrade(null);
        }
        chr.setTrade(null);
    }

    public static final void startTrade(MapleCharacter c) {
        if (c.getTrade() == null) {
            c.setTrade(new MapleTrade((byte) 0, c));
            c.getClient().getSession().write(CField.InteractionPacket.getTradeStart(c.getClient(), c.getTrade(), (byte) 0, false));
        } else {
            c.getClient().getSession().write(CWvsContext.broadcastMsg(5, "You are already in a trade"));
        }
    }
    
    public static final void startCS(final MapleCharacter c) {
        if (c.getTrade() == null) {
            c.setTrade(new MapleTrade((byte) 0, c));
            c.getClient().getSession().write(CField.InteractionPacket.getTradeStart(c.getClient(), c.getTrade(), (byte) 0, true));
        } else {
            c.getClient().getSession().write(CWvsContext.serverNotice(5, "不能同时做多件事情。"));
        }
    }

    public static final void inviteTrade(MapleCharacter c1, MapleCharacter c2) {
        if ((c1 == null) || (c1.getTrade() == null)) {
            return;
        }
        if ((c2 != null) && (c2.getTrade() == null)) {
            c2.setTrade(new MapleTrade((byte) 1, c2));
            c2.getTrade().setPartner(c1.getTrade());
            c1.getTrade().setPartner(c2.getTrade());
            c2.getClient().getSession().write(CField.InteractionPacket.getTradeInvite(c1, false));
        } else {
            c1.getClient().getSession().write(CWvsContext.broadcastMsg(5, "The other player is already trading with someone else."));
            cancelTrade(c1.getTrade(), c1.getClient(), c1);
        }
    }
    
    public static final void inviteCS(final MapleCharacter c1, final MapleCharacter c2) {
        if (c1 == null || c1.getTrade() == null) {
            return;
        }
        if (c2 != null && c2.getTrade() == null) {
            c2.setTrade(new MapleTrade((byte) 1, c2));
            c2.getTrade().setPartner(c1.getTrade());
            c1.getTrade().setPartner(c2.getTrade());
            c2.getClient().getSession().write(CField.InteractionPacket.getTradeInvite(c1, true));
        } else {
            c1.getClient().getSession().write(CWvsContext.serverNotice(5, "对方正在和其他玩家进行交易中。"));
            cancelTrade(c1.getTrade(), c1.getClient(), c1);
        }
    }
    
    public static final void visitCS(final MapleCharacter c1, final MapleCharacter c2) {
        if (c1.getTrade() != null && c1.getTrade().getPartner() == c2.getTrade() && c2.getTrade() != null && c2.getTrade().getPartner() == c1.getTrade()) {
            c2.getClient().getSession().write(PlayerShopPacket.shopVisitorAdd(c1, 1));
            c1.getClient().getSession().write(CField.InteractionPacket.getTradeStart(c1.getClient(), c1.getTrade(), (byte) 1, true));
        } else {
            c1.getClient().getSession().write(CWvsContext.broadcastMsg(5, "对方已经取消了交易。"));
        }
    }

    public static final void visitTrade(MapleCharacter c1, MapleCharacter c2) {
        if ((c2 != null) && (c1.getTrade() != null) && (c1.getTrade().getPartner() == c2.getTrade()) && (c2.getTrade() != null) && (c2.getTrade().getPartner() == c1.getTrade())) {
            c1.getTrade().inTrade = true;
            c2.getClient().getSession().write(PlayerShopPacket.shopVisitorAdd(c1, 1));
            c1.getClient().getSession().write(CField.InteractionPacket.getTradeStart(c1.getClient(), c1.getTrade(), (byte) 1, false));
            c1.dropMessage(-2, "系统提示 : 进行金币交换请注意手续费");
            c2.dropMessage(-2, "系统提示 : 进行金币交换请注意手续费");
            c1.getClient().getSession().write(CWvsContext.enableActions());
            c2.getClient().getSession().write(CWvsContext.enableActions());
        } else {
            c1.getClient().getSession().write(CWvsContext.broadcastMsg(5, "对方已经取消了交易。"));
        }
    }

    public static final void declineTrade(MapleCharacter c) {
        MapleTrade trade = c.getTrade();
        if (trade != null) {
            if (trade.getPartner() != null) {
                MapleCharacter other = trade.getPartner().getChr();
                if ((other != null) && (other.getTrade() != null)) {
                    other.getTrade().cancel(other.getClient(), other);
                    other.setTrade(null);
                    other.dropMessage(5, c.getName() + " 拒绝了你的交易要求");
                }
            }
            trade.cancel(c.getClient(), c);
            c.setTrade(null);
        }
    }
}
