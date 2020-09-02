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

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DBConPool;
import handling.channel.ChannelServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePackageActions;
import tools.FileoutputUtil;
import tools.Pair;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class PackageHandler {

    /*
     * 19 = Successful
     * 18 = One-of-a-kind Item is already in Reciever's delivery
     * 17 = The Character is unable to recieve the parcel
     * 15 = Same account
     * 14 = Name does not exist
     */
    public static void handleAction(final LittleEndianAccessor slea, final MapleClient c) {
        final byte operation = slea.readByte();

        switch (operation) {
            case 1: { // Start Donald, 13 digit AS
                final String AS13Digit = slea.readMapleAsciiString();
                //int unk = slea.readInt(); // Theres an int here, value = 1
                //9 = error
                final int conv = c.getPlayer().getConversation();
                if (conv == 2) { // Donald
                    c.getSession().write(CField.sendPackageMSG((byte) 10, loadItems(c.getPlayer())));
                }
                break;
            }
            case 3: { // Send Item
                if (c.getPlayer().getConversation() != 2) {
                    return;
                }
                final byte inventId = slea.readByte();
                final short itemPos = slea.readShort();
                final short amount = slea.readShort();
                final int mesos = slea.readInt();
                final String recipient = slea.readMapleAsciiString();
                boolean quickdelivery = slea.readByte() > 0;

                final int finalcost = mesos + GameConstants.getTaxAmount(mesos) + (quickdelivery ? 0 : 5000);

                if (mesos >= 0 && mesos <= 100000000 && c.getPlayer().getMeso() >= finalcost) {
                    final int accid = MapleCharacterUtil.getIdByName(recipient);
                    if (accid != -1) {
                        if (accid != c.getAccID()) {
                            boolean recipientOn = false;
                            MapleClient rClient = null;
                            try {
                                //int channel = c.getChannelServer().getWorldInterface().find(recipient);
                                int channel = c.getChannelServer().getPlayerStorage().getCharacterByName(recipient).getClient().getChannel();
                                if (channel > -1) {
                                    recipientOn = true;
                                    ChannelServer rcserv = ChannelServer.getInstance(channel);
                                    rClient = rcserv.getPlayerStorage().getCharacterByName(recipient).getClient();
                                }
                            } catch (Exception e) {
                                //c.getChannelServer().reconnectWorld();
                            }

                            if (inventId > 0) {
                                final MapleInventoryType inv = MapleInventoryType.getByType(inventId);
                                final Item item = c.getPlayer().getInventory(inv).getItem((byte) itemPos);
                                if (item == null) {
                                    c.getSession().write(CField.sendPackageMSG((byte) 17, null)); // Unsuccessfull
                                    return;
                                }
                                final short flag = item.getFlag();
                                if (ItemFlag.UNTRADABLE.check(flag) || ItemFlag.LOCK.check(flag)) {
                                    c.getSession().write(CWvsContext.enableActions());
                                    return;
                                }
                                if (c.getPlayer().getItemQuantity(item.getItemId(), false) >= amount) {
                                    final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                                    if (!ii.isDropRestricted(item.getItemId()) && !ii.isAccountShared(item.getItemId())) {
                                        if (addItemToDB(item, amount, mesos, c.getPlayer().getName(), accid, recipientOn)) {
                                            if (GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId())) {
                                                MapleInventoryManipulator.removeFromSlot(c, inv, (byte) itemPos, item.getQuantity(), true);
                                            } else {
                                                MapleInventoryManipulator.removeFromSlot(c, inv, (byte) itemPos, amount, true, false);
                                            }
                                            c.getPlayer().gainMeso(-finalcost, false);
                                            c.getSession().write(CField.sendPackageMSG((byte) 19, null)); // Successfull
                                        } else {
                                            c.getSession().write(CField.sendPackageMSG((byte) 17, null)); // Unsuccessful
                                        }
                                    } else {
                                        c.getSession().write(CField.sendPackageMSG((byte) 17, null)); // Unsuccessfull
                                    }
                                } else {
                                    c.getSession().write(CField.sendPackageMSG((byte) 17, null)); // Unsuccessfull
                                }
                            } else {
                                if (addMesoToDB(mesos, c.getPlayer().getName(), accid, recipientOn)) {
                                    c.getPlayer().gainMeso(-finalcost, false);

                                    c.getSession().write(CField.sendPackageMSG((byte) 19, null)); // Successfull
                                } else {
                                    c.getSession().write(CField.sendPackageMSG((byte) 17, null)); // Unsuccessfull
                                }
                            }
                            //if (recipientOn && rClient != null) {
                            //    rClient.getSession().write(CField.sendPackageMSG(Actions.PACKAGE_MSG.getCode()));
                            //}
                        } else {
                            c.getSession().write(CField.sendPackageMSG((byte) 15, null)); // Same acc error
                        }
                    } else {
                        c.getSession().write(CField.sendPackageMSG((byte) 14, null)); // Name does not exist
                    }
                } else {
                    c.getSession().write(CField.sendPackageMSG((byte) 12, null)); // Not enough mesos
                }
                break;
            }
            case 5: { // Recieve Package
                if (c.getPlayer().getConversation() != 2) {
                    return;
                }
                final int packageid = slea.readInt();
                //System.out.println("Item attempted : " + packageid);
                final MaplePackageActions dp = loadSingleItem(packageid, c.getPlayer().getId());
                if (dp == null) {
                    return;
                }
                if (dp.getItem() != null && !MapleInventoryManipulator.checkSpace(c, dp.getItem().getItemId(), dp.getItem().getQuantity(), dp.getItem().getOwner())) {
                    c.getSession().write(CField.sendPackageMSG((byte) 16, null)); // Not enough Space
                    return;
                } else if (dp.getMesos() < 0 || (dp.getMesos() + c.getPlayer().getMeso()) < 0) {
                    c.getSession().write(CField.sendPackageMSG((byte) 17, null)); // Unsuccessfull
                    return;
                }
                removeItemFromDB(packageid, c.getPlayer().getId()); // Remove first
                //System.out.println("Item removed : " + packageid);
                if (dp.getItem() != null) {
                    MapleInventoryManipulator.addFromDrop(c, dp.getItem(), false);
                }
                if (dp.getMesos() != 0) {
                    c.getPlayer().gainMeso(dp.getMesos(), false);
                }
                c.getSession().write(CField.removeFromPackageList(false, packageid));
                break;
            }
            case 6: { // Remove package
                if (c.getPlayer().getConversation() != 2) {
                    return;
                }
                final int packageid = slea.readInt();
                removeItemFromDB(packageid, c.getPlayer().getId());
                c.getSession().write(CField.removeFromPackageList(true, packageid));
                break;
            }
            case 8: { // Close Package Deliverer
                c.getPlayer().setConversation(0);
                break;
            }
            default: {
                System.out.println("Unhandled Package operation : " + slea.toString());
                break;
            }
        }
    }

    private static boolean addMesoToDB(final int mesos, final String sName, final int recipientID, final boolean isOn) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO dueypackages (RecieverId, SenderName, Mesos, TimeStamp, Checked, Type) VALUES (?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, recipientID);
                ps.setString(2, sName);
                ps.setInt(3, mesos);
                ps.setLong(4, System.currentTimeMillis());
                ps.setInt(5, isOn ? 0 : 1);
                ps.setInt(6, 3);
                ps.executeUpdate();
            }

            return true;
        } catch (SQLException se) {
            System.err.println("addMesoToDB" + se);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", se);
            return false;
        }
    }

    private static boolean addItemToDB(final Item item, final int quantity, final int mesos, final String sName, final int recipientID, final boolean isOn) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO dueypackages (RecieverId, SenderName, Mesos, TimeStamp, Checked, Type) VALUES (?, ?, ?, ?, ?, ?)", DBConPool.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, recipientID);
                ps.setString(2, sName);
                ps.setInt(3, mesos);
                ps.setLong(4, System.currentTimeMillis());
                ps.setInt(5, isOn ? 0 : 1);

                ps.setInt(6, item.getType());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        ItemLoader.PACKAGE.saveItems(Collections.singletonList(new Pair<>(item, GameConstants.getInventoryType(item.getItemId()))), con, recipientID);
                    }
                }
            }

            return true;
        } catch (SQLException se) {
            System.err.println("addItemToDB" + se);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", se);
            return false;
        }
    }

    public static List<MaplePackageActions> loadItems(final MapleCharacter chr) {
        List<MaplePackageActions> packages = new LinkedList<>();
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dueypackages WHERE RecieverId = ?")) {
                ps.setInt(1, chr.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        MaplePackageActions pack = getItemByPID(rs.getInt("packageid"));
                        pack.setSender(rs.getString("SenderName"));
                        pack.setMesos(rs.getInt("Mesos"));
                        pack.setSentTime(rs.getLong("TimeStamp"));
                        packages.add(pack);
                    }
                }
            }
            return packages;
        } catch (SQLException se) {
            System.err.println("loadItems" + se);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", se);
            return null;
        }
    }

    public static MaplePackageActions loadSingleItem(final int packageid, final int charid) {
        List<MaplePackageActions> packages = new LinkedList<>();
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM dueypackages WHERE PackageId = ? and RecieverId = ?");
            ps.setInt(1, packageid);
            ps.setInt(2, charid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                MaplePackageActions pack = getItemByPID(packageid);
                pack.setSender(rs.getString("SenderName"));
                pack.setMesos(rs.getInt("Mesos"));
                pack.setSentTime(rs.getLong("TimeStamp"));
                packages.add(pack);
                rs.close();
                ps.close();
                return pack;
            } else {
                rs.close();
                ps.close();
                return null;
            }
        } catch (SQLException se) {
            System.err.println("loadSingleItem" + se);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", se);
            return null;
        }
    }

    public static void reciveMsg(final MapleClient c, final int recipientId) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("UPDATE dueypackages SET Checked = 0 where RecieverId = ?")) {
                ps.setInt(1, recipientId);
                ps.executeUpdate();
            }
        } catch (SQLException se) {
            System.err.println("reciveMsg" + se);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", se);
        }
    }

    private static void removeItemFromDB(final int packageid, final int charid) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM dueypackages WHERE PackageId = ? and RecieverId = ?")) {
                ps.setInt(1, packageid);
                ps.setInt(2, charid);
                ps.executeUpdate();
            }
        } catch (SQLException se) {
            System.err.println("removeItemFromDB" + se);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", se);
        }
    }

    private static MaplePackageActions getItemByPID(final int packageid) {
        try {
            Map<Long, Pair<Item, MapleInventoryType>> iter = ItemLoader.PACKAGE.loadItems(false, packageid);
            if (iter != null && iter.size() > 0) {
                for (Pair<Item, MapleInventoryType> i : iter.values()) {
                    return new MaplePackageActions(packageid, i.getLeft());
                }
            }
        } catch (Exception se) {
        }
        return new MaplePackageActions(packageid);
    }
}
