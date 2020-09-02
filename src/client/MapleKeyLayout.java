package client;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import tools.Pair;
import tools.data.MaplePacketLittleEndianWriter;

public class MapleKeyLayout implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    private boolean changed = false;
    private MapleCharacter chr;
    private final Map<Integer, Pair<Byte, Integer>> keymap;

    public MapleKeyLayout() {
        keymap = new HashMap<>();
    }

    public MapleKeyLayout(Map<Integer, Pair<Byte, Integer>> keys) {
        keymap = keys;
    }

    public final Map<Integer, Pair<Byte, Integer>> Layout() {
        changed = true;
        return keymap;
    }

    public final void unchanged() {
        changed = false;
    }

    public final void writeData(final MaplePacketLittleEndianWriter mplew) {
        mplew.write(keymap.isEmpty() ? 1 : 0);
        if (keymap.isEmpty()) {
            return;
        }
        Pair<Byte, Integer> binding;
        for (int x = 0; x < 89; x++) { // Normal
            binding = keymap.get(x);
            if (binding != null) {
                mplew.write(binding.getLeft());
                mplew.writeInt(binding.getRight());
            } else {
                mplew.write(0);
                mplew.writeInt(0);
            }
        }
    }

    public final void saveKeys(Connection con, final int charid) throws SQLException {
        if (!changed) {
            return;
        }

        PreparedStatement ps = con.prepareStatement("DELETE FROM keymap WHERE characterid = ?");
        ps.setInt(1, charid);
        ps.execute();
        ps.close();
        if (keymap.isEmpty()) {
            return;
        }
        boolean first = true;
        StringBuilder query = new StringBuilder();

        for (Entry<Integer, Pair<Byte, Integer>> keybinding : keymap.entrySet()) {
            if (first) {
                first = false;
                query.append("INSERT INTO keymap VALUES (");
            } else {
                query.append(",(");
            }
            query.append("DEFAULT,");
            query.append(charid).append(",");
            query.append(keybinding.getKey().intValue()).append(",");
            query.append(keybinding.getValue().getLeft().byteValue()).append(",");
            query.append(keybinding.getValue().getRight().intValue()).append(")");
        }
        ps = con.prepareStatement(query.toString());
        ps.execute();
        ps.close();
    }
}
