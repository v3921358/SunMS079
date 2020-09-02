package provider.nx;

import provider.MapleCanvas;
import provider.pkgnx.format.nodes.NXBitmapNode;

import java.awt.image.BufferedImage;

/**
 * @author Aaron
 * @version 1.0
 * @since 6/8/13
 */
public class NXCanvas implements MapleCanvas {

    private final NXBitmapNode bitmapNode;
    private BufferedImage cache = null;

    public NXCanvas(NXBitmapNode bitmapNode) {
        this.bitmapNode = bitmapNode;
    }

    @Override
    public int getHeight() {
        ensureCached();
        return cache.getHeight();
    }

    @Override
    public int getWidth() {
        ensureCached();
        return cache.getWidth();
    }

    @Override
    public BufferedImage getImage() {
        ensureCached();
        return cache;
    }

    private void ensureCached() {
        if (cache == null) {
            cache = bitmapNode.getImage();
        }
    }
}
