//package com.ss.price.controller;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import javax.imageio.ImageWriteParam;
//import javax.imageio.ImageWriter;
//import javax.imageio.stream.ImageOutputStream;
//
//public class ImageCompressor {
//
//    public static void compressImage(String imagePath, int maxSizeKB) throws IOException {
//        File file = new File(imagePath);
//        BufferedImage img = ImageIO.read(file);
//
//        // Try different quality parameters until the file size is less than or equal to maxSizeKB
//        float quality = 0.95f;
//        while (true) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
//            ImageWriteParam param = writer.getDefaultWriteParam();
//            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//            param.setCompressionQuality(quality);
//
//            ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
//            writer.setOutput(ios);
//            writer.write(null, new javax.imageio.IIOImage(img, null, null), param);
//            writer.dispose();
//            ios.close();
//
//            byte[] bytes = baos.toByteArray();
//            if (bytes.length / 1024 <= maxSizeKB || quality <= 0.1f) {
//                break;
//            }
//            quality -= 0.05f;
//        }
//
//        ImageIO.write(img, "jpg", file);
//    }
//}
