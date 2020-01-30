/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.base64;

import java.io.ByteArrayOutputStream;

//


/**
 * The Class Base64Base.
 */
public class Base64Base {

    /**
     * The buffer size.
     */
    private final int bufferSize;

    /**
     * The Constant DEF_BUFFER_SIZE.
     */
    private static final int DEF_BUFFER_SIZE = 1048576; // 1 MB

    /**
     * The Constructor.
     */
    public Base64Base() {
        bufferSize = Base64Base.DEF_BUFFER_SIZE;
    }

    /**
     * The Constructor.
     *
     * @param bufferSize the buffer size
     */
    public Base64Base(final int bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
     * Gets the buffer size.
     *
     * @return the buffer size
     */
    public int getBufferSize() {
        return bufferSize;
    }

    // if we want to encode large files, we should encode it in chunks that are
    // a multiple of 57 bytes.
    // This ensures that the base64 lines line up and that we do not end up with
    // padding in the middle.
    // 57 bytes of data fills one complete base64 line (76 == 57*4/3):

    /**
     * The Constant DECODED_CHUNK_SIZE.
     */
    public static final int DECODED_CHUNK_SIZE = 57;

    /**
     * The Constant ENCODED_CHUNK_SIZE.
     */
    public static final int ENCODED_CHUNK_SIZE = 76;

    /**
     * The Constant toTable.
     */
    static final char[] toTable = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
        'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+',
        '/'};

    /**
     * The Constant fromTable.
     */
    static final byte[] fromTable = {-1, -1, -1, -1, -1, -1, -1, -1, // 0
        -1, -1, -1, -1, -1, -1, -1, -1, // 8
        -1, -1, -1, -1, -1, -1, -1, -1, // 16
        -1, -1, -1, -1, -1, -1, -1, -1, // 24
        -1, -1, -1, -1, -1, -1, -1, -1, // 32
        -1, -1, -1, 62, -1, -1, -1, 63, // 40
        52, 53, 54, 55, 56, 57, 58, 59, // 48
        60, 61, -1, -1, -1, 0, -1, -1, // 56
        -1, 0, 1, 2, 3, 4, 5, 6, // 64
        7, 8, 9, 10, 11, 12, 13, 14, // 72
        15, 16, 17, 18, 19, 20, 21, 22, // 80
        23, 24, 25, -1, -1, -1, -1, -1, // 88
        -1, 26, 27, 28, 29, 30, 31, 32, // 96
        33, 34, 35, 36, 37, 38, 39, 40, // 104
        41, 42, 43, 44, 45, 46, 47, 48, // 112
        49, 50, 51, -1, -1, -1, -1, -1 // 120
    };

    /**
     * Encode.
     *
     * @param len            the len
     * @param currLineLength the curr line length
     * @param spos           the spos
     * @param bin            the bin
     * @return the string
     */
    public static String encode(final byte[] bin, final int spos, final int len, final int currLineLength) {

        int currentLineLength = currLineLength;
        int input;
        int bytes;
        final StringBuilder sb = new StringBuilder();
        int apos = spos;
        final int epos = apos + len - 1;
        do {
            input = 0;

            // get the next three bytes into "input" (and count how many we
            // actually get)
            for (bytes = 0; bytes < 3 && apos <= epos; bytes++, apos++) {
                input <<= 8;
                input += bin[apos];
            }

            int bits = bytes * 8;
            while (bits > 0) {
                bits -= 6;
                //noinspection ConstantConditions
                final int index = ((bits < 0) ? input << -bits : input >> bits) & 0x3F;
                sb.append(Base64Base.toTable[index]);
                if (++currentLineLength == Base64Base.ENCODED_CHUNK_SIZE) { // ensure
                    // proper
                    // line
                    // length
                    sb.append("\n");
                    currentLineLength = 0;
                }
            }

        } while (bytes == 3);

        if (bytes > 0)
            for (int i = bytes; i < 3; i++)
                sb.append("=");
        return sb.toString();
    }

    /**
     * Decode.
     *
     * @param sin the sin
     * @return the byte[]
     */
    public static byte[] decode(final String sin) {

        return Base64Base.decode(sin.toCharArray(), 0, sin.length());
    }

    /**
     * Decode.
     *
     * @param len  the len
     * @param spos the spos
     * @param cin  the cin
     * @return the byte[]
     */
    public static byte[] decode(final char[] cin, final int spos, final int len) {

        int chars;
        final byte[] input = new byte[] {0, 0, 0, 0};
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        int apos = spos;
        final int epos = apos + len - 1;
        do {
            for (chars = 0; chars < 4 && apos <= epos; apos++) {
                if (cin[apos] == '=')
                    break;
                final char c = cin[apos];
                if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '+' || c == '/') {
                    input[chars] = Base64Base.fromTable[c];
                    chars++;
                }
            }
            if (chars >= 2) {
                data.write((input[0] << 2) + (input[1] >> 4));
                if (chars >= 3) {
                    data.write((input[1] << 4) + (input[2] >> 2));
                    if (chars >= 4)
                        data.write((input[2] << 6) + input[3]);
                }
            }

        } while (chars == 4);
        return data.toByteArray();
    }
}
