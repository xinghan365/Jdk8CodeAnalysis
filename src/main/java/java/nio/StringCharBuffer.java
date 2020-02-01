/*
 * Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.nio;


// ## If the sequence is a string, use reflection to share its array

class StringCharBuffer                                  // package-private
    extends CharBuffer
{
    CharSequence str;

    StringCharBuffer(CharSequence s, int start, int end) { // package-private
        super(-1, start, end, s.length());
        int n = s.length();
        if ((start < 0) || (start > n) || (end < start) || (end > n)) {
            throw new IndexOutOfBoundsException();
        }
        str = s;
    }

    @Override
    public CharBuffer slice() {
        return new StringCharBuffer(str,
                                    -1,
                                    0,
                                    this.remaining(),
                                    this.remaining(),
                                    offset + this.position());
    }

    private StringCharBuffer(CharSequence s,
                             int mark,
                             int pos,
                             int limit,
                             int cap,
                             int offset) {
        super(mark, pos, limit, cap, null, offset);
        str = s;
    }

    @Override
    public CharBuffer duplicate() {
        return new StringCharBuffer(str, markValue(),
                                    position(), limit(), capacity(), offset);
    }

    @Override
    public CharBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override
    public final char get() {
        return str.charAt(nextGetIndex() + offset);
    }

    @Override
    public final char get(int index) {
        return str.charAt(checkIndex(index) + offset);
    }

    @Override
    char getUnchecked(int index) {
        return str.charAt(index + offset);
    }

    // ## Override bulk get methods for better performance

    @Override
    public final CharBuffer put(char c) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public final CharBuffer put(int index, char c) {
        throw new ReadOnlyBufferException();
    }

    @Override
    public final CharBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override
    public final boolean isReadOnly() {
        return true;
    }

    @Override
    final String toString(int start, int end) {
        return str.toString().substring(start + offset, end + offset);
    }

    @Override
    public final CharBuffer subSequence(int start, int end) {
        try {
            int pos = position();
            return new StringCharBuffer(str,
                                        -1,
                                        pos + checkIndex(start, pos),
                                        pos + checkIndex(end, pos),
                                        capacity(),
                                        offset);
        } catch (IllegalArgumentException x) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean isDirect() {
        return false;
    }

    @Override
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }

}
