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


class ByteBufferAsShortBufferRL                  // package-private
    extends ByteBufferAsShortBufferL
{








    ByteBufferAsShortBufferRL(ByteBuffer bb) {   // package-private












        super(bb);

    }

    ByteBufferAsShortBufferRL(ByteBuffer bb,
                                     int mark, int pos, int lim, int cap,
                                     int off)
    {





        super(bb, mark, pos, lim, cap, off);

    }

    @Override
    public ShortBuffer slice() {
        int pos = this.position();
        int lim = this.limit();
        assert (pos <= lim);
        int rem = (pos <= lim ? lim - pos : 0);
        int off = (pos << 1) + offset;
        assert (off >= 0);
        return new ByteBufferAsShortBufferRL(bb, -1, 0, rem, rem, off);
    }

    @Override
    public ShortBuffer duplicate() {
        return new ByteBufferAsShortBufferRL(bb,
                                                    this.markValue(),
                                                    this.position(),
                                                    this.limit(),
                                                    this.capacity(),
                                                    offset);
    }

    @Override
    public ShortBuffer asReadOnlyBuffer() {








        return duplicate();

    }























    @Override
    public ShortBuffer put(short x) {




        throw new ReadOnlyBufferException();

    }

    @Override
    public ShortBuffer put(int i, short x) {




        throw new ReadOnlyBufferException();

    }

    @Override
    public ShortBuffer compact() {

















        throw new ReadOnlyBufferException();

    }

    @Override
    public boolean isDirect() {
        return bb.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }











































    @Override
    public ByteOrder order() {




        return ByteOrder.LITTLE_ENDIAN;

    }

}
