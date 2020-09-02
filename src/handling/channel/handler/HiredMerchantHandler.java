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
package handling.channel.handler;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.MapleClient;
import client.MapleCharacter;
import constants.GameConstants;
import client.inventory.ItemLoader;
import database.DBConPool;
import handling.world.World;
import java.util.Map;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MerchItemPackage;
import tools.FileoutputUtil;
import tools.Pair;
import tools.StringUtil;
import tools.packet.PlayerShopPacket;
import tools.data.LittleEndianAccessor;
import tools.packet.CField.NPCPacket;
import tools.packet.CWvsContext;

public class HiredMerchantHandler {

    public static final boolean UseHiredMerchant(final MapleClient c, final boolean packet) {
        if (c.getPlayer().getMap() != null && c.getPlayer().getMap().allowPersonalShop()) {
            final byte state = checkExistance(c.getPlayer().getAccountID(), c.getPlayer().getId());

            switch (state) {
                case 1:
                    c.getPlayer().dropMessage(1, "请先去找弗兰德里领取你的东西.");
                    break;
                case 0:
                    boolean merch = World.hasMerchant(c.getPlayer().getAccountID(), c.getPlayer().getId());
                    if (!merch) {
                        if (c.getChannelServer().isShutdown()) {
                            c.getPlayer().dropMessage(1, "服务器即将关闭，不能执行此操作。.");
                            return false;
                        }
                        if (packet) {
                            c.getSession().write(PlayerShopPacket.sendTitleBox());
                        }
                        return true;
                    } else {
                        c.getSession().write(PlayerShopPacket.ShowMerchItemStore(9030000, World.getMerchantMap(c.getPlayer()), World.getMerchantChannel(c.getPlayer())));

                        //  c.getPlayer().dropMessage(1, "请关闭雇用后再试一次");
                    }
                    break;
                default:
                    c.getPlayer().dropMessage(1, "发生未知的错误.");
                    break;
            }
        } else {
            c.getPlayer().ban("修改数据包 - 在非自由市场洞口内开店", true, true, false);
            World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "[封号系统]" + c.getPlayer().getName() + " 因为使用非法软件而被永久封号。"));
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
        }
        return false;
    }

    private static byte checkExistance(final int accid, final int cid) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * from hiredmerch where accountid = ? OR characterid = ?")) {
                ps.setInt(1, accid);
                ps.setInt(2, cid);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ps.close();
                        rs.close();
                        return 1;
                    }
                }
            }
            return 0;
        } catch (SQLException se) {
            System.err.println("checkExistance" + se);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", se);
            return -1;
        }
    }

    public static void displayMerch(MapleClient c) {
        final int conv = c.getPlayer().getConversation();
        boolean merch = World.hasMerchant(c.getPlayer().getAccountID(), c.getPlayer().getId());
        if (merch) {
            c.getPlayer().dropMessage(1, "请关闭您的经雇佣商人后再来找我.");
            c.getPlayer().setConversation(0);
        } else if (c.getChannelServer().isShutdown()) {
            c.getPlayer().dropMessage(1, "服务器即将关闭，不能执行此操作。.");
            c.getPlayer().setConversation(0);
        } else if (conv == 3) { // Hired Merch
            final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getAccountID());

            if (pack == null) {
                c.getSession().write(PlayerShopPacket.merchItemStore((byte) 0x25, 0, 0));
                c.getPlayer().setConversation(0);
            } else if (pack.getItems().size() <= 0) { //error fix for complainers.
                if (!check(c.getPlayer(), pack)) {
                    c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x21));
                    return;
                }
                if (deletePackage(c.getPlayer().getAccountID(), pack.getPackageid(), c.getPlayer().getId())) {
                    //c.getPlayer().fakeRelog();
                    c.getPlayer().gainMeso(pack.getMesos(), true);
                    c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x1d));
                    c.getPlayer().setConversation(0);
                } else {
                    c.getPlayer().dropMessage(1, "发生未知的错误.");
                }
                c.getPlayer().setConversation(0);
            } else {
                if (!check(c.getPlayer(), pack)) {
                    c.getPlayer().dropMessage(1, "请检查身上的钱是否过多以及背包空间是否足够");
                    //  c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x21));
                    return;
                }
                //  c.getSession().write(PlayerShopPacket.merchItemStore_ItemData(pack));
                c.getPlayer().gainMeso(pack.getMesos(), true);
                for (final Item item : pack.getItems()) {
                    MapleInventoryManipulator.addFromDrop(c, item, true);
                }
                deletePackage(c.getPlayer().getAccountID(), pack.getPackageid(), c.getPlayer().getId());
                c.getSession().write(NPCPacket.getNPCTalk(9030000, (byte) 0, "您雇佣商店保存的道具已发送到背包上.", "00 00", (byte) 0));
                c.getPlayer().setConversation(0);
            }
        }
        c.getSession().write(CWvsContext.enableActions());
    }

    public static final void MerchantItemStore(final LittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer() == null) {
            return;
        }

        final byte operation = slea.readByte();
        if (operation == 20 || operation == 26) {
//            if (c.getPlayer().getLastHM() + 24 * 60 * 60 * 1000 > System.currentTimeMillis()) {
//                c.getPlayer().dropMessage(1, "24小时内无法进行操作，\r\n请24小时之后再进行操作。\r\n");
//                c.getSession().write(MaplePacketCreator.enableActions());
//                c.getPlayer().setConversation(0);
//                return;
//            }
        }
        switch (operation) {
            case 20: {
                slea.readMapleAsciiString();

                final int conv = c.getPlayer().getConversation();
                boolean merch = World.hasMerchant(c.getPlayer().getAccountID(), c.getPlayer().getId());
                if (merch) {
                    c.getSession().write(PlayerShopPacket.ShowMerchItemStore(9030000, World.getMerchantMap(c.getPlayer()), World.getMerchantChannel(c.getPlayer())));
                    c.getPlayer().setConversation(0);
                } else if (conv == 3) { // Hired Merch 雇来的东西
                    final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getAccountID());

                    if (pack == null) {
                        c.getSession().write(PlayerShopPacket.merchItemStore((byte) 0x25, 0, 0));
                        c.getPlayer().setConversation(0);
                    } else if (pack.getItems().size() <= 0) { //error fix for complainers.对于抱怨错误修复。
                        if (!check(c.getPlayer(), pack)) {
                            c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x21));
                            return;
                        }
                        if (deletePackage(c.getPlayer().getId(), c.getPlayer().getAccountID(), pack.getPackageid())) {
                            FileoutputUtil.logToFile_chr(c.getPlayer(), "Log_雇佣金币领取记录.txt", " 领回金币 " + pack.getMesos());
                            c.getPlayer().gainMeso(pack.getMesos(), false);
                            c.getPlayer().setConversation(0);
                            c.getPlayer().dropMessage("领取金币" + pack.getMesos());
                            //     c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x1d));
                            //      c.getPlayer().setLastHM(System.currentTimeMillis());
                        } else {
                            c.getPlayer().dropMessage(1, "发生未知错误。");
                        }
                        c.getPlayer().setConversation(0);
                        c.getSession().write(CWvsContext.enableActions());
                    } else {
                        c.getSession().write(PlayerShopPacket.merchItemStore_ItemData(pack));
                    }
                }
                break;
            }
            case 25: { // 要求拿出物品
                if (c.getPlayer().getConversation() != 3) {
                    return;
                }
                c.getSession().write(PlayerShopPacket.merchItemStore((byte) 0x24, 0, 0));// 38
                break;
            }
            case 26: { // 取出物品
                if (c.getPlayer().getConversation() != 3) {
                    c.getPlayer().dropMessage(1, "发生未知错误1.");
                    c.getSession().write(CWvsContext.enableActions());
                    return;
                }
                final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getAccountID());

                if (pack == null) {
                    c.getSession().write(PlayerShopPacket.merchItemStore((byte) 0x25, 0, 0));
                    return;
                }
                if (!check(c.getPlayer(), pack)) {
                    c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x21));
                    return;
                }
                if (deletePackage(c.getPlayer().getAccountID(), pack.getPackageid(), c.getPlayer().getId())) {
                    c.getPlayer().gainMeso(pack.getMesos(), false);
                    for (Item item : pack.getItems()) {
                        MapleInventoryManipulator.addFromDrop(c, item, false);
                    }
                    c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x1d));
                    String item_id = "";
                    String item_name = "";
                    for (Item item : pack.getItems()) {
                        item_id += item.getItemId() + "(" + item.getQuantity() + "), ";
                        item_name += MapleItemInformationProvider.getInstance().getName(item.getItemId()) + "(" + item.getQuantity() + "), ";
                    }
                    c.getPlayer().setConversation(0);
                    FileoutputUtil.logToFile_chr(c.getPlayer(), "Data/Log_雇佣领取记录.txt", " 领回金币 " + pack.getMesos() + " 领回道具数量 " + pack.getItems().size() + " 道具 " + item_id);
                    FileoutputUtil.logToFile_chr(c.getPlayer(), "Data/Log_雇佣领取记录2.txt", " 领回金币 " + pack.getMesos() + " 领回道具数量 " + pack.getItems().size() + " 道具 " + item_name);
                    //    c.getPlayer().setLastHM(System.currentTimeMillis());
                } else {
                    c.getPlayer().dropMessage(1, "发生未知错误.");
                }
                break;
            }
            case 27: { // Exit
                c.getPlayer().setConversation(0);
                break;
            }
        }
