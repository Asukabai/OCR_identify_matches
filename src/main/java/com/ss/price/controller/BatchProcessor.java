//package com.ss.price.controller;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Base64;
//import java.util.List;
//
//public class BatchProcessor {
//
//    public static void main(String[] args) {
//        String folderPath = "D:\\ssproject\\ssprice\\采购合同\\pic_1 - 副本";
//        processFiles(folderPath);
//        System.out.println("处理结束！");
//    }
//
//    public static void processFiles(String folderPath) {
//        File folder = new File(folderPath);
//        File[] files = folder.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".pdf"));
//
//        if (files != null) {
//            for (File file : files) {
//                try {
//                    if (file.getName().endsWith(".pdf")) {
//                        List<String> images = PDFConverter.convertPdfToImages(file.getAbsolutePath(), folderPath);
//                        for (String imgPath : images) {
//                            processImage(imgPath);
//                        }
//                    } else {
//                        processImage(file.getAbsolutePath());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//
//
//    public static void processImage(String imagePath) throws IOException {
//        long fileSizeKB = Files.size(Paths.get(imagePath)) / 1024;
//        if (fileSizeKB > 1024) { // 1MB
//            ImageCompressor.compressImage(imagePath, 1024);
//        }
//        BufferedImage img = ImageIO.read(new File(imagePath));
//        String base64String = encodeImageToBase64(img);
//        String ocrResult = sendOcrRequest(base64String);
//        // 通过mybatis-plus 保存ocr结果 到数据库中
////        DatabaseHandler.saveToDatabase(file.getName(), ocrResult);
//    }
//
//    public static String encodeImageToBase64(BufferedImage image) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(image, "jpg", baos);
//        byte[] imageBytes = baos.toByteArray();
//        return Base64.getEncoder().encodeToString(imageBytes);
//    }
//
//    public static String sendOcrRequest(String base64String) {
//        String url = "http://127.0.0.1:1224/api/ocr";
//        String jsonInputString = "{\"base64\": \"" + base64String + "\", \"options\": {\"data.format\": \"text\"}}";
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpPost request = new HttpPost(url);
//            request.setHeader("Content-Type", "application/json");
//            request.setEntity(new StringEntity(jsonInputString));
//
//            try (CloseableHttpResponse response = httpClient.execute(request)) {
//                HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    String result = EntityUtils.toString(entity);
//                    return parseOcrResult(result);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    public static String parseOcrResult(String result) {
//        // Parse the JSON response and extract the OCR result
//        // This is a simplified example, you should use a JSON parser like Jackson or Gson
//        return result.split("\"data\":\"")[1].split("\"")[0];
//    }
//}
