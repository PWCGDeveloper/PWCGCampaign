package pwcg.core.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;


public class DDSWriter {
    private static final int DDS_MAGIC = 0x20534444;
    private static final int HEADER_SIZE = 124;
    private static final int HEADER_FLAGS_VALUE = 0x81007;
    private static final int CAPS_VALUE = 0x1000;

    private static final int PIXELFORMAT_SIZE = 32;
    private static final int PIXELFORMAT_FLAGS_VALUE = 0x4;
    private static final int DXT5_FOURCC = 0x35545844;

    private static final int SQUISH_FLAG_DXT1                   = 1 << 0;
    private static final int SQUISH_FLAG_DXT3                   = 1 << 1;
    private static final int SQUISH_FLAG_DXT5                   = 1 << 2;
    private static final int SQUISH_FLAG_BC4                    = 1 << 3;
    private static final int SQUISH_FLAG_BC5                    = 1 << 4;
    private static final int SQUISH_FLAG_CLUSTERFIT             = 1 << 5;
    private static final int SQUISH_FLAG_RANGEFIT               = 1 << 6;
    private static final int SQUISH_FLAG_WEIGHTCOLOURBYALPHA    = 1 << 7;
    private static final int SQUISH_FLAG_ITERATIVECLUSTERFIT    = 1 << 8;
    private static final int SQUISH_FLAG_BGRA                   = 1 << 9;
    private static final int SQUISH_FLAG_ABGR                   = 1 << 10;

    public static void writeImage(BufferedImage image, File file) throws PWCGIOException
    {
        try {
            DataBufferByte dataBuffer = (DataBufferByte) image.getRaster().getDataBuffer();
            byte[] compressed = new byte[GetStorageRequirements(image.getWidth(), image.getHeight(), 4)];
            int flags = SQUISH_FLAG_ABGR | SQUISH_FLAG_DXT5 | SQUISH_FLAG_RANGEFIT;
            CompressImage(dataBuffer.getData(), image.getWidth(), image.getHeight(), image.getWidth()*4, compressed, flags, (float[])null);

            FileOutputStream output = new FileOutputStream(file);

            writeInt(output, DDS_MAGIC);
            writeInt(output, HEADER_SIZE);
            writeInt(output, HEADER_FLAGS_VALUE);
            writeInt(output, image.getHeight());
            writeInt(output, image.getWidth());
            writeInt(output, GetStorageRequirements(image.getWidth(), image.getHeight(), 4));
            writeInt(output, 0);                    // Volume texture depth
            writeInt(output, 0);                    // Number of mipmap levels
            for (int i = 0; i < 11; i++)
                writeInt(output, 0);                // Reserved

            // Pixel format
            writeInt(output, PIXELFORMAT_SIZE);
            writeInt(output, PIXELFORMAT_FLAGS_VALUE);
            writeInt(output, DXT5_FOURCC);
            writeInt(output, 0);                        // RGB bit count
            writeInt(output, 0);                        // R bitmask
            writeInt(output, 0);                        // B bitmask
            writeInt(output, 0);                        // G bitmask
            writeInt(output, 0);                        // A bitmask

            writeInt(output, CAPS_VALUE);
            writeInt(output, 0);                        // Caps 2
            writeInt(output, 0);                        // Caps 3
            writeInt(output, 0);                        // Caps 4
            writeInt(output, 0);                        // Reserved

            output.write(compressed);

            output.close();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    private static native int GetStorageRequirements(int width, int height, int flags);
    private static native void CompressImage(byte[] rgba, int width, int height, int pitch, byte[] blocks, int flags, float[] metric);

    static {
        System.loadLibrary("squish" + System.getProperty("sun.arch.data.model"));
    }

    private static void writeInt(FileOutputStream output, int value) throws IOException
    {
        output.write(value & 0xff);
        output.write((value >> 8) & 0xff);
        output.write((value >> 16) & 0xff);
        output.write((value >> 24) & 0xff);
    }
}
