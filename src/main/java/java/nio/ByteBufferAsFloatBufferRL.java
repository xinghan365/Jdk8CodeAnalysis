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


class ByteBufferAsFloatBufferRL                  // package-private
    extends ByteBufferAsFloatBufferL
{








    ByteBufferAsFloatBufferRL(ByteBuffer bb) {   // package-private












        super(bb);

    }

    ByteBufferAsFloatBufferRL(ByteBuffer bb,
                                     int mark, int pos, int lim, int cap,
                                     int off)
    {





        super(bb, mark, pos, lim, cap, off);

    }

    @Override
    public FloatBuffer slice() {
        int pos = this.position();
        int lim = this.limit();
        assert (pos <= lim);
        int rem = (pos <= lim ? lim - pos : 0);
        int off = (pos << 2) + offset;
        assert (off >= 0);
        return new ByteBufferAsFloatBufferRL(bb, -1, 0, rem, rem, off);
    }

    @Override
    public FloatBuffer duplicate() {
        return new ByteBufferAsFloatBufferRL(bb,
                                                    this.markValue(),
                                                    this.position(),
                                                    this.limit(),
                                                    this.capacity(),
                                                    offset);
    }

    @Override
    public FloatBuffer asReadOnlyBuffer() {








        return duplicate();

    }























    @Override
    public FloatBuffer put(float x) {




        throw new ReadOnlyBufferException();

    }

    @Override
    public FloatBuffer put(int i, float x) {




        throw new ReadOnlyBufferException();

    }

    @Override
    public FloatBuffer compact() {

















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
