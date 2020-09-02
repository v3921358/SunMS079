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
package client.inventory;

import java.io.Serializable;
import server.MapleItemInformationProvider;

public class Item implements Comparable<Item>, Serializable {

    private final int id;
    private short position;
    private short quantity;
    private short flag;
    private long expiration = -1, inventoryitemid = 0;
    private MaplePet pet = null;
    private int uniqueid;
    private long equipOnlyId = -1;
    private String owner = "";
    private String GameMaster_log = "";
    private String giftFrom = "";
    private int exp;
    protected MapleRing ring = null;

    public Item(final int id, final short position, final short quantity, final short flag, final int uniqueid) {
        super();
        this.id = id;
        this.position = position;
        this.quantity = quantity;
        this.flag = flag;
        this.uniqueid = uniqueid;
        this.equipOnlyId = -1;
    }

    public Item(final int id, final short position, final short quantity, final short flag) {
        super();
        this.id = id;
        this.position = position;
        this.quantity = quantity;
        this.flag = flag;
        this.uniqueid = -1;
        this.equipOnlyId = -1;
    }

    public Item(int id, byte position, short quantity) {
        super();
        this.id = id;
        this.position = position;
        this.quantity = quantity;
        this.uniqueid = -1;
        this.equipOnlyId = -1;
    }

    public Item copy() {
        final Item ret = new Item(id, position, quantity, flag, uniqueid);
        ret.pet = pet;
        ret.owner = owner;
        ret.GameMaster_log = GameMaster_log;
        ret.expiration = expiration;
        ret.giftFrom = giftFrom;
        ret.equipOnlyId = equipOnlyId;
        //ret.exp = exp;
        return ret;
    }

    public Item copyWithQuantity(final short qq) {
        final Item ret = new Item(id, position, qq, flag, uniqueid);
        ret.pet = pet;
        ret.owner = owner;
        ret.GameMaster_log = GameMaster_log;
        ret.expiration = expiration;
        ret.giftFrom = giftFrom;
        ret.equipOnlyId = equipOnlyId;
        //ret.exp = exp;
        return ret;
    }

    public final void setPosition(final short position) {
        this.position = position;

        if (pet != null) {
            pet.setInventoryPosition(position);
        }
    }

    public void setQuantity(final short quantity) {
        this.quantity = quantity;
    }

    public final int getItemId() {
        return id;
    }

    public final short getPosition() {
        return position;
    }

    public final short getFlag() {
        return flag;
    }

    public final short getQuantity() {
        return quantity;
    }

    public byte getType() {
        return 2; // An Item
    }

    public final String getOwner() {
        return owner;
    }

    public final void setOwner(final String owner) {
        this.owner = owner;
    }

    public final void setFlag(final short flag) {
        this.flag = flag;
    }

    public final long getExpiration() {
        return expiration;
    }

    public final void setExpiration(final long expire) {
        this.expiration = expire;
    }

    public final String getGMLog() {
        return GameMaster_log;
    }

    public void setGMLog(final String GameMaster_log) {
        this.GameMaster_log = GameMaster_log;
    }

    public final int getUniqueId() {
        return uniqueid;
    }

    public void setUniqueId(int ui) {
        this.uniqueid = ui;
    }

    public boolean hasSetOnlyId() {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if ((this.uniqueid > 0) || (ii.isCash(this.id)) || (this.id / 1000000 != 1)) {
            return false;
        }
        return this.equipOnlyId <= 0;
    }

    public long getEquipOnlyId() {
        return this.equipOnlyId;
    }

    public void setEquipOnlyId(long OnlyId) {
        this.equipOnlyId = OnlyId;
    }

    public final long getInventoryId() { //this doesn't need to be 100% accurate, just different
        return inventoryitemid;
    }

    public void setInventoryId(long ui) {
        this.inventoryitemid = ui;
    }

    public final MaplePet getPet() {
        return pet;
    }

    public final void setPet(final MaplePet pet) {
        this.pet = pet;
        if (pet != null) {
            this.uniqueid = pet.getUniqueId();
        }
    }

    public void setGiftFrom(String gf) {
        this.giftFrom = gf;
    }

    public String getGiftFrom() {
        return giftFrom;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    @Override
    public int compareTo(Item other) {
        if (Math.abs(position) < Math.abs(other.getPosition())) {
            return -1;
        } else if (Math.abs(position) == Math.abs(other.getPosition())) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Item)) {
            return false;
        }
        final Item ite = (Item) obj;
        return uniqueid == ite.getUniqueId() && id == ite.getItemId() && quantity == ite.getQuantity() && Math.abs(position) == Math.abs(ite.getPosition());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.id;
        hash = 53 * hash + this.position;
        hash = 53 * hash + this.quantity;
        hash = 53 * hash + this.uniqueid;
        return hash;
    }

    @Override
    public String toString() {
        return "Item: " + id + " quantity: " + quantity;
    }
}
