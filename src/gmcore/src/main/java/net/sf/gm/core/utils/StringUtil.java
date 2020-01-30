/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

//


/**
 * This class contains utility functions for manipulating strings.
 */
public class StringUtil {

    /**
     * Escape C string.
     *
     * @param in the in
     * @return the string
     */
    public static String escapeCString(final String in) {

        final StringBuilder sb = new StringBuilder();
        final int strLen = in.length();
        char ch;
        for (int i = 0; i < strLen; i++) {
            ch = in.charAt(i);
            switch (ch) {
                case 0:
                    return sb.toString();
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\'':
                    sb.append("\\'");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    if (ch < 0x20 || ch > 0x7e)
                        sb.append(String.format("\\x%02x", (int) ch));
                    else
                        sb.append(ch);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * Unescape C string.
     *
     * @param in the in
     * @return the string
     */
    public static String unescapeCString(final String in) {

        final StringBuilder sb = new StringBuilder();
        final int strLen = in.length();
        char ch, chNext;
        boolean escaped = false;
        for (int i = 0; i < strLen; i++) {
            ch = in.charAt(i);
            chNext = i + 1 < strLen ? in.charAt(i + 1) : '\0';
            if (escaped)
                switch (ch) {
                    case 'b':
                        sb.append("\b");
                        break;
                    case 't':
                        sb.append("\t");
                        break;
                    case 'n':
                        sb.append("\n");
                        break;
                    case 'f':
                        sb.append("\f");
                        break;
                    case 'r':
                        sb.append("\r");
                        break;
                    case 'x': // hexadecimal 1-3 digits
                        if (!StringUtil.isHexDigit(chNext)) {
                            sb.append("x");
                            continue;
                        }
                    {
                        final StringBuilder digits = new StringBuilder();
                        i = StringUtil.consumeHexDigits(digits, in, i, 3) - 1;
                        throw new UnsupportedOperationException("unescape cstring: \\x" +
                            digits.toString());
                    }
                    // break;
                    case 'u': // hexadecimal 4 digits
                        if (!StringUtil.isHexDigit(chNext)) {
                            sb.append("u");
                            continue;
                        }
                    {
                        final StringBuilder digits = new StringBuilder();
                        i = StringUtil.consumeHexDigits(digits, in, i, 4) - 1;
                        if (digits.length() != 4) {
                            sb.append("u");
                            sb.append(digits.toString());
                            continue;
                        }
                        throw new UnsupportedOperationException("unescape cstring: \\u" +
                            digits.toString());
                    }
                    // break;
                    case 'U': // hexadecimal 8 digits
                        if (!StringUtil.isHexDigit(chNext)) {
                            sb.append("U");
                            continue;
                        }
                    {
                        final StringBuilder digits = new StringBuilder();
                        i = StringUtil.consumeHexDigits(digits, in, i, 8) - 1;
                        if (digits.length() != 8) {
                            sb.append("u");
                            sb.append(digits.toString());
                            continue;
                        }
                        throw new UnsupportedOperationException("unescape cstring: \\U" +
                            digits.toString());
                    }
                    // break;
                    case '0': // octal 1-3 digits
                        if (!StringUtil.isOctalDigit(chNext)) {
                            sb.append("0");
                            continue;
                        }
                    {
                        final StringBuilder digits = new StringBuilder();
                        i = StringUtil.consumeOctalDigits(digits, in, i, 8) - 1;
                        throw new UnsupportedOperationException("unescape cstring: \\0" +
                            digits.toString());
                    }
                    // break;
                    default:
                        sb.append(ch);
                        break;
                }
            else {
                if (ch == '\\') {
                    escaped = true;
                    continue;
                }
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * Checks if is hex digit.
     *
     * @param c the c
     * @return true, if is hex digit
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted") public static boolean isHexDigit(final char c) {

        return (c >= '0' && c <= '9') ||
            (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    /**
     * Checks if is octal digit.
     *
     * @param c the c
     * @return true, if is octal digit
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted") public static boolean isOctalDigit(final char c) {

        return (c >= '0' && c <= '7');
    }

    /**
     * Consume hex digits.
     *
     * @param out    the out
     * @param pos    the pos
     * @param maxLen the max len
     * @param in     the in
     * @return the int
     */
    public static int consumeHexDigits(final StringBuilder out, final String in,
        final int pos, final int maxLen) {

        int p = pos;
        final int strLen = in.length();
        for (int j = 0; j < maxLen && p < strLen; j++, p++) {
            final char c = in.charAt(p);
            if (!StringUtil.isHexDigit(c))
                return p;
            out.append(c);
        }
        return p;
    }

    /**
     * Consume octal digits.
     *
     * @param out    the out
     * @param pos    the pos
     * @param maxLen the max len
     * @param in     the in
     * @return the int
     */
    public static int consumeOctalDigits(final StringBuilder out, final String in,
        final int pos, final int maxLen) {

        int p = pos;
        final int strLen = in.length();
        for (int j = 0; j < maxLen && p < strLen; j++, p++) {
            final char c = in.charAt(p);
            if (!StringUtil.isOctalDigit(c))
                return p;
            out.append(c);
        }
        return p;
    }

    /**
     * Gets the short exception message.
     *
     * @param e the e
     * @return the short exception message
     */
    public static String getShortExceptionMessage(final Throwable e) {

        return e.getMessage() == null ? e.getClass().getName() : e.getMessage();
    }

    /**
     * Gets the exception message.
     *
     * @param e the e
     * @return the exception message
     */
    public static String getExceptionMessage(final Throwable e) {

        final StringBuilder sb = new StringBuilder();
        sb.append(e.getClass().getName());
        if (e.getMessage() != null) {
            sb.append(": ");
            sb.append(e.getMessage());
        }
        return sb.toString();
    }

    /**
     * Ltrim.
     *
     * @param in the in
     * @return the string
     */
    public static String ltrim(final String in) {

        if (in == null)
            return null;
        return in.replaceAll("^\\s+", "");
    }

    /**
     * Rtrim.
     *
     * @param in the in
     * @return the string
     */
    public static String rtrim(final String in) {

        if (in == null)
            return null;
        return in.replaceAll("\\s+$", "");
    }
}
