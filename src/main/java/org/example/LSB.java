package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

class LSB {
    public static void encodeFile(File target, File payload, File output) throws Exception {
        byte[] payloadBytes = Files.readAllBytes(payload.toPath());

        FileOutputStream fileOutputStream = new FileOutputStream(output);
        FileInputStream fileInputStream = new FileInputStream(target);

        // The following if statement check if the target file is large enough to fit
        // the payload using the LSB algorithm
        // 150 is 54 bytes header, 32 bytes size and 64 bytes file extension
        if ((long) payloadBytes.length * 8 > (long) fileInputStream.available() - 150)
            throw new Exception();

        byte[] targetHeader = new byte[54];
        fileInputStream.read(targetHeader);
        fileOutputStream.write(targetHeader);
        encodeBytes(fileOutputStream, fileSizeToByteArray(payloadBytes.length), fileInputStream);
        encodeBytes(fileOutputStream, fileExtensionToByteArray(getFileExtension(payload)), fileInputStream);
        encodeBytes(fileOutputStream, payloadBytes, fileInputStream);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = fileInputStream.read(buffer)) > 0)

            fileOutputStream.write(buffer, 0, length);

        fileOutputStream.close();
        fileInputStream.close();
    }

    private static void encodeBytes(FileOutputStream fileOutputStream, byte[] payload, FileInputStream fileInputStream) throws IOException {
        for (byte b : payload) {
            for (int i = 0; i < 8; i++) {
                int bit = b >>> (7 - i) & 0x1;
                int originalByte = fileInputStream.read();

                if (bit == 1)
                    fileOutputStream.write(originalByte | 0x1);
                else
                    fileOutputStream.write(originalByte & ~0x1);
            }
        }
    }

    private static byte[] fileSizeToByteArray(int fileSize) {
        byte[] newPayloadSizeBytes = new byte[4];

        for (int i = 0; i < 4; i++)
            newPayloadSizeBytes[i] = (byte) ((fileSize >>> ((3 - i) * 8)) & 0x000000ff);

        return newPayloadSizeBytes;
    }

    private static byte[] fileExtensionToByteArray(String fileExtension) {
        byte[] payloadFileExtensionBytes = fileExtension.getBytes(StandardCharsets.UTF_8);
        byte[] newPayloadFileExtensionBytes = new byte[8];

        for (int i = 7; i >= 0; i--) {
            if (7 - i <= payloadFileExtensionBytes.length - 1)
                newPayloadFileExtensionBytes[i] = payloadFileExtensionBytes[payloadFileExtensionBytes.length - 1 - (7 - i)];
            else
                break;
        }

        return newPayloadFileExtensionBytes;
    }


    private static String getFileExtension(File file) {
        String name = file.getName();

        int lastIndexOf = name.lastIndexOf(".");

        if (lastIndexOf == -1)
            return ""; // missing extension

        lastIndexOf++;

        return name.substring(lastIndexOf);
    }

//    public static void decodeFile(File target, String outputFileName) throws IOException
//    {
//        FileInputStream fileInputStream = new FileInputStream(target);
//
//        fileInputStream.skip(54);
//        byte[] sizeBytes = decodeBytes(fileInputStream, 4);
//        byte[] fileExtensionBytes = decodeBytes(fileInputStream, 8);
//
//        int size = byteArrayToInt(sizeBytes);
//        String fileExtension = new String(fileExtensionBytes, StandardCharsets.UTF_8);
//
//        File output = new File(outputFileName + "." + fileExtension.trim());
//        FileOutputStream fileOutputStream = new FileOutputStream(output);
//
//        byte[] outputBytes = decodeBytes(fileInputStream, size);
//        fileOutputStream.write(outputBytes);
//    }
//
//    private static byte[] decodeBytes(FileInputStream fileInputStream, int numberOfBytes) throws IOException
//    {
//        byte[] result = new byte[numberOfBytes];
//        int readByte;
//        int writeByte;
//
//        for (int i = 0; i < numberOfBytes; i++)
//        {
//            writeByte = 0;
//
//            for (int j = 0; j < 8; j++)
//            {
//                readByte = fileInputStream.read();
//                writeByte = writeByte + (readByte & 0x1);
//
//                if (j < 7)
//                    writeByte = writeByte << 1;
//            }
//
//            result[i] = (byte) writeByte;
//        }
//        return result;
//    }
//
//    private static int byteArrayToInt(byte[] input)
//    {
//        int result = 0;
//
//        for (int i = 0; i < input.length; i++)
//        {
//            result = result | (0x000000FF & input[i]);
//
//            if (i < input.length - 1)
//            {
//                result = result << 8;
//            }
//        }
//        return result;
//    }
}