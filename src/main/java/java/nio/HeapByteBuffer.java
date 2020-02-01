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

 * A read/write HeapByteBuffer.






 */

class HeapByteBuffer
    extends ByteBuffer
{

    // For speed these fields are actually declared in X-Buffer;
    // these declarations are here as documentation
    /*

    protected final byte[] hb;
    protected final int offset;

    */

    HeapByteBuffer(int cap, int lim) {            // package-private

        super(-1, 0, lim, cap, new byte[cap], 0);
        /*
        hb = new byte[cap];
        offset = 0;
        */




    }

    HeapByteBuffer(byte[] buf, int off, int len) { // package-private

        super(-1, off, off + len, buf.length, buf, 0);
        /*
        hb = buf;
        offset = 0;
        */




    }

    protected HeapByteBuffer(byte[] buf,
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
    public ByteBuffer slice() {
        return new HeapByteBuffer(hb,
                                        -1,
                                        0,
                                        this.remaining(),
                                        this.remaining(),
                                        this.position() + offset);
    }

    @Override
    public ByteBuffer duplicate() {
        return new HeapByteBuffer(hb,
                                        this.markValue(),
                                        this.position(),
                                        this.limit(),
                                        this.capacity(),
                                        offset);
    }

    @Override
    public ByteBuffer asReadOnlyBuffer() {

        return new HeapByteBufferR(hb,
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
    public byte get() {
        return hb[ix(nextGetIndex())];
    }

    @Override
    public byte get(int i) {
        return hb[ix(checkIndex(i))];
    }







    @Override
    public ByteBuffer get(byte[] dst, int offset, int length) {
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
    public ByteBuffer put(byte x) {

        hb[ix(nextPutIndex())] = x;
        return this;



    }

    @Override
    public ByteBuffer put(int i, byte x) {

        hb[ix(checkIndex(i))] = x;
        return this;



    }

    @Override
    public ByteBuffer put(byte[] src, int offset, int length) {

        checkBounds(offset, length, src.length);
        if (length > remaining()) {
            throw new BufferOverflowException();
        }
        System.arraycopy(src, offset, hb, ix(position()), length);
        position(position() + length);
        return this;



    }

    @Override
    public ByteBuffer put(ByteBuffer src) {

        if (src instanceof HeapByteBuffer) {
            if (src == this) {
                throw new IllegalArgumentException();
            }
            HeapByteBuffer sb = (HeapByteBuffer)src;
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
    public ByteBuffer compact() {

        System.arraycopy(hb, ix(position()), hb, ix(0), remaining());
        position(remaining());
        limit(capacity());
        discardMark();
        return this;



    }





    @Override
    byte _get(int i) {                          // package-private
        return hb[i];
    }

    @Override
    void _put(int i, byte b) {                  // package-private

        hb[i] = b;



    }

    // char



    @Override
    public char getChar() {
        return Bits.getChar(this, ix(nextGetIndex(2)), bigEndian);
    }

    @Override
    public char getChar(int i) {
        return Bits.getChar(this, ix(checkIndex(i, 2)), bigEndian);
    }



    @Override
    public ByteBuffer putChar(char x) {

        Bits.putChar(this, ix(nextPutIndex(2)), x, bigEndian);
        return this;



    }

    @Override
    public ByteBuffer putChar(int i, char x) {

        Bits.putChar(this, ix(checkIndex(i, 2)), x, bigEndian);
        return this;



    }

    @Override
    public CharBuffer asCharBuffer() {
        int size = this.remaining() >> 1;
        int off = offset + position();
        return (bigEndian
                ? (CharBuffer)(new ByteBufferAsCharBufferB(this,
                                                               -1,
                                                               0,
                                                               size,
                                                               size,
                                                               off))
                : (CharBuffer)(new ByteBufferAsCharBufferL(this,
                                                               -1,
                                                               0,
                                                               size,
                                                               size,
                                                               off)));
    }


    // short



    @Override
    public short getShort() {
        return Bits.getShort(this, ix(nextGetIndex(2)), bigEndian);
    }

    @Override
    public short getShort(int i) {
        return Bits.getShort(this, ix(checkIndex(i, 2)), bigEndian);
    }



    @Override
    public ByteBuffer putShort(short x) {

        Bits.putShort(this, ix(nextPutIndex(2)), x, bigEndian);
        return this;



    }

    @Override
    public ByteBuffer putShort(int i, short x) {

        Bits.putShort(this, ix(checkIndex(i, 2)), x, bigEndian);
        return this;



    }

    @Override
    public ShortBuffer asShortBuffer() {
        int size = this.remaining() >> 1;
        int off = offset + position();
        return (bigEndian
                ? (ShortBuffer)(new ByteBufferAsShortBufferB(this,
                                                                 -1,
                                                                 0,
                                                                 size,
                                                                 size,
                                                                 off))
                : (ShortBuffer)(new ByteBufferAsShortBufferL(this,
                                                                 -1,
                                                                 0,
                                                                 size,
                                                                 size,
                                                                 off)));
    }


    // int



    @Override
    public int getInt() {
        return Bits.getInt(this, ix(nextGetIndex(4)), bigEndian);
    }

    @Override
    public int getInt(int i) {
        return Bits.getInt(this, ix(checkIndex(i, 4)), bigEndian);
    }



    @Override
    public ByteBuffer putInt(int x) {

        Bits.putInt(this, ix(nextPutIndex(4)), x, bigEndian);
        return this;



    }

    @Override
    public ByteBuffer putInt(int i, int x) {

        Bits.putInt(this, ix(checkIndex(i, 4)), x, bigEndian);
        return this;



    }

    @Override
    public IntBuffer asIntBuffer() {
        int size = this.remaining() >> 2;
        int off = offset + position();
        return (bigEndian
                ? (IntBuffer)(new ByteBufferAsIntBufferB(this,
                                                             -1,
                                                             0,
                                                             size,
                                                             size,
                                                             off))
                : (IntBuffer)(new ByteBufferAsIntBufferL(this,
                                                             -1,
                                                             0,
                                                             size,
                                                             size,
                                                             off)));
    }


    // long



    @Override
    public long getLong() {
        return Bits.getLong(this, ix(nextGetIndex(8)), bigEndian);
    }

    @Override
    public long getLong(int i) {
        return Bits.getLong(this, ix(checkIndex(i, 8)), bigEndian);
    }



    @Override
    public ByteBuffer putLong(long x) {

        Bits.putLong(this, ix(nextPutIndex(8)), x, bigEndian);
        return this;



    }

    @Override
    public ByteBuffer putLong(int i, long x) {

        Bits.putLong(this, ix(checkIndex(i, 8)), x, bigEndian);
        return this;



    }

    @Override
    public LongBuffer asLongBuffer() {
        int size = this.remaining() >> 3;
        int off = offset + position();
        return (bigEndian
                ? (LongBuffer)(new ByteBufferAsLongBufferB(this,
                                                               -1,
                                                               0,
                                                               size,
                                                               size,
                                                               off))
                : (LongBuffer)(new ByteBufferAsLongBufferL(this,
                                                               -1,
                                                               0,
                                                               size,
                                                               size,
                                                               off)));
    }


    // float



    @Override
    public float getFloat() {
        return Bits.getFloat(this, ix(nextGetIndex(4)), bigEndian);
    }

    @Override
    public float getFloat(int i) {
        return Bits.getFloat(this, ix(checkIndex(i, 4)), bigEndian);
    }



    @Override
    public ByteBuffer putFloat(float x) {

        Bits.putFloat(this, ix(nextPutIndex(4)), x, bigEndian);
        return this;



    }

    @Override
    public ByteBuffer putFloat(int i, float x) {

        Bits.putFloat(this, ix(checkIndex(i, 4)), x, bigEndian);
        return this;



    }

    @Override
    public FloatBuffer asFloatBuffer() {
        int size = this.remaining() >> 2;
        int off = offset + position();
        return (bigEndian
                ? (FloatBuffer)(new ByteBufferAsFloatBufferB(this,
                                                                 -1,
                                                                 0,
                                                                 size,
                                                                 size,
                                                                 off))
                : (FloatBuffer)(new ByteBufferAsFloatBufferL(this,
                                                                 -1,
                                                                 0,
                                                                 size,
                                                                 size,
                                                                 off)));
    }


    // double



    @Override
    public double getDouble() {
        return Bits.getDouble(this, ix(nextGetIndex(8)), bigEndian);
    }

    @Override
    public double getDouble(int i) {
        return Bits.getDouble(this, ix(checkIndex(i, 8)), bigEndian);
    }



    @Override
    public ByteBuffer putDouble(double x) {

        Bits.putDouble(this, ix(nextPutIndex(8)), x, bigEndian);
        return this;



    }

    @Override
    public ByteBuffer putDouble(int i, double x) {

        Bits.putDouble(this, ix(checkIndex(i, 8)), x, bigEndian);
        return this;



    }

    @Override
    public DoubleBuffer asDoubleBuffer() {
        int size = this.remaining() >> 3;
        int off = offset + position();
        return (bigEndian
                ? (DoubleBuffer)(new ByteBufferAsDoubleBufferB(this,
                                                                   -1,
                                                                   0,
                                                                   size,
                                                                   size,
                                                                   off))
                : (DoubleBuffer)(new ByteBufferAsDoubleBufferL(this,
                                                                   -1,
                                                                   0,
                                                                   size,
                                                                   size,
                                                                   off)));
    }











































}
