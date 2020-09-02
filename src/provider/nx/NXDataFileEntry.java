package provider.nx;

import provider.MapleDataFileEntry;
import provider.pkgnx.format.NXNode;

/**
 * @author Aaron
 * @version 1.0
 * @since 6/8/13
 */
public class NXDataFileEntry extends NXDataEntry implements MapleDataFileEntry {

    public NXDataFileEntry(NXNode node, NXData parent) {
        super(node, parent);
    }

    @Override
    public void setOffset(int offset) {
        throw new UnsupportedOperationException("NXDataFileEntry :: implement only if really needed...");
    }
}
