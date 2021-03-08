package com.zoradev.websocket.server.server.nethandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

public class ByteBufWrapper extends ByteBuf {
    public static final int MAX_STRING_LENGTH = 8191;
    public final ByteBuf buf;

    public ByteBufWrapper(final ByteBuf buf) {
        this.buf = buf;
    }

    public static int lIIIIlIIllIIlIIlIIIlIIllI(final int var0) {
        return ((var0 & 0xFFFFFF80) == 0x0) ? 1 : (((var0 & 0xFFFFC000) == 0x0) ? 2 : (((var0 & 0xFFE00000) == 0x0) ? 3 : (((var0 & 0xF0000000) == 0x0) ? 4 : 5)));
    }

    public int readVarInt()
    {
        int i = 0;
        int j = 0;
        while (true)
        {
            byte b0 = this.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5)
            {
                throw new RuntimeException("VarInt too big");
            }
            if ((b0 & 128) != 128)
            {
                break;
            }
        }

        return i;
    }

    public void writeVarInt(int input) {
        while ((input & -128) != 0) {
            this.writeByte(input & 127 | 128);
            input >>>= 7;
        }
        this.writeByte(input);
    }

    public String readString(final int maxLength) throws IOException {
        final int i = this.readVarInt();
        if (i > maxLength * 4) {
            throw new IOException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
        }
        if (i < 0) {
            throw new IOException("The received encoded string buffer length is less than zero! Weird string!");
        }
        try {
            final byte[] data = this.readBytes(i).array();
            final String s = new String(data, Charset.defaultCharset());
            if (s.length() > maxLength) {
                throw new IOException("The received string length is longer than maximum allowed (" + s.length() + " > " + maxLength + ")");
            }
            return s;
        } catch (UnsupportedEncodingException e) {
            throw new IOException(e);
        }
    }

    public void writeString(final String string) throws IOException {
        try {
            final byte[] abyte = string.getBytes(Charset.defaultCharset());
            if (abyte.length > 32767) {
                throw new IOException("String too big (was " + abyte.length + " bytes encoded, max " + 32767 + ")");
            }
            this.writeVarInt(abyte.length);
            this.writeBytes(abyte);
        } catch (UnsupportedEncodingException e) {
            throw new IOException(e);
        }
    }

    @Override
    public int capacity() {
        return this.buf.capacity();
    }

    @Override
    public ByteBuf capacity(final int var1) {
        return this.buf.capacity(var1);
    }

    @Override
    public int maxCapacity() {
        return this.buf.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }

    @Override
    public ByteOrder order() {
        return this.buf.order();
    }

    @Override
    public ByteBuf order(final ByteOrder var1) {
        return this.buf.order(var1);
    }

    @Override
    public ByteBuf unwrap() {
        return this.buf.unwrap();
    }

    @Override
    public boolean isDirect() {
        return this.buf.isDirect();
    }

    @Override
    public int readerIndex() {
        return this.buf.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(final int var1) {
        return this.buf.readerIndex(var1);
    }

    @Override
    public int writerIndex() {
        return this.buf.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(final int var1) {
        return this.buf.writerIndex(var1);
    }

    @Override
    public ByteBuf setIndex(final int var1, final int var2) {
        return this.buf.setIndex(var1, var2);
    }

    @Override
    public int readableBytes() {
        return this.buf.readableBytes();
    }

    @Override
    public int writableBytes() {
        return this.buf.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return this.buf.isReadable();
    }

    @Override
    public boolean isReadable(final int var1) {
        return this.buf.isReadable(var1);
    }

    @Override
    public boolean isWritable() {
        return this.buf.isWritable();
    }

    @Override
    public boolean isWritable(final int var1) {
        return this.buf.isWritable(var1);
    }

    @Override
    public ByteBuf clear() {
        return this.buf.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return this.buf.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return this.buf.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return this.buf.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return this.buf.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return this.buf.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return this.buf.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(final int var1) {
        return this.buf.ensureWritable(var1);
    }

    @Override
    public int ensureWritable(final int var1, final boolean var2) {
        return this.buf.ensureWritable(var1, var2);
    }

    @Override
    public boolean getBoolean(final int var1) {
        return this.buf.getBoolean(var1);
    }

    @Override
    public byte getByte(final int var1) {
        return this.buf.getByte(var1);
    }

    @Override
    public short getUnsignedByte(final int var1) {
        return this.buf.getUnsignedByte(var1);
    }

    @Override
    public short getShort(final int var1) {
        return this.buf.getShort(var1);
    }


    @Override
    public int getUnsignedShort(final int var1) {
        return this.buf.getUnsignedShort(var1);
    }


    @Override
    public int getMedium(final int var1) {
        return this.buf.getMedium(var1);
    }

    @Override
    public int getUnsignedMedium(final int var1) {
        return this.buf.getUnsignedMedium(var1);
    }

    @Override
    public int getInt(final int var1) {
        return this.buf.getInt(var1);
    }

    @Override
    public long getUnsignedInt(final int var1) {
        return this.buf.getUnsignedInt(var1);
    }

    @Override
    public long getLong(final int var1) {
        return this.buf.getLong(var1);
    }

    @Override
    public char getChar(final int var1) {
        return this.buf.getChar(var1);
    }

    @Override
    public float getFloat(final int var1) {
        return this.buf.getFloat(var1);
    }

    @Override
    public double getDouble(final int var1) {
        return this.buf.getDouble(var1);
    }

    @Override
    public ByteBuf getBytes(final int var1, final ByteBuf var2) {
        return this.buf.getBytes(var1, var2);
    }

    @Override
    public ByteBuf getBytes(final int var1, final ByteBuf var2, final int var3) {
        return this.buf.getBytes(var1, var2, var3);
    }

    @Override
    public ByteBuf getBytes(final int var1, final ByteBuf var2, final int var3, final int var4) {
        return this.buf.getBytes(var1, var2, var3, var4);
    }

    @Override
    public ByteBuf getBytes(final int var1, final byte[] var2) {
        return this.buf.getBytes(var1, var2);
    }

    @Override
    public ByteBuf getBytes(final int var1, final byte[] var2, final int var3, final int var4) {
        return this.buf.getBytes(var1, var2, var3, var4);
    }

    @Override
    public ByteBuf getBytes(final int var1, final ByteBuffer var2) {
        return this.buf.getBytes(var1, var2);
    }

    @Override
    public ByteBuf getBytes(final int var1, final OutputStream var2, final int var3) throws IOException {
        return this.buf.getBytes(var1, var2, var3);
    }

    @Override
    public int getBytes(final int var1, final GatheringByteChannel var2, final int var3) throws IOException {
        return this.buf.getBytes(var1, var2, var3);
    }

    @Override
    public ByteBuf setBoolean(final int var1, final boolean var2) {
        return this.buf.setBoolean(var1, var2);
    }

    @Override
    public ByteBuf setByte(final int var1, final int var2) {
        return this.buf.setByte(var1, var2);
    }

    @Override
    public ByteBuf setShort(final int var1, final int var2) {
        return this.buf.setShort(var1, var2);
    }

    @Override
    public ByteBuf setMedium(final int var1, final int var2) {
        return this.buf.setMedium(var1, var2);
    }

    @Override
    public ByteBuf setInt(final int var1, final int var2) {
        return this.buf.setInt(var1, var2);
    }

    @Override
    public ByteBuf setLong(final int var1, final long var2) {
        return this.buf.setLong(var1, var2);
    }

    @Override
    public ByteBuf setChar(final int var1, final int var2) {
        return this.buf.setChar(var1, var2);
    }

    @Override
    public ByteBuf setFloat(final int var1, final float var2) {
        return this.buf.setFloat(var1, var2);
    }

    @Override
    public ByteBuf setDouble(final int var1, final double var2) {
        return this.buf.setDouble(var1, var2);
    }

    @Override
    public ByteBuf setBytes(final int var1, final ByteBuf var2) {
        return this.buf.setBytes(var1, var2);
    }

    @Override
    public ByteBuf setBytes(final int var1, final ByteBuf var2, final int var3) {
        return this.buf.setBytes(var1, var2, var3);
    }

    @Override
    public ByteBuf setBytes(final int var1, final ByteBuf var2, final int var3, final int var4) {
        return this.buf.setBytes(var1, var2, var3, var4);
    }

    @Override
    public ByteBuf setBytes(final int var1, final byte[] var2) {
        return this.buf.setBytes(var1, var2);
    }

    @Override
    public ByteBuf setBytes(final int var1, final byte[] var2, final int var3, final int var4) {
        return this.buf.setBytes(var1, var2, var3, var4);
    }

    @Override
    public ByteBuf setBytes(final int var1, final ByteBuffer var2) {
        return this.buf.setBytes(var1, var2);
    }

    @Override
    public int setBytes(final int var1, final InputStream var2, final int var3) throws IOException {
        return this.buf.setBytes(var1, var2, var3);
    }

    @Override
    public int setBytes(final int var1, final ScatteringByteChannel var2, final int var3) throws IOException {
        return this.buf.setBytes(var1, var2, var3);
    }


    @Override
    public ByteBuf setZero(final int var1, final int var2) {
        return this.buf.setZero(var1, var2);
    }


    @Override
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    @Override
    public byte readByte() {
        return this.buf.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return this.buf.readShort();
    }


    @Override
    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }


    @Override
    public int readMedium() {
        return this.buf.readMedium();
    }

    @Override
    public int readUnsignedMedium() {
        return this.buf.readUnsignedMedium();
    }


    @Override
    public int readInt() {
        return this.buf.readInt();
    }


    @Override
    public long readUnsignedInt() {
        return this.buf.readUnsignedInt();
    }


    @Override
    public long readLong() {
        return this.buf.readLong();
    }

    @Override
    public char readChar() {
        return this.buf.readChar();
    }

    @Override
    public float readFloat() {
        return this.buf.readFloat();
    }

    @Override
    public double readDouble() {
        return this.buf.readDouble();
    }

    @Override
    public ByteBuf readBytes(final int var1) {
        return this.buf.readBytes(var1);
    }

    @Override
    public ByteBuf readSlice(final int var1) {
        return this.buf.readSlice(var1);
    }

    @Override
    public ByteBuf readBytes(final ByteBuf var1) {
        return this.buf.readBytes(var1);
    }

    @Override
    public ByteBuf readBytes(final ByteBuf var1, final int var2) {
        return this.buf.readBytes(var1, var2);
    }

    @Override
    public ByteBuf readBytes(final ByteBuf var1, final int var2, final int var3) {
        return this.buf.readBytes(var1, var2, var3);
    }

    @Override
    public ByteBuf readBytes(final byte[] var1) {
        return this.buf.readBytes(var1);
    }

    @Override
    public ByteBuf readBytes(final byte[] var1, final int var2, final int var3) {
        return this.buf.readBytes(var1, var2, var3);
    }

    @Override
    public ByteBuf readBytes(final ByteBuffer var1) {
        return this.buf.readBytes(var1);
    }

    @Override
    public ByteBuf readBytes(final OutputStream var1, final int var2) throws IOException {
        return this.buf.readBytes(var1, var2);
    }

    @Override
    public int readBytes(final GatheringByteChannel var1, final int var2) throws IOException {
        return this.buf.readBytes(var1, var2);
    }


    @Override
    public ByteBuf skipBytes(final int var1) {
        return this.buf.skipBytes(var1);
    }

    @Override
    public ByteBuf writeBoolean(final boolean var1) {
        return this.buf.writeBoolean(var1);
    }

    @Override
    public ByteBuf writeByte(final int var1) {
        return this.buf.writeByte(var1);
    }

    @Override
    public ByteBuf writeShort(final int var1) {
        return this.buf.writeShort(var1);
    }


    @Override
    public ByteBuf writeMedium(final int var1) {
        return this.buf.writeMedium(var1);
    }


    @Override
    public ByteBuf writeInt(final int var1) {
        return this.buf.writeInt(var1);
    }

    @Override
    public ByteBuf writeLong(final long var1) {
        return this.buf.writeLong(var1);
    }


    @Override
    public ByteBuf writeChar(final int var1) {
        return this.buf.writeChar(var1);
    }

    @Override
    public ByteBuf writeFloat(final float var1) {
        return this.buf.writeFloat(var1);
    }

    @Override
    public ByteBuf writeDouble(final double var1) {
        return this.buf.writeDouble(var1);
    }

    @Override
    public ByteBuf writeBytes(final ByteBuf var1) {
        return this.buf.writeBytes(var1);
    }

    @Override
    public ByteBuf writeBytes(final ByteBuf var1, final int var2) {
        return this.buf.writeBytes(var1, var2);
    }

    @Override
    public ByteBuf writeBytes(final ByteBuf var1, final int var2, final int var3) {
        return this.buf.writeBytes(var1, var2, var3);
    }

    @Override
    public ByteBuf writeBytes(final byte[] var1) {
        return this.buf.writeBytes(var1);
    }

    @Override
    public ByteBuf writeBytes(final byte[] var1, final int var2, final int var3) {
        return this.buf.writeBytes(var1, var2, var3);
    }

    @Override
    public ByteBuf writeBytes(final ByteBuffer var1) {
        return this.buf.writeBytes(var1);
    }

    @Override
    public int writeBytes(final InputStream var1, final int var2) throws IOException {
        return this.buf.writeBytes(var1, var2);
    }

    @Override
    public int writeBytes(final ScatteringByteChannel var1, final int var2) throws IOException {
        return this.buf.writeBytes(var1, var2);
    }

    @Override
    public ByteBuf writeZero(final int var1) {
        return this.buf.writeZero(var1);
    }

    @Override
    public int indexOf(final int var1, final int var2, final byte var3) {
        return this.buf.indexOf(var1, var2, var3);
    }

    @Override
    public int bytesBefore(final byte var1) {
        return this.buf.bytesBefore(var1);
    }

    @Override
    public int bytesBefore(final int var1, final byte var2) {
        return this.buf.bytesBefore(var1, var2);
    }

    @Override
    public int bytesBefore(final int var1, final int var2, final byte var3) {
        return this.buf.bytesBefore(var1, var2, var3);
    }

    @Override
    public int forEachByte(ByteBufProcessor byteBufProcessor) {
        return 0;
    }

    @Override
    public int forEachByte(int i, int i1, ByteBufProcessor byteBufProcessor) {
        return 0;
    }

    @Override
    public int forEachByteDesc(ByteBufProcessor byteBufProcessor) {
        return 0;
    }

    @Override
    public int forEachByteDesc(int i, int i1, ByteBufProcessor byteBufProcessor) {
        return 0;
    }



    @Override
    public ByteBuf copy() {
        return this.buf.copy();
    }

    @Override
    public ByteBuf copy(final int var1, final int var2) {
        return this.buf.copy(var1, var2);
    }

    @Override
    public ByteBuf slice() {
        return this.buf.slice();
    }


    @Override
    public ByteBuf slice(final int var1, final int var2) {
        return this.buf.slice(var1, var2);
    }


    @Override
    public ByteBuf duplicate() {
        return this.buf.duplicate();
    }


    @Override
    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(final int var1, final int var2) {
        return this.buf.nioBuffer(var1, var2);
    }

    @Override
    public ByteBuffer internalNioBuffer(final int var1, final int var2) {
        return this.buf.internalNioBuffer(var1, var2);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return this.buf.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(final int var1, final int var2) {
        return this.buf.nioBuffers(var1, var2);
    }

    @Override
    public boolean hasArray() {
        return this.buf.hasArray();
    }

    @Override
    public byte[] array() {
        return this.buf.array();
    }

    @Override
    public int arrayOffset() {
        return this.buf.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return this.buf.memoryAddress();
    }

    @Override
    public String toString(final Charset var1) {
        return this.buf.toString(var1);
    }

    @Override
    public String toString(final int var1, final int var2, final Charset var3) {
        return this.buf.toString(var1, var2, var3);
    }

    @Override
    public int hashCode() {
        return this.buf.hashCode();
    }

    @Override
    public boolean equals(final Object var1) {
        return this.buf.equals(var1);
    }

    @Override
    public int compareTo(final ByteBuf var1) {
        return this.buf.compareTo(var1);
    }

    @Override
    public String toString() {
        return this.buf.toString();
    }

    @Override
    public ByteBuf retain(final int var1) {
        return this.buf.retain(var1);
    }

    @Override
    public ByteBuf retain() {
        return this.buf.retain();
    }

    @Override
    public int refCnt() {
        return this.buf.refCnt();
    }

    @Override
    public boolean release() {
        return this.buf.release();
    }

    @Override
    public boolean release(final int var1) {
        return this.buf.release(var1);
    }
}
