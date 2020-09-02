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
package provider.pkgnx.util;

import provider.pkgnx.NXException;
import provider.pkgnx.NXFile;
import provider.pkgnx.format.NXNode;
import provider.pkgnx.format.nodes.*;

/**
 * A basic utility to parse data into an {@code NXNode}.
 *
 * @author Aaron Weiss
 * @version 2.0.0
 * @since 5/27/13
 */
public class NodeParser {

    /**
     * Parses the next {@code NXNode} from the supplied data.
     *
     * @param file the file to read nodes from
     * @param slea the {@code SeekableLittleEndianAccessor} to read the node
     * from
     * @return the newly parsed node
     */
    public static NXNode parseNode(NXFile file, SeekableLittleEndianAccessor slea) {
        String name = file.getTables().getString(slea.getUnsignedInt());
        long childIndex = slea.getUnsignedInt();
        int childCount = slea.getUnsignedShort();
        int type = slea.getUnsignedShort();
        switch (type) {
            case 0:
                return new NXNullNode(name, file, childIndex, childCount, slea);
            case 1:
                return new NXLongNode(name, file, childIndex, childCount, slea);
            case 2:
                return new NXDoubleNode(name, file, childIndex, childCount, slea);
            case 3:
                return new NXStringNode(name, file, childIndex, childCount, slea);
            case 4:
                return new NXPointNode(name, file, childIndex, childCount, slea);
            case 5:
                return new NXBitmapNode(name, file, childIndex, childCount, slea);
            case 6:
                return new NXAudioNode(name, file, childIndex, childCount, slea);
            default:
                throw new NXException("Failed to parse nodes. Encountered invalid node type (" + type + ") in file.");
        }
    }
}
