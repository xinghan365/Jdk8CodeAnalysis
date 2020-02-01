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


/**



 * A read-only HeapShortBuffer.  This class extends the corresponding
 * read/write class, overriding the mutation methods to throw a {@link
 * ReadOnlyBufferException} and overriding the view-buffer methods to return an
 * instance of this class rather than of the superclass.

 */

class HeapShortBufferR
    extends HeapShortBuffer
{

    // For speed these fields are actually declared in X-Buffer;
    // these declarations are here as documentation
    /*




    */

    HeapShortBufferR(int cap, int lim) {            // package-private







        super(cap, lim);
        this.isReadOnly = true;

    }

    HeapShortBufferR(short[] buf, int off, int len) { // package-private







        super(buf, off, len);
        this.isReadOnly = true;

    }

    protected HeapShortBufferR(short[] buf,
                                   int mark, int pos, int lim, int cap,
                                   int off)
    {







        super(buf, mark, pos, lim, cap, off);
        this.isReadOnly = true;

    }

    @Override
    public ShortBuffer slice() {
        return new HeapShortBufferR(hb,
                                        -1,
                                        0,
                                        this.remaining(),
                                        this.remaining(),
                                        this.position() + offset);
    }

    @Override
    public ShortBuffer duplicate() {
        return new HeapShortBufferR(hb,
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
    public boolean isReadOnly() {
        return true;
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
    public ShortBuffer put(short[] src, int offset, int length) {








        throw new ReadOnlyBufferException();

    }

    @Override
    public ShortBuffer put(ShortBuffer src) {























        throw new ReadOnlyBufferException();

    }

    @Override
    public ShortBuffer compact() {







        throw new ReadOnlyBufferException();

    }






































































































































































































































































































































































    @Override
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }



}
