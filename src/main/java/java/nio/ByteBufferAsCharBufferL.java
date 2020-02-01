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

// -- This file was mechanically generated: Do not edit! -- //

package java.nio;


class ByteBufferAsCharBufferL                  // package-private
    extends CharBuffer
{



    protected final ByteBuffer bb;
    protected final int offset;



    ByteBufferAsCharBufferL(ByteBuffer bb) {   // package-private

        super(-1, 0,
              bb.remaining() >> 1,
              bb.remaining() >> 1);
        this.bb = bb;
        // enforce limit == capacity
        int cap = this.capacity();
        this.limit(cap);
        int pos = this.position();
        assert (pos <= cap);
        offset = pos;



    }

    ByteBufferAsCharBufferL(ByteBuffer bb,
                                     int mark, int pos, int lim, int cap,
                                     int off)
    {

        super(mark, pos, lim, cap);
        this.bb = bb;
        offset = off;



    }

    @Override
    public CharBuffer slice() {
        int pos = this.position();
        int lim = this.limit();
        assert (pos <= lim);
        int rem = (pos <= lim ? lim - pos : 0);
        int off = (pos << 1) + offset;
        assert (off >= 0);
        return new ByteBufferAsCharBufferL(bb, -1, 0, rem, rem, off);
    }

    @Override
    public CharBuffer duplicate() {
        return new ByteBufferAsCharBufferL(bb,
                                                    this.markValue(),
                                                    this.position(),
                                                    this.limit(),
                                                    this.capacity(),
                                                    offset);
    }

    @Override
    public CharBuffer asReadOnlyBuffer() {

        return new ByteBufferAsCharBufferRL(bb,
                                                 this.markValue(),
                                                 this.position(),
                                                 this.limit(),
                                                 this.capacity(),
                                                 offset);



    }



    protected int ix(int i) {
        return (i << 1) + offset;
    }

    @Override
    public char get() {
        return Bits.getCharL(bb, ix(nextGetIndex()));
    }

    @Override
    public char get(int i) {
        return Bits.getCharL(bb, ix(checkIndex(i)));
    }


   @Override
   char getUnchecked(int i) {
        return Bits.getCharL(bb, ix(i));
    }




    @Override
    public CharBuffer put(char x) {

        Bits.putCharL(bb, ix(nextPutIndex()), x);
        return this;



    }

    @Override
    public CharBuffer put(int i, char x) {

        Bits.putCharL(bb, ix(checkIndex(i)), x);
        return this;



    }

    @Override
    public CharBuffer compact() {

        int pos = position();
        int lim = limit();
        assert (pos <= lim);
        int rem = (pos <= lim ? lim - pos : 0);

        ByteBuffer db = bb.duplicate();
        db.limit(ix(lim));
        db.position(ix(0));
        ByteBuffer sb = db.slice();
        sb.position(pos << 1);
        sb.compact();
        position(rem);
        limit(capacity());
        discardMark();
        return this;



    }

    @Override
    public boolean isDirect() {
        return bb.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }



    @Override
    public String toString(int start, int end) {
        if ((end > limit()) || (start > end)) {
            throw new IndexOutOfBoundsException();
        }
        try {
            int len = end - start;
            char[] ca = new char[len];
            CharBuffer cb = CharBuffer.wrap(ca);
            CharBuffer db = this.duplicate();
            db.position(start);
            db.limit(end);
            cb.put(db);
            return new String(ca);
        } catch (StringIndexOutOfBoundsException x) {
            throw new IndexOutOfBoundsException();
        }
    }


    // --- Methods to support CharSequence ---

    @Override
    public CharBuffer subSequence(int start, int end) {
        int pos = position();
        int lim = limit();
        assert (pos <= lim);
        pos = (pos <= lim ? pos : lim);
        int len = lim - pos;

        if ((start < 0) || (end > len) || (start > end)) {
            throw new IndexOutOfBoundsException();
        }
        return new ByteBufferAsCharBufferL(bb,
                                                  -1,
                                                  pos + start,
                                                  pos + end,
                                                  capacity(),
                                                  offset);
    }




    @Override
    public ByteOrder order() {




        return ByteOrder.LITTLE_ENDIAN;

    }

}
