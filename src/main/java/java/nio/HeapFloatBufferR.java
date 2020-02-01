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



 * A read-only HeapFloatBuffer.  This class extends the corresponding
 * read/write class, overriding the mutation methods to throw a {@link
 * ReadOnlyBufferException} and overriding the view-buffer methods to return an
 * instance of this class rather than of the superclass.

 */

class HeapFloatBufferR
    extends HeapFloatBuffer
{

    // For speed these fields are actually declared in X-Buffer;
    // these declarations are here as documentation
    /*




    */

    HeapFloatBufferR(int cap, int lim) {            // package-private







        super(cap, lim);
        this.isReadOnly = true;

    }

    HeapFloatBufferR(float[] buf, int off, int len) { // package-private







        super(buf, off, len);
        this.isReadOnly = true;

    }

    protected HeapFloatBufferR(float[] buf,
                                   int mark, int pos, int lim, int cap,
                                   int off)
    {







        super(buf, mark, pos, lim, cap, off);
        this.isReadOnly = true;

    }

    @Override
    public FloatBuffer slice() {
        return new HeapFloatBufferR(hb,
                                        -1,
                                        0,
                                        this.remaining(),
                                        this.remaining(),
                                        this.position() + offset);
    }

    @Override
    public FloatBuffer duplicate() {
        return new HeapFloatBufferR(hb,
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
    public boolean isReadOnly() {
        return true;
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
    public FloatBuffer put(float[] src, int offset, int length) {








        throw new ReadOnlyBufferException();

    }

    @Override
    public FloatBuffer put(FloatBuffer src) {























        throw new ReadOnlyBufferException();

    }

    @Override
    public FloatBuffer compact() {







        throw new ReadOnlyBufferException();

    }






































































































































































































































































































































































    @Override
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }



}
