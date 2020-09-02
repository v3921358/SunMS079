package client;

import database.DBConPool;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import tools.FileoutputUtil;
import tools.packet.CWvsContext.BuddylistPacket;

public class BuddyList implements Serializable {

    public static enum BuddyOperation {

        ADDED, DELETED
    }

    public static enum BuddyAddResult {

        BUDDYLIST_FULL, ALREADY_ON_LIST, OK
    }
    private static final long serialVersionUID = 1413738569L;
    private final Map<Integer, BuddylistEntry> buddies;
    private byte capacity;
    private boolean changed = false;

    public BuddyList(byte capacity) {
        this.buddies = new LinkedHashMap<>();
        this.capacity = capacity;
    }

    public boolean contains(int characterId) {
        return buddies.containsKey(characterId);
    }

    public boolean containsVisible(int characterId) {
        BuddylistEntry ble = buddies.get(characterId);
        if (ble == null) {
            return false;
        }
        return ble.isVisible();
    }

    public byte getCapacity() {
        return capacity;
    }

    public void setCapacity(byte capacity) {
        this.capacity = capacity;
    }

    public BuddylistEntry get(int characterId) {
        return buddies.get(characterId);
    }

    public BuddylistEntry get(String characterName) {
        String lowerCaseName = characterName.toLowerCase();
        for (BuddylistEntry ble : buddies.values()) {
            if (ble.getName().toLowerCase().equals(lowerCaseName)) {
                return ble;
            }
        }
        return null;
    }

    public void put(BuddylistEntry entry) {
        buddies.put(entry.getCharacterId(), entry);
        changed = true;
    }

    public void remove(int characterId) {
        buddies.remove(characterId);
        changed = true;
    }

    public Collection<BuddylistEntry> getBuddies() {
        return buddies.values();
    }

    public boolean isFull() {
        return buddies.size() >= capacity;
    }

    public int[] getBuddyIds() {
        int buddyIds[] = new int[buddies.size()];
        int i = 0;
        for (BuddylistEntry ble : buddies.values()) {
            if (ble.isVisible()) {
                buddyIds[i++] = ble.getCharacterId();
            }
        }
        return buddyIds;
    }

    public void loadFromTransfer(final Map<CharacterNameAndId, Boolean> data) {
        CharacterNameAndId buddyid;
        for (final Map.Entry<CharacterNameAndId, Boolean> qs : data.entrySet()) {
            buddyid = qs.getKey();
            put(new BuddylistEntry(buddyid.getName(), buddyid.getId(), buddyid.getGroup(), -1, qs.getValue()));
        }
    }

    public void loadFromDb(int characterId) throws SQLException {

        try (Connection con = DBConPool.getInstance().getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement("SELECT b.buddyid, b.pending, c.name as buddyname, b.groupname FROM buddies as b, characters as c WHERE c.id = b.buddyid AND b.characterid = ?")) {
            ps.setInt(1, characterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    put(new BuddylistEntry(rs.getString("buddyname"), rs.getInt("buddyid"), rs.getString("groupname"), -1, rs.getInt("pending") != 1));
                }
            }

        } catch (SQLException e) {
            System.err.println("buddiese" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public void addBuddyRequest(MapleClient c, int cidFrom, String nameFrom, int channelFrom, int levelFrom, int jobFrom) {
        put(new BuddylistEntry(nameFrom, cidFrom, "群未定", channelFrom, false));
        c.getSession().write(BuddylistPacket.requestBuddylistAdd(cidFrom, nameFrom, levelFrom, jobFrom));
    }

    public void setChanged(boolean v) {
        this.changed = v;
    }

    public boolean changed() {
        return changed;
    }
}
