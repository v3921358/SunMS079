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

import net.jpountz.lz4.LZ4Decompressor;
import net.jpountz.lz4.LZ4Factory;

/**
 * A simple wrapper for handling LZ4 decompression of byte arrays.
 *
 * @author Aaron Weiss
 * @version 1.0.0
 * @since 5/27/13
 */
public class Decompressor {

    private static final LZ4Decompressor decompressor = LZ4Factory.fastestInstance().decompressor();

    /**
     * Decompresses the supplied {@code input} to the desired {@code length}.
     *
     * @param input the data to decompress as an array of bytes
     * @param length the decompressed length of the data (or the length to
     * decompress)
     * @return the data to decompress
     */
    public static byte[] decompress(byte[] input, int length) {
        byte[] ret = new byte[length];
        decompressor.decompress(input, 0, ret, 0, length);
        return ret;
    }
}
