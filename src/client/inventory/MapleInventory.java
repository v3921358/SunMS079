package client.inventory;

import client.MapleCharacter;
import constants.GameConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import server.MapleItemInformationProvider;
import tools.packet.CWvsContext.InventoryPacket;

public class MapleInventory implements Iterable<Item>, Serializable {

    private final Map<Short, Item> inventory;
    private byte slotLimit = 0;
    private final MapleInventoryType type;

    public MapleInventory(MapleInventoryType type) {
        this.inventory = new LinkedHashMap();
        this.type = type;
    }

    public void addSlot(byte slot) {
        this.slotLimit = ((byte) (this.slotLimit + slot));

        if (this.slotLimit > 96) {
            this.slotLimit = 96;
        }
    }

    public byte getSlotLimit() {
        return this.slotLimit;
    }

    public void setSlotLimit(byte slot) {
        if (slot > 96) {
            slot = 96;
        }
        this.slotLimit = slot;
    }

    public Item findById(int itemId, int position) {
        for (Item item : this.inventory.values()) {
            if (item.getItemId() == itemId && item.getPosition() == position) {
                return item;
            }
        }
        return null;
    }

    public Item findById(int itemId) {
        for (Item item : this.inventory.values()) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }
        return null;
    }

    public Item findByUniqueId(int itemId) {
        for (Item item : this.inventory.values()) {
            if (item.getUniqueId() == itemId) {
                return item;
            }
        }
        return null;
    }

    public Item findByInventoryId(long itemId, int itemI) {
        for (Item item : this.inventory.values()) {
            if ((item.getInventoryId() == itemId) && (item.getItemId() == itemI)) {
                return item;
            }
        }
        return findById(itemI);
    }

    public Item findByInventoryIdOnly(long itemId, int itemI) {
        for (Item item : this.inventory.values()) {
            if ((item.getInventoryId() == itemId) && (item.getItemId() == itemI)) {
                return item;
            }
        }
        return null;
    }

    public int countById(int itemId) {
        int possesed = 0;
        for (Item item : this.inventory.values()) {
            if (item.getItemId() == itemId) {
                possesed += item.getQuantity();
            }
        }
        return possesed;
    }

    public List<Item> listById(int itemId) {
        List ret = new ArrayList();
        for (Item item : this.inventory.values()) {
            if (item.getItemId() == itemId) {
                ret.add(item);
            }

        }

        if (ret.size() > 1) {
            Collections.sort(ret);
        }
        return ret;
    }

    public List<Item> listByEquipOnlyId(long equipOnlyId) {
        List ret = new ArrayList();
        for (Item item : this.inventory.values()) {
            if ((item.getEquipOnlyId() > 0) && (item.getEquipOnlyId() == equipOnlyId)) {
                ret.add(item);
            }
        }
        if (ret.size() > 1) {
            Collections.sort(ret);
        }
        return ret;
    }

    public Collection<Item> list() {
        return this.inventory.values();
    }

    public List<Item> newList() {
        if (this.inventory.size() <= 0) {
            return Collections.emptyList();
        }
        return new LinkedList(this.inventory.values());
    }

    public List<Integer> listIds() {
        List ret = new ArrayList();
        for (Item item : this.inventory.values()) {
            if (!ret.contains(item.getItemId())) {
                ret.add(item.getItemId());
            }
        }
        if (ret.size() > 1) {
            Collections.sort(ret);
        }
        return ret;
    }

    public short addItem(Item item, int slotId) {
        this.inventory.put((short) slotId, item);
        item.setPosition((short) slotId);
        return (short) slotId;
    }

    public short addItem(Item item) {
        short slotId = getNextFreeSlot();
        if (slotId < 0) {
            return -1;
        }
        this.inventory.put(slotId, item);
        item.setPosition(slotId);
        return slotId;
    }

    public short addItem_shied(Item item) {
        short slotId = 97;
        if (this.inventory.containsKey(97)) {
            return -1;
        }

        this.inventory.put(slotId, item);
        item.setPosition(slotId);
        return slotId;
    }

    public void addFromDB(Item item) {

        if ((item.getPosition() < 0) && (!this.type.equals(MapleInventoryType.EQUIPPED))) {
            return;
        }
        if ((item.getPosition() > 0) && (this.type.equals(MapleInventoryType.EQUIPPED))) {
            return;
        }
        this.inventory.put(item.getPosition(), item);
    }

    public void move(short sSlot, short dSlot, short slotMax) {
        Item source = (Item) this.inventory.get(Short.valueOf(sSlot));
        Item target = (Item) this.inventory.get(Short.valueOf(dSlot));
        if (source == null) {
            throw new InventoryException("Trying to move empty slot");
        }
        if (target == null) {
            if ((dSlot < 0) && (!this.type.equals(MapleInventoryType.EQUIPPED))) {
                return;
            }
            if ((dSlot > 0) && (this.type.equals(MapleInventoryType.EQUIPPED))) {
                return;
            }
            source.setPosition(dSlot);
            this.inventory.put(dSlot, source);
            this.inventory.remove(sSlot);
        } else if ((target.getItemId() == source.getItemId()) && (!GameConstants.isThrowingStar(source.getItemId())) && (!GameConstants.isBullet(source.getItemId())) && (target.getOwner().equals(source.getOwner())) && (target.getExpiration() == source.getExpiration())) {
            if ((this.type.getType() == MapleInventoryType.EQUIP.getType()) || (this.type.getType() == MapleInventoryType.CASH.getType())) {
                swap(target, source);
            } else if (source.getQuantity() + target.getQuantity() > slotMax) {
                source.setQuantity((short) (source.getQuantity() + target.getQuantity() - slotMax));
                target.setQuantity(slotMax);
            } else {
                target.setQuantity((short) (source.getQuantity() + target.getQuantity()));
                this.inventory.remove(sSlot);
            }
        } else {
            swap(target, source);
        }
    }

    private void swap(Item source, Item target) {
        this.inventory.remove(source.getPosition());
        this.inventory.remove(target.getPosition());
        short swapPos = source.getPosition();
        source.setPosition(target.getPosition());
        target.setPosition(swapPos);
        this.inventory.put(source.getPosition(), source);
        this.inventory.put(target.getPosition(), target);
    }

    public Item getItem(short slot) {
        return (Item) this.inventory.get(slot);
    }

    public void removeItem(short slot) {
        removeItem(slot, (short) 1, false);
    }

    public void removeItem(short slot, short quantity, boolean allowZero) {
        Item item = (Item) this.inventory.get(Short.valueOf(slot));
        if (item == null) {
            return;
        }
        item.setQuantity((short) (item.getQuantity() - quantity));
        if (item.getQuantity() < 0) {
            item.setQuantity((short) 0);
        }
        if ((item.getQuantity() == 0) && (!allowZero)) {
            removeSlot(slot);
        }
    }

    public void removeItem(short slot, short quantity, boolean allowZero, MapleCharacter chr) {
        Item item = inventory.get(slot);
        if (item == null) { // TODO is it ok not to throw an exception here?
            return;
        }
        item.setQuantity((short) (item.getQuantity() - quantity));
        if (item.getQuantity() < 0) {
            item.setQuantity((short) 0);
        }
        if (item.getQuantity() == 0 && !allowZero) {
            removeSlot(slot);
        }
        if (chr != null) {
            chr.getClient().getSession().write(InventoryPacket.clearInventoryItem(GameConstants.getInventoryType(item.getItemId()), item.getPosition(), false));
            chr.getStat().recalcLocalStats(chr);
            chr.dropMessage(5, "加值道具[" + MapleItemInformationProvider.getInstance().getName(item.getItemId()) + "]因到期而消失了。");
        }
    }

    public void removeSlot(short slot) {
        this.inventory.remove(slot);
    }

    public boolean isFull() {
        return this.inventory.size() >= this.slotLimit;
    }

    public boolean isFull(int margin) {
        return this.inventory.size() + margin >= this.slotLimit;
    }

    public short getNextFreeSlot() {
        if (isFull()) {
            return -1;
        }
        for (short i = 1; i <= this.slotLimit; i = (short) (i + 1)) {
            if (!this.inventory.containsKey(i)) {
                return i;
            }
        }
        return -1;
    }

    public short getNumFreeSlot() {
        if (isFull()) {
            return 0;
        }
        byte free = 0;
        for (short i = 1; i <= this.slotLimit; i = (short) (i + 1)) {
            if (!this.inventory.containsKey(i)) {
                free = (byte) (free + 1);
            }
        }
        return (short) free;
    }

    public MapleInventoryType getType() {
        return this.type;
    }

    @Override
    public Iterator<Item> iterator() {
        return Collections.unmodifiableCollection(this.inventory.values()).iterator();
    }

    public Item getItemByInventoryItemId(Long inventoryitemid) {
        for (Item item : this.inventory.values()) {
            if (item.getInventoryId() == inventoryitemid) {
                return item;
            }
        }
        return null;
    }

}
