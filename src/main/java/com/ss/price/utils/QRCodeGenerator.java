package com.ss.price.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    public static void main(String[] args) {
        String jsonFilePath = "D:\\QRcode/file.json";
        generateQRCodeFromJsonFile(jsonFilePath);
    }

    public static void generateQRCodeFromJsonFile(String jsonFilePath) {
        try {
            // 读取JSON文件
            File file = new File(jsonFilePath);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonData[] jsonDataArray = objectMapper.readValue(file, JsonData[].class);

            // 逐个解析JSON并生成二维码
            for (JsonData jsonData : jsonDataArray) {
                String jsonString = objectMapper.writeValueAsString(jsonData);

                // 生成二维码图片
                String qrCodeImagePath = generateQRCodeImage(jsonString);

                System.out.println("Generated QR Code for JSON: " + jsonString);
                System.out.println("QR Code image path: " + qrCodeImagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateQRCodeImage(String data) {
        int width = 300;
        int height = 300;
        String format = "png";
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);
            File qrCodeFile = new File("path/to/save/qrcode.png");
            MatrixToImageWriter.writeToFile(bitMatrix, format, qrCodeFile);
            return qrCodeFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class JsonData {
        private String name;
        private int age;
        private String city;

        // getter and setter methods

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}