//        if (c.getPlayer() == null) {
//            return;
//        }
//        final byte operation = slea.readByte();
//        if (operation == 27 || operation == 28) { // Request, Take out
//            requestItems(c, operation == 27);
//        } else if (operation == 30) { // Exit
//            c.getPlayer().setConversation(0);
//        }
    }

    private static void requestItems(final MapleClient c, final boolean request) {
        if (c.getPlayer().getConversation() != 3) {
            return;
        }
        boolean merch = World.hasMerchant(c.getPlayer().getAccountID(), c.getPlayer().getId());
        if (merch) {
            c.getPlayer().dropMessage(1, "请关闭雇用后再试一次");
            c.getPlayer().setConversation(0);
            return;
        }
        final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getAccountID());
        if (pack == null) {
            c.getPlayer().dropMessage(1, "发生未知的错误.");
            return;
        } else if (c.getChannelServer().isShutdown()) {
            c.getPlayer().dropMessage(1, "服务器即将关闭，不能执行此操作。.");
            c.getPlayer().setConversation(0);
            return;
        }
        final int days = StringUtil.getDaysAmount(pack.getSavedTime(), System.currentTimeMillis()); // max 100%
        final double percentage = days / 100.0;
        final int fee = (int) Math.ceil(percentage * pack.getMesos()); // if no mesos = no tax
        if (request && days > 0 && percentage > 0 && pack.getMesos() > 0 && fee > 0) {
            c.getSession().write(PlayerShopPacket.merchItemStore((byte) 38, days, fee));
            return;
        }
        if (fee < 0) { // impossible
            c.getSession().write(PlayerShopPacket.merchItem_Message(33));
            return;
        }
        if (c.getPlayer().getMeso() < fee) {
            c.getSession().write(PlayerShopPacket.merchItem_Message(35));
            return;
        }
        if (!check(c.getPlayer(), pack)) {
            c.getSession().write(PlayerShopPacket.merchItem_Message(36));
            return;
        }
        if (deletePackage(c.getPlayer().getAccountID(), pack.getPackageid(), c.getPlayer().getId())) {
            if (fee > 0) {
                c.getPlayer().gainMeso(-fee, true);
            }
            c.getPlayer().gainMeso(pack.getMesos(), false);
            for (Item item : pack.getItems()) {
                MapleInventoryManipulator.addFromDrop(c, item, false);
            }
            c.getSession().write(PlayerShopPacket.merchItem_Message(32));
        } else {
            c.getPlayer().dropMessage(1, "发生未知的错误.");
        }
    }

    private static boolean check(final MapleCharacter chr, final MerchItemPackage pack) {
        if (chr.getMeso() + pack.getMesos() < 0) {
            return false;
        }
        byte eq = 0, use = 0, setup = 0, etc = 0, cash = 0;
        for (Item item : pack.getItems()) {
            final MapleInventoryType invtype = GameConstants.getInventoryType(item.getItemId());
            if (invtype == MapleInventoryType.EQUIP) {
                eq++;
            } else if (invtype == MapleInventoryType.USE) {
                use++;
            } else if (invtype == MapleInventoryType.SETUP) {
                setup++;
            } else if (invtype == MapleInventoryType.ETC) {
                etc++;
            } else if (invtype == MapleInventoryType.CASH) {
                cash++;
            }
        }

        boolean slot = true;
        if (chr.getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() <= eq && eq != 0) {
            slot = false;
        }
        if (chr.getInventory(MapleInventoryType.USE).getNumFreeSlot() <= use && use != 0) {
            slot = false;
        }
        if (chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() <= setup && setup != 0) {
            slot = false;
        }
        if (chr.getInventory(MapleInventoryType.ETC).getNumFreeSlot() <= etc && etc != 0) {
            slot = false;
        }
        if (chr.getInventory(MapleInventoryType.CASH).getNumFreeSlot() <= cash && cash != 0) {
            slot = false;

        }
        return slot;
    }

    private static boolean deletePackage(final int accid, final int packageid, final int chrId) {

        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("DELETE from hiredmerch where accountid = ? OR packageid = ? OR characterid = ?")) {
                ps.setInt(1, accid);
                ps.setInt(2, packageid);
                ps.setInt(3, chrId);
                ps.executeUpdate();
            }
            ItemLoader.HIRED_MERCHANT.saveItems(null, con, packageid);
            return true;
        } catch (SQLException e) {
            System.err.println("deletePackage" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
            return false;
        }
    }

    public static final void showFredrick(MapleClient c) {
        final MerchItemPackage pack = HiredMerchantHandler.loadItemFrom_Database(c.getPlayer().getAccountID());
        c.getSession().write(PlayerShopPacket.merchItemStore_ItemData(pack));
    }

    private static MerchItemPackage loadItemFrom_Database(final int accountid) {

        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            ResultSet rs;

            final int packageid;
            final MerchItemPackage pack;
            try (PreparedStatement ps = con.prepareStatement("SELECT * from hiredmerch where accountid = ?")) {
                ps.setInt(1, accountid);
                rs = ps.executeQuery();
                if (!rs.next()) {
                    ps.close();
                    rs.close();
                    return null;
                }
                packageid = rs.getInt("PackageId");
                pack = new MerchItemPackage();
                pack.setPackageid(packageid);
                pack.setMesos(rs.getInt("Mesos"));
                pack.setSavedTime(rs.getLong("time"));
            }
            rs.close();

            Map<Long, Pair<Item, MapleInventoryType>> items = ItemLoader.HIRED_MERCHANT.loadItems(false, packageid);
            if (items != null) {
                List<Item> iters = new ArrayList<>();
                for (Pair<Item, MapleInventoryType> z : items.values()) {
                    iters.add(z.left);
                }
                pack.setItems(iters);
            }

            return pack;
        } catch (SQLException e) {
            System.err.println("loadItemFrom_Database" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
            return null;
        }
    }
}
