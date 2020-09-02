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
package provider.pkgnx.format;

import provider.pkgnx.NXFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The basic information container for the NX file format.
 *
 * @author Aaron Weiss
 * @version 1.0.7
 * @since 5/26/13
 */
public abstract class NXNode implements Iterable<NXNode> {

    private static final EmptyNodeIterator EMPTY_NODE_ITERATOR = new EmptyNodeIterator();
    private static final int MIN_COUNT_FOR_MAPS = 41;

    protected final String name;
    protected final NXFile file;
    protected final long childIndex;
    protected final int childCount;
    private final NXNode[] children;
    private Map<String, NXNode> childMap;

    /**
     * Sets up the basic information for the {@code NXNode}.
     *
     * @param name the name of the node
     * @param file the file the node is from
     * @param childIndex the index of the first child of the node
     * @param childCount the number of children
     */
    public NXNode(String name, NXFile file, long childIndex, int childCount) {
        this.name = name;
        this.file = file;
        this.childIndex = childIndex;
        this.childCount = childCount;
        if (childCount >= MIN_COUNT_FOR_MAPS) {
            childMap = new HashMap<>();
            children = null;
        } else if (childCount > 0) {
            children = new NXNode[childCount];
        } else {
            children = null;
        }
    }

    /**
     * Populates the children {@code Map} for this node.
     */
    public void populateChildren() {
        if (childCount == 0) {
            return;
        }
        NXNode[] nodes = file.getNodes();
        if (childMap != null && childMap.isEmpty()) {
            for (int i = (int) childIndex; i < childIndex + childCount; i++) {
                childMap.put(nodes[i].getName(), nodes[i]);
            }
        } else if (children[0] == null) {
            int k = 0;
            for (int i = (int) childIndex; i < childIndex + childCount; i++) {
                children[k++] = nodes[i];
            }
        }
    }

    /**
     * Gets the value of this node universally.
     *
     * @return the value as an {@code Object}
     */
    public abstract Object get();

    /**
     * Gets a child node by {@code name}. Returns null if child is not present.
     *
     * @param name the name of the child
     * @return the child {@code NXNode}
     */
    public NXNode getChild(String name) {
        if (childCount == 0) {
            return null;
        }
        return searchChild(name);
    }

    /**
     * Determines whether or not this node has a child by the specified
     * {@code name}.
     *
     * @param name the name of the child
     * @return whether or not this node has a child by the specified
     * {@code name}
     */
    public boolean hasChild(String name) {
        if (childCount == 0) {
            return false;
        }
        return searchChild(name) != null;
    }

    /**
     * Searches for a specific child node by {@code name}. Internally, this
     * deals with how the children are stored.
     *
     * @param name the name of the child to find
     * @return the found child or null, if it doesn't exist
     */
    protected NXNode searchChild(String name) {
        if (childCount == 0 || (children != null && children[0] == null) || (childMap != null && childMap.isEmpty())) {
            return null;
        }
        if (childMap != null) {
            return childMap.get(name);
        }
        int min = 0, max = childCount - 1;
        String minVal = children[min].getName(), maxVal = children[max].getName();
        while (true) {
            if (name.compareTo(minVal) <= 0) {
                return (name.equals(minVal)) ? children[min] : null;
            }
            if (name.compareTo(maxVal) >= 0) {
                return (name.equals(maxVal)) ? children[max] : null;
            }

            int pivot = (min + max) >> 1;
            String pivotVal = children[pivot].getName();

            if (name.compareTo(pivotVal) > 0) {
                min = pivot + 1;
                max--;
            } else if (name.equals(pivotVal)) {
                return children[pivot];
            } else {
                min++;
                max = pivot - 1;
            }
            minVal = children[min].getName();
            maxVal = children[max].getName();
        }
    }

    /**
     * Gets the name of the node.
     *
     * @return the name of this node
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the file that the node belonged to.
     *
     * @return the file owning this node
     */
    public NXFile getFile() {
        return file;
    }

    /**
     * Gets the number of children had by this node.
     *
     * @return number of child nodes
     */
    public int getChildCount() {
        return childCount;
    }

    /**
     * Gets the index of the first child of this node.
     *
     * @return first child node index
     */
    public long getFirstChildIndex() {
        return childIndex;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof NXNode)) {
            return false;
        } else {
            return obj == this || (((NXNode) obj).getName().equals(getName())
                    && ((NXNode) obj).getChildCount() == getChildCount()
                    && ((NXNode) obj).getFirstChildIndex() == getFirstChildIndex()
                    && ((((NXNode) obj).get() == null && get() == null) || ((NXNode) obj).get().equals(get())));
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public Iterator<NXNode> iterator() {
        return (childCount == 0) ? EMPTY_NODE_ITERATOR : (childMap != null) ? childMap.values().iterator() : Arrays.asList(children).iterator();
    }

    /**
     * A silent, empty iterator for childless {@code NXNode}s.
     *
     * @author Aaron Weiss
     * @version 1.0
     * @since 5/26/13
     */
    private static class EmptyNodeIterator implements Iterator<NXNode> {

        /**
         * Creates an {@code EmptyNodeIterator}.
         */
        private EmptyNodeIterator() {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public NXNode next() {
            return null;
        }

        @Override
        public void remove() {
        }
    }
}
