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
package provider.pkgnx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.pkgnx.format.NXHeader;
import provider.pkgnx.format.NXNode;
import provider.pkgnx.format.NXTables;
import provider.pkgnx.util.NodeParser;
import provider.pkgnx.util.SeekableLittleEndianAccessor;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An memory-mapped file for reading specification-compliant NX files.
 *
 * @author Aaron Weiss
 * @version 2.0.1
 * @since 5/26/13
 */
public class NXFile {

    public static final Logger logger = LoggerFactory.getLogger(NXFile.class);
    private final SeekableLittleEndianAccessor slea;
    private final String filePath;
    private boolean parsed;

    private NXHeader header;
    private NXTables tables;
    private NXNode[] nodes;

    /**
     * Creates a new {@code NXFile} from the specified {@code path}.
     *
     * @param path the absolute or relative path to the file
     * @throws IOException if something goes wrong in reading the file
     */
    public NXFile(String path) throws IOException {
        this(Paths.get(path));
    }

    /**
     * Creates a new {@code NXFile} from the specified {@code path}.
     *
     * @param path the absolute or relative path to the file
     * @throws IOException if something goes wrong in reading the file
     */
    public NXFile(Path path) throws IOException {
        this(path, true);
    }

    /**
     * Creates a new {@code NXFile} from the specified {@code path} with the
     * option to parse later.
     *
     * @param path the absolute or relative path to the file
     * @param parsedImmediately whether or not to parse all nodes immediately
     * @throws IOException if something goes wrong in reading the file
     */
    public NXFile(String path, boolean parsedImmediately) throws IOException {
        this(Paths.get(path), parsedImmediately);
    }

    /**
     * Creates a new {@code NXFile} from the specified {@code path} with the
     * option to parse later.
     *
     * @param path the absolute or relative path to the file
     * @param parsedImmediately whether or not to parse the file immediately
     * @throws IOException if something goes wrong in reading the file
     */
    public NXFile(Path path, boolean parsedImmediately) throws IOException {
        FileChannel channel = FileChannel.open(path);
        slea = new SeekableLittleEndianAccessor(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()));
        filePath = path.toString();
        if (parsedImmediately) {
            parse();
        }
    }

    /**
     * Parses the file completely.
     */
    public void parse() {
        if (parsed) {
            return;
        }
        header = new NXHeader(this, slea);
        nodes = new NXNode[(int) header.getNodeCount()];
        tables = new NXTables(header, slea);
        populateNodesTable();
        populateNodeChildren();
        parsed = true;
    }

    /**
     * Populates the node table by parsing all nodes.
     */
    private void populateNodesTable() {
        slea.seek(header.getNodeOffset());
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = NodeParser.parseNode(this, slea);
        }
    }

    /**
     * Populates the children of all nodes.
     */
    private void populateNodeChildren() {
        for (NXNode node : nodes) {
            node.populateChildren();
        }
    }

    /**
     * Gets the {@code NXHeader} of this file.
     *
     * @return this file's header
     */
    public NXHeader getHeader() {
        return header;
    }

    /**
     * Gets the {@code NXTables} from this file.
     *
     * @return this file's offset tables
     */
    public NXTables getTables() {
        return tables;
    }

    /**
     * Gets whether or not this file has been parsed.
     *
     * @return whether or not this file has been parsed
     */
    public boolean isParsed() {
        return parsed;
    }

    /**
     * Gets the path to this {@code NXFile}.
     *
     * @return the path to this file
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Gets an array of all of the {@code NXNode}s in this file.
     *
     * @return an array of all the nodes in this file
     */
    public NXNode[] getNodes() {
        return nodes;
    }

    /**
     * Gets the root {@code NXNode} of the file.
     *
     * @return the file's root node
     */
    public NXNode getRoot() {
        return nodes[0];
    }

    /**
     * Resolves the desired {@code path} to an {@code NXNode}.
     *
     * @param path the path to the node
     * @return the desired node
     */
    public NXNode resolve(String path) {
        if (path.equals("/")) {
            return getRoot();
        }
        return resolve(path.split("/"));
    }

    /**
     * Resolves the desired {@code path} to an {@code NXNode}.
     *
     * @param path the path to the node
     * @return the desired node
     */
    public NXNode resolve(String[] path) {
        NXNode cursor = getRoot();
        for (String path1 : path) {
            if (cursor == null) {
                return null;
            }
            cursor = cursor.getChild(path1);
        }
        return cursor;
    }
}
