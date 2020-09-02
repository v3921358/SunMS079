package provider.nx;

import provider.MapleDataEntity;
import provider.pkgnx.format.NXNode;

/**
 * @author Aaron
 * @version 1.0
 * @since 6/8/13
 */
public class NXDataEntity implements MapleDataEntity {

    private final NXNode node;
    private final NXData parent;

    public NXDataEntity(NXNode node, NXData parent) {
        this.node = node;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return node.getName();
    }

    @Override
    public MapleDataEntity getParent() {
        return new NXDataEntity(parent.getNode(), parent.getParentAsNX());
    }
}
