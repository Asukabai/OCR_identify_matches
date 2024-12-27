package com.ss.price.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGeneratorByString {

    public static void main(String[] args) {
        String filePath = "D:\\ssproject\\newItem\\qr_code_back\\qrcodes_test";
        int width = 300;
        int height = 300;

        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hintMap.put(EncodeHintType.MARGIN, 1);
            hintMap.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.L);

            // 设置背景色和前景色
            Color backgroundColor = Color.WHITE;
            Color foregroundColor = Color.BLACK;

            for (int i = 10; i <= 19; i++) {
                // 生成类似 Fboard001 的字符串
                String content = "Fboard" +"_" + String.format("%03d", i);
                String fileFullPath = filePath + i + ".png";

                BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hintMap);

                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        int color = matrix.get(x, y) ? foregroundColor.getRGB() : backgroundColor.getRGB();
                        image.setRGB(x, y, color);
                    }
                }

                File qrCodeFile = new File(fileFullPath);
                ImageIO.write(image, "png", qrCodeFile);

                System.out.println("QR Code for " + content + " generated successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}