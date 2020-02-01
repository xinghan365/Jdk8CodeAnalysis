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



 * A read-only HeapDoubleBuffer.  This class extends the corresponding
 * read/write class, overriding the mutation methods to throw a {@link
 * ReadOnlyBufferException} and overriding the view-buffer methods to return an
 * instance of this class rather than of the superclass.

 */

class HeapDoubleBufferR
    extends HeapDoubleBuffer
{

    // For speed these fields are actually declared in X-Buffer;
    // these declarations are here as documentation
    /*




    */

    HeapDoubleBufferR(int cap, int lim) {            // package-private







        super(cap, lim);
        this.isReadOnly = true;

    }

    HeapDoubleBufferR(double[] buf, int off, int len) { // package-private







        super(buf, off, len);
        this.isReadOnly = true;

    }

    protected HeapDoubleBufferR(double[] buf,
                                   int mark, int pos, int lim, int cap,
                                   int off)
    {







        super(buf, mark, pos, lim, cap, off);
        this.isReadOnly = true;

    }

    @Override
    public DoubleBuffer slice() {
        return new HeapDoubleBufferR(hb,
                                        -1,
                                        0,
                                        this.remaining(),
                                        this.remaining(),
                                        this.position() + offset);
    }

    @Override
    public DoubleBuffer duplicate() {
        return new HeapDoubleBufferR(hb,
                                        this.markValue(),
                                        this.position(),
                                        this.limit(),
                                        this.capacity(),
                                        offset);
    }

    @Override
    public DoubleBuffer asReadOnlyBuffer() {








        return duplicate();

    }




































    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public DoubleBuffer put(double x) {




        throw new ReadOnlyBufferException();

    }

    @Override
    public DoubleBuffer put(int i, double x) {




        throw new ReadOnlyBufferException();

    }

    @Override
    public DoubleBuffer put(double[] src, int offset, int length) {








        throw new ReadOnlyBufferException();

    }

    @Override
    public DoubleBuffer put(DoubleBuffer src) {























        throw new ReadOnlyBufferException();

    }

    @Override
    public DoubleBuffer compact() {







        throw new ReadOnlyBufferException();

    }






































































































































































































































































































































































    @Override
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }



}
