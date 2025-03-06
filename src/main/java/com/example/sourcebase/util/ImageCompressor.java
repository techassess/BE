package com.example.sourcebase.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class ImageCompressor {

    // Maximum file size (2MB in bytes)
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    /**
     * Compresses an image to be under 2MB while maintaining aspect ratio
     *
     * @param imageFile the MultipartFile to compress
     * @return byte array of the compressed image
     * @throws IOException if image processing fails
     */
    public static byte[] compressImage(MultipartFile imageFile) throws IOException {
        // Check if compression is needed
        if (imageFile.getSize() <= MAX_FILE_SIZE) {
            return imageFile.getBytes();
        }

        // Get image format
        String fileName = imageFile.getOriginalFilename();
        String formatName = getImageFormat(fileName);

        // Read the image
        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());

        // First try: quality compression
        byte[] compressedImageData = compressImageQuality(originalImage, formatName);

        // Second try: resize if quality compression is not enough
        if (compressedImageData.length > MAX_FILE_SIZE) {
            compressedImageData = resizeAndCompressImage(originalImage, formatName);
        }

        return compressedImageData;
    }

    private static byte[] compressImageQuality(BufferedImage image, String formatName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
        if (!writers.hasNext()) {
            // If no writer found, default to PNG
            formatName = "png";
            writers = ImageIO.getImageWritersByFormatName(formatName);
        }

        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

            // Start with 0.7 quality
            float quality = 0.7f;
            param.setCompressionQuality(quality);

            try (ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(outputStream)) {
                writer.setOutput(imageOutputStream);
                writer.write(null, new IIOImage(image, null, null), param);
            } finally {
                writer.dispose();
            }

            // If still too large, compress more aggressively
            if (outputStream.size() > MAX_FILE_SIZE) {
                outputStream.reset();
                quality = 0.4f;
                param.setCompressionQuality(quality);

                try (ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(outputStream)) {
                    writer.setOutput(imageOutputStream);
                    writer.write(null, new IIOImage(image, null, null), param);
                } finally {
                    writer.dispose();
                }
            }
        } else {
            // If compression is not supported, just write the image
            ImageIO.write(image, formatName, outputStream);
        }

        return outputStream.toByteArray();
    }

    private static byte[] resizeAndCompressImage(BufferedImage originalImage, String formatName) throws IOException {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        float scale = 0.8f;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] imageData;

        do {
            // Calculate new dimensions
            int newWidth = Math.round(width * scale);
            int newHeight = Math.round(height * scale);

            // Resize the image
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
            Graphics2D g = resizedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            // Compress the resized image
            outputStream.reset();
            ImageIO.write(resizedImage, formatName, outputStream);

            imageData = outputStream.toByteArray();

            // Reduce scale for next iteration if needed
            scale *= 0.8f;
        } while (imageData.length > MAX_FILE_SIZE && scale > 0.1f);

        return imageData;
    }

    private static String getImageFormat(String fileName) {
        if (fileName == null) return "jpeg";

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            String extension = fileName.substring(lastDotIndex + 1).toLowerCase();
            if (extension.equals("jpg")) {
                return "jpeg";
            }
            return extension;
        }

        return "jpeg";
    }

    /**
     * Creates a MultipartFile from compressed image data
     *
     * @param compressedImage byte array of compressed image
     * @param originalFile the original MultipartFile
     * @return a MultipartFile containing the compressed image
     */
    public static MultipartFile createCompressedMultipartFile(byte[] compressedImage, MultipartFile originalFile) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return originalFile.getName();
            }

            @Override
            public String getOriginalFilename() {
                return originalFile.getOriginalFilename();
            }

            @Override
            public String getContentType() {
                return originalFile.getContentType();
            }

            @Override
            public boolean isEmpty() {
                return compressedImage.length == 0;
            }

            @Override
            public long getSize() {
                return compressedImage.length;
            }

            @Override
            public byte[] getBytes() {
                return compressedImage;
            }

            @Override
            public java.io.InputStream getInputStream() {
                return new ByteArrayInputStream(compressedImage);
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(dest)) {
                    fos.write(compressedImage);
                }
            }
        };
    }
}