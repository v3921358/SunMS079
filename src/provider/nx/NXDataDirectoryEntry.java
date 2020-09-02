package provider.nx;

import provider.MapleDataDirectoryEntry;
import provider.MapleDataEntity;
import provider.MapleDataEntry;
import provider.MapleDataFileEntry;
import provider.pkgnx.format.NXNode;
import provider.pkgnx.format.nodes.NXNullNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron
 * @version 1.0
 * @since 6/8/13
 */
public class NXDataDirectoryEntry implements MapleDataDirectoryEntry {

    private final NXNode node;
    private final NXData parent;

    public NXDataDirectoryEntry(NXNode node, NXData parent) {
        this.node = node;
        this.parent = parent;
    }

    @Override
    public List<MapleDataDirectoryEntry> getSubdirectories() {
        List<MapleDataDirectoryEntry> mdde = new ArrayList<>();
        for (NXNode child : node) {
            if (child instanceof NXNullNode) {
                mdde.add(new NXDataDirectoryEntry(child, new NXData(node, parent)));
            }
        }
        return mdde;
    }

    @Override
    public List<MapleDataFileEntry> getFiles() {
        List<MapleDataFileEntry> mdde = new ArrayList<>();
        for (NXNode child : node) {
            if (!(child instanceof NXNullNode)) {
                mdde.add(new NXDataFileEntry(child, new NXData(node, parent)));
            }
        }
        return mdde;
    }

    @Override
    public MapleDataEntry getEntry(String name) {
        return new NXDataEntry(node.getChild(name), new NXData(node, parent));
    }

    @Override
    public String getName() {
        return node.getName();
    }

    @Override
    public MapleDataEntity getParent() {
        return new NXDataEntity(parent.getNode(), parent.getParentAsNX());
    }

    @Override
    public int getSize() {
        return node.getChildCount();
    }

    @Override
    public int getChecksum() {
        return -1; // NOT USED ANYWHERE IN THE SOURCE.
    }

    @Override
    public int getOffset() {
        int i = 0;
        NXNode[] nodes = node.getFile().getNodes();
        for (; i < nodes.length; i++) {
            if (nodes[i] == node) {
                break;
            }
        }
        return (int) (node.getFile().getHeader().getNodeOffset() + (20 * i));
    }
}
