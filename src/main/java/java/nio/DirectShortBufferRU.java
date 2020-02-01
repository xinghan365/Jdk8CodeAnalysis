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

import java.io.FileDescriptor;
import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.misc.VM;
import sun.nio.ch.DirectBuffer;


class DirectShortBufferRU



    extends DirectShortBufferU

    implements DirectBuffer
{















































































































































    // For duplicates and slices
    //
    DirectShortBufferRU(DirectBuffer db,         // package-private
                               int mark, int pos, int lim, int cap,
                               int off)
    {








        super(db, mark, pos, lim, cap, off);

    }

    @Override
    public ShortBuffer slice() {
        int pos = this.position();
        int lim = this.limit();
        assert (pos <= lim);
        int rem = (pos <= lim ? lim - pos : 0);
        int off = (pos << 1);
        assert (off >= 0);
        return new DirectShortBufferRU(this, -1, 0, rem, rem, off);
    }

    @Override
    public ShortBuffer duplicate() {
        return new DirectShortBufferRU(this,
                                              this.markValue(),
                                              this.position(),
                                              this.limit(),
                                              this.capacity(),
                                              0);
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
    public ShortBuffer put(ShortBuffer src) {




































        throw new ReadOnlyBufferException();

    }

    @Override
    public ShortBuffer put(short[] src, int offset, int length) {




























        throw new ReadOnlyBufferException();

    }

    @Override
    public ShortBuffer compact() {












        throw new ReadOnlyBufferException();

    }

    @Override
    public boolean isDirect() {
        return true;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }















































    @Override
    public ByteOrder order() {





        return ((ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN)
                ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);

    }


























}
