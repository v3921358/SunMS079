/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2013 Aaron Weiss
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package provider.pkgnx.format.nodes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.pkgnx.NXFile;
import provider.pkgnx.format.NXNode;
import provider.pkgnx.util.SeekableLittleEndianAccessor;

/**
 * An {@code NXNode} representing a {@code String}.
 *
 * @author Aaron Weiss
 * @version 2.0.0
 * @since 5/27/13
 */
public class NXStringNode extends NXNode {

    public static final Logger logger = LoggerFactory.getLogger(NXStringNode.class);
    private final long stringIndex;

    /**
     * Creates a new {@code NXStringNode}.
     *
     * @param name the name of the node
     * @param file the file the node is from
     * @param childIndex the index of the first child of the node
     * @param childCount the number of children
     * @param slea the {@code SeekableLittleEndianAccessor} to read from
     */
    public NXStringNode(String name, NXFile file, long childIndex, int childCount, SeekableLittleEndianAccessor slea) {
        super(name, file, childIndex, childCount);
        stringIndex = slea.getUnsignedInt();
        slea.skip(4);
    }

    @Override
    public Object get() {
        return getString();
    }

    /**
     * Gets the value of this node as a {@code String}.
     *
     * @return the node value
     */
    public String getString() {
        return file.getTables().getString(stringIndex);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof NXStringNode)) {
            return false;
        } else {
            return obj == this || (((NXNode) obj).getName().equals(getName())
                    && ((NXNode) obj).getChildCount() == getChildCount()
                    && ((NXNode) obj).getFirstChildIndex() == getFirstChildIndex()
                    && ((NXStringNode) obj).stringIndex == stringIndex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.stringIndex ^ (this.stringIndex >>> 32));
        return hash;
    }
}
