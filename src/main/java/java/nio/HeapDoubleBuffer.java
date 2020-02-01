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

 * A read/write HeapDoubleBuffer.






 */

class HeapDoubleBuffer
    extends DoubleBuffer
{

    // For speed these fields are actually declared in X-Buffer;
    // these declarations are here as documentation
    /*

    protected final double[] hb;
    protected final int offset;

    */

    HeapDoubleBuffer(int cap, int lim) {            // package-private

        super(-1, 0, lim, cap, new double[cap], 0);
        /*
        hb = new double[cap];
        offset = 0;
        */




    }

    HeapDoubleBuffer(double[] buf, int off, int len) { // package-private

        super(-1, off, off + len, buf.length, buf, 0);
        /*
        hb = buf;
        offset = 0;
        */




    }

    protected HeapDoubleBuffer(double[] buf,
                                   int mark, int pos, int lim, int cap,
                                   int off)
    {

        super(mark, pos, lim, cap, buf, off);
        /*
        hb = buf;
        offset = off;
        */




    }

    @Override
    public DoubleBuffer slice() {
        return new HeapDoubleBuffer(hb,
                                        -1,
                                        0,
                                        this.remaining(),
                                        this.remaining(),
                                        this.position() + offset);
    }

    @Override
    public DoubleBuffer duplicate() {
        return new HeapDoubleBuffer(hb,
                                        this.markValue(),
                                        this.position(),
                                        this.limit(),
                                        this.capacity(),
                                        offset);
    }

    @Override
    public DoubleBuffer asReadOnlyBuffer() {

        return new HeapDoubleBufferR(hb,
                                     this.markValue(),
                                     this.position(),
                                     this.limit(),
                                     this.capacity(),
                                     offset);



    }



    protected int ix(int i) {
        return i + offset;
    }

    @Override
    public double get() {
        return hb[ix(nextGetIndex())];
    }

    @Override
    public double get(int i) {
        return hb[ix(checkIndex(i))];
    }







    @Override
    public DoubleBuffer get(double[] dst, int offset, int length) {
        checkBounds(offset, length, dst.length);
        if (length > remaining()) {
            throw new BufferUnderflowException();
        }
        System.arraycopy(hb, ix(position()), dst, offset, length);
        position(position() + length);
        return this;
    }

    @Override
    public boolean isDirect() {
        return false;
    }



    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public DoubleBuffer put(double x) {

        hb[ix(nextPutIndex())] = x;
        return this;



    }

    @Override
    public DoubleBuffer put(int i, double x) {

        hb[ix(checkIndex(i))] = x;
        return this;



    }

    @Override
    public DoubleBuffer put(double[] src, int offset, int length) {

        checkBounds(offset, length, src.length);
        if (length > remaining()) {
            throw new BufferOverflowException();
        }
        System.arraycopy(src, offset, hb, ix(position()), length);
        position(position() + length);
        return this;



    }

    @Override
    public DoubleBuffer put(DoubleBuffer src) {

        if (src instanceof HeapDoubleBuffer) {
            if (src == this) {
                throw new IllegalArgumentException();
            }
            HeapDoubleBuffer sb = (HeapDoubleBuffer)src;
            int n = sb.remaining();
            if (n > remaining()) {
                throw new BufferOverflowException();
            }
            System.arraycopy(sb.hb, sb.ix(sb.position()),
                             hb, ix(position()), n);
            sb.position(sb.position() + n);
            position(position() + n);
        } else if (src.isDirect()) {
            int n = src.remaining();
            if (n > remaining()) {
                throw new BufferOverflowException();
            }
            src.get(hb, ix(position()), n);
            position(position() + n);
        } else {
            super.put(src);
        }
        return this;



    }

    @Override
    public DoubleBuffer compact() {

        System.arraycopy(hb, ix(position()), hb, ix(0), remaining());
        position(remaining());
        limit(capacity());
        discardMark();
        return this;



    }






































































































































































































































































































































































    @Override
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }



}
