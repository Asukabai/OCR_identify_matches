package com.ss.price.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ss.price.config.FileProperties;
import com.ss.price.entity.*;
import com.ss.price.entity.dto.RespondDto;
import com.ss.price.mapper.*;
import com.ss.price.utils.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

/**
 * @author zhangxuejin
 * @version 1.0.0 2024-12-4
 */

@CrossOrigin
@RestController
@RequestMapping("/ss/service")
@Slf4j
public class PriceFileController {
    private static final AtomicInteger counter = new AtomicInteger(0);
    @Resource
    private FileProperties fileProperties;
    @Resource
    FileImageMapper fileImageMapper;
    @Resource
    FileInfoMapper fileInfoMapper;
    @Resource
    UploadLogMapper uploadLogMapper;

    // 增加配置类文件对应的Mapper
    @Resource
    ConfigTypesMapper configTypesMapper;
    @Resource
    ConfigItemsMapper configItemsMapper;
    @Resource
    ProductInfoMapper productInfoMapper;

    // 在 getUploadLog 方法中进行修改
    @RequestMapping(value = "/getUploadLog", method = {RequestMethod.GET, RequestMethod.POST})
    public RespondDto getUploadLog() {
        // 查询所有的上传日志记录
        List<UploadLog> uploadLogs = uploadLogMapper.selectList(null);
        // 将查询到的数据封装成前端需要的格式
        List<Map<String, Object>> activities = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (UploadLog log : uploadLogs) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("content", log.getUploadContent());
            // 格式化时间戳
            String formattedTime = dateFormat.format(log.getCreateTime());
            activity.put("timestamp", formattedTime);
            activities.add(activity);
        }
        // 返回封装好的数据
        return RespondUtils.success(activities, "查询成功 ！");
    }



    /**
     * 2.0 功能： 上传并解析文件（支持一次性单个，多个文件 或 整个文件夹上传）
     * URL：   /ss/service/uploadFileOperation
     * 逻辑步骤：
     1    验证文件类型：首先遍历所有文件，确保每个文件都是允许的类型（ PDF 或 图片 ）。
     2    检查文件是否存在：检查文件目录下是否有同名文件，如果有则跳过。
     3    处理 PDF 文件：将 PDF 文件转换为图片。
     4    压缩图片：如果图片大小超过 1MB，则进行压缩。
     5    转换为 Base64：将图片转换为 Base64 字符串。
     6    调用 baidu-OCR 接口：将 Base64 字符串发送到 OCR 接口进行解析。(分别调用两种 SDK,文本类型和表格类型，抛弃掉原来的 UMI-OCR 接口)
     7    存储解析结果：将解析结果存储到数据库中，并处理解析失败的情况。
     8    存储 Base64 字符串/图片文件到对应的文件夹中。
     9   记录文件信息：将文件信息存储到数据库中，并返回给前端。
     */
    @RequestMapping(value = "/uploadFileOperation", method = {RequestMethod.GET, RequestMethod.POST})
    public RespondDto uploadFile(@NonNull @RequestParam("multipartFiles") MultipartFile[] multipartFiles) {
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        // Step 1: Validate file types 校验文件类型
        for (MultipartFile file : multipartFiles) {
            String originalFileName = file.getOriginalFilename();
            System.out.println("文件名：" + originalFileName);
            assert originalFileName != null;
            if (!(originalFileName.endsWith(".pdf") || originalFileName.endsWith(".PDF") || originalFileName.endsWith(".png") ||
                    originalFileName.endsWith(".jpg") || originalFileName.endsWith(".jpeg"))) {
                log.error(originalFileName + " 不是 pdf 或图片类型文件!");
                return RespondUtils.error("仅支持 .pdf 或图片类型文件!");
            }}
        // Step 2: Check if files already exist 遍历文件列表，判断文件列表中是否重复或者要上传的文件已存在于目标文件夹中
        File dir = new File(String.valueOf(fileProperties.getSavePath()));
        if (!dir.exists()) {
            dir.mkdirs();}
        List<String> fileUrls = new ArrayList<>();
        List<String> skippedFiles = new ArrayList<>();
        List<String> failedFiles = new ArrayList<>();
        // 获取 upload 目录下的所有子文件夹名称
        File[] subDirs = dir.listFiles(File::isDirectory);
        List<String> subDirNames = new ArrayList<>();
        if (subDirs != null) {
            for (File subDir : subDirs) {
                subDirNames.add(subDir.getName().toLowerCase());
            }}
        // 获取 upload 目录下的所有图片文件名称（不包括扩展名）
        File[] imageFiles = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg"));
        List<String> imageFileNames = new ArrayList<>();
        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                imageFileNames.add(FilenameUtils.getBaseName(imageFile.getName()).toLowerCase());
            }}
        for (MultipartFile file : multipartFiles) {
            String originalFileName = file.getOriginalFilename();
            assert originalFileName != null;
            // 获取文件的基本名称（不包括扩展名）
            String baseName = FilenameUtils.getBaseName(originalFileName).toLowerCase();
            boolean fileExists = false;
            if (originalFileName.toLowerCase().endsWith(".pdf")) {
                // 检查是否存在同名的子文件夹
                if (subDirNames.contains(baseName)) {
                    log.warn("文件夹 " + baseName + " 已存在于目标文件夹中，跳过解析上传步骤 。");
                    skippedFiles.add(originalFileName);
                    fileExists = true;}
            } else {
                // 检查是否存在同名的图片文件
                if (imageFileNames.contains(baseName)) {
                    log.warn("文件 " + baseName + " 已存在于目标文件夹中，跳过解析上传步骤 。");
                    skippedFiles.add(originalFileName);
                    fileExists = true;
                }}
            if (fileExists) {continue;}
            try {
                if (originalFileName.toLowerCase().endsWith(".pdf")) {
                    // 处理各种类型的PDF类型文件
                    processPdfFile(file, dir, fileUrls, failedFiles);
                } else {
                    // 处理各种类型的图片类型文件
                    processImageFile(file, dir, fileUrls, failedFiles);}
            } catch (Exception e) {
                e.printStackTrace();
                failedFiles.add(originalFileName);
            }}
        Set<String> uniqueFileNames = new HashSet<>();
        for (String url : fileUrls) {
            String fileName = new File(url).getName();
//            System.out.println("文件名：" + fileName);
            String baseName = FilenameUtils.getBaseName(fileName);
//            System.out.println("文件名（不含扩展名）：" + baseName);
            if (baseName.contains("_")) {
                // 只保留 '_' 前的部分
                String uniqueName = baseName.substring(0, baseName.indexOf("_"));
//                System.out.println("处理后的文件名（不含扩展名且去掉页码）：" + uniqueName);
                uniqueFileNames.add(uniqueName);
            } else {
                // 直接加入集合
//                System.out.println("处理后的文件名（不含扩展名）：" + baseName);
                uniqueFileNames.add(baseName);
            }}
//        System.out.println("最终统计的唯一文件名集合：" + uniqueFileNames.toString());
        int successfulUploads = uniqueFileNames.size();
        int totalFilesReceived = multipartFiles.length;
        StringBuilder skippedFilesMessage = new StringBuilder();
        if (!skippedFiles.isEmpty()) {
            skippedFilesMessage.append("; 【跳过的文件】 (").append(skippedFiles.size()).append(" 个 ): ").append(String.join(", ", skippedFiles));
        }
        StringBuilder failedFilesMessage = new StringBuilder();
        if (!failedFiles.isEmpty()) {
            failedFilesMessage.append("; 【解析失败的文件】 (").append(failedFiles.size()).append(" 个 ): ").append(String.join(", ", failedFiles));
        }
        // 记录结束时间并计算耗时
        long endTime = System.currentTimeMillis();
        long durationInMillis = endTime - startTime;
        long durationInSeconds = durationInMillis / 1000;
        // Save log to database  :
        // 总共接收的文件数量
        //成功上传的文件数量
        //成功上传的文件名称列表
        //跳过的文件信息（如果有）
        //解析失败的文件信息（如果有）
        UploadLog uploadLog = new UploadLog();
        // Convert uniqueFileNames to a comma-separated string
        String uploadedFileNames = String.join(", ", uniqueFileNames);
        uploadLog.setCreateTime(new Date());
        uploadLog.setUploadContent("【 "+totalFilesReceived + " 个文件接收成功！】" +"【 "+
                successfulUploads + " 个文件上传成功！】" +
                "【上传成功的文件】: " + "【 "+ uploadedFileNames +" 】"+
                (skippedFilesMessage.length() > 0 && failedFilesMessage.length() > 0 ? " " : "") +
                skippedFilesMessage.toString() +
                (failedFilesMessage.length() > 0 ? " " : "") +
                failedFilesMessage.toString()+
                " ; 【接口耗时】: " + durationInSeconds + " s");
        uploadLogMapper.insert(uploadLog);
        return RespondUtils.success("上传完成 ！");
    }



    /**
     * 功能： 处理 PDF 文件 （解析成功之后才进行文件上传）
     * 逻辑步骤：
     1    获取上传文件的原始文件名。
     2    使用 Apache PDFBox 库加载 PDF 文件。获取 PDF 文件的总页数。
     3    处理 PDF 文件：将 PDF 文件转换为图片。
     4    压缩图片：如果图片大小超过 1MB，则进行压缩。
     5    转换为 Base64：将图片转换为 Base64 字符串。
     6    调用 OCR 接口：将 Base64 字符串发送到 OCR 接口进行解析。
     7    存储解析结果：将解析结果存储到数据库中，并处理解析失败的情况。
     8    存储 Base64 字符串：将 Base64 字符串存储到对应的文件夹中。(弃用)  存储 图片文件本身：将 Base64 字符串存储到对应的文件夹中。(弃用)
     */
    private void processPdfFile(MultipartFile file, File dir, List<String> fileUrls, List<String> failedFiles) throws IOException {
        // 设置系统属性以优化 PDF 渲染
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
        System.out.println("进入到 processPdfFile 方法");
        String originalFileName = file.getOriginalFilename(); // 获取上传文件的原始文件名。
        System.out.println("Processing PDF file: " + originalFileName);
        String baseName = FilenameUtils.getBaseName(originalFileName); // 获取文件的基本名称（不包括扩展名）。
        System.out.println("Base name: " + baseName);
        PDDocument document = PDDocument.load(file.getInputStream()); // PDDocument：使用 Apache PDFBox 库加载 PDF 文件。
        PDFRenderer pdfRenderer = new PDFRenderer(document); // PDFRenderer：创建一个 PDF 渲染器对象，用于将 PDF 页面渲染为图像。
        List<String> ocrResults = new ArrayList<>(); // 用于存储每一页的 OCR 结果
        for (int page = 0; page < document.getNumberOfPages(); ++page) { // 获取 PDF 文件的总页数。
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300); // 将当前页渲染为 DPI 为 300 的 BufferedImage 对象。
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bim, "jpg", baos); // 将 BufferedImage 对象写入 ByteArrayOutputStream，并以 PNG 格式保存。
            byte[] imageBytes = baos.toByteArray(); // 将输出流转换为字节数组。
            // 检查图像的大小是否超过 1MB，如果是，则调用 compressImage 方法进行压缩。得到 compressImageBytes
            if (imageBytes.length > 1024 * 1024) {
                imageBytes = compressImage(imageBytes,baseName + "_" + (page + 1) + ".jpg");
            }
            String imageFileName = baseName + "_" + (page + 1) + ".jpg"; // imageFileName：生成图像文件名，格式为 基本名称_页码.png。
            File imageFile = new File(dir.getAbsolutePath() + File.separator + baseName + File.separator + imageFileName); // 创建一个 File 对象，表示要保存的图像文件。
            imageFile.getParentFile().mkdirs(); // 确保目标目录存在，如果不存在则创建。
            // 调用 umi-OCR 并获取结果
            String ocrResult = sendToOcrAndStoreResult(imageBytes, imageFileName);
            // 调用 baidu-OCR 获取PDF中表格的识别结果，并解析相关的字段将结果存储到数据库中，如果识别失败，则将失败的文件名添加到 failedFiles 列表中。
            baiDuSDKTable(imageBytes, imageFileName);
            // 如果 OCR 成功，则将图像数据写入文件
            if (!ocrResult.isEmpty()) {
                try (FileOutputStream fos = new FileOutputStream(imageFile)) { // FileOutputStream：将图像数据写入文件。
                    fos.write(imageBytes);
                }
                String fileUrl = imageFile.getAbsolutePath(); // 获取图像文件的绝对路径。
                fileUrls.add(fileUrl); // 将文件路径添加到 fileUrls 列表中。
                ocrResults.add(ocrResult); // 将 OCR 结果添加到列表中
            } else {
                failedFiles.add(imageFileName);
            }
        }
        // 合并所有 OCR 结果
        StringBuilder mergedOcrResult = new StringBuilder();
        for (String result : ocrResults) {
            if (mergedOcrResult.length() > 0) {
                mergedOcrResult.append("\n"); // 添加换行符分隔不同的页面内容
            }
            mergedOcrResult.append(result);
        }
        // 存储合并后的 OCR 结果到数据库中
        if (mergedOcrResult.length() > 0) {
            // 假设有一个方法 storeOcrResult 存储 OCR 结果到数据库
            storeOcrResult(originalFileName, mergedOcrResult.toString(),fileUrls);
        } else {
            failedFiles.add(originalFileName);
        }
        document.close(); // 关闭 PDDocument 对象，释放资源
    }
    private String baiDuSDKTable(byte[] imageBytes, String fileName) {
        // 调用百度表格OCR_请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/table";
        try {
            String imgStr = Base64Util.encode(imageBytes);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = Sample.getAccessToken();
            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            // 判断识别的识别结果
            if (result.contains("error_code")) {
                System.out.println("百度表格OCR_请求失败");
                return null;
            } else {
                System.out.println("百度表格OCR_请求成功");
                // 如果请求成功，则解析图片中的表格数据
                parseBaiDuOcrResult(result);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析百度表格OCR的识别结果
     *
     * 注入 ProductInfoMapper：在 PriceFileController 类中注入 ProductInfoMapper。
     * 创建 ProductInfo 对象：在解析表格内容后，创建 ProductInfo 对象并设置相应的属性。
     * 保存到数据库：使用 productInfoMapper.insert(productInfo) 将 ProductInfo 对象插入数据库。
     *
     * @param result
     */
    private void parseBaiDuOcrResult(String result) {
        // 从数据库中读取配置
        List<ConfigTypes> configTypes = configTypesMapper.selectList(null);
        Map<String, List<ConfigItems>> configItemsMap = new HashMap<>();
        for (ConfigTypes configType : configTypes) {
            List<ConfigItems> items = configItemsMapper.selectByMap(Collections.singletonMap("config_type_id", configType.getId()));
            configItemsMap.put(configType.getTypeName(), items);
        }
        // 初始化 nameAliases 和 specAliases
        // nameAliases = [名称, 产品名称, 货物名称, 品名, 产品, 存货名称, 设备及产品名称、型号、规格, 设备或产品名称, 品牌, 商品名称, 货品名称, 品名Items]
        // specAliases = [型号, 材质, 存货规格, 规格型号, 规格参数, 产品型号, 编码/规格型号, 产品规格（mm）, 产品规格(mm), 型号及规格, 规格及型号, 规格或图号 mm, 规格或图号mm, 规格Spec, 型号/封装]
        List<String> nameAliases = configItemsMap.getOrDefault("产品名称", Collections.emptyList()).stream().map(ConfigItems::getAlias).collect(Collectors.toList());
//        System.out.println("nameAliases = " + nameAliases);
        List<String> specAliases = configItemsMap.getOrDefault("型号", Collections.emptyList()).stream().map(ConfigItems::getAlias).collect(Collectors.toList());
//        System.out.println("specAliases = " + specAliases);
        List<String> priceAliases = configItemsMap.getOrDefault("单价", Collections.emptyList()).stream().map(ConfigItems::getAlias).collect(Collectors.toList());
        // 使用 Gson 解析 JSON 字符串
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
        JsonArray tablesResult = jsonObject.getAsJsonArray("tables_result");
        JsonObject firstTable = tablesResult.get(0).getAsJsonObject();
        JsonArray body = firstTable.getAsJsonArray("body");
        // 获取表头信息
        JsonArray headerCells = new JsonArray();
        int firstRowColumnCount = 0;
        for (int i = 0; i < body.size(); i++) {
            JsonObject row = body.get(i).getAsJsonObject();
            int rowStart = row.get("row_start").getAsInt();
            if (rowStart == 0) {
                firstRowColumnCount++;
                headerCells.add(row); // 将表头行添加到 headerCells 中
            } else {
                break; // 假设表头只在第一行，遇到非表头行则停止计数
            }
        }
        System.out.println("表头列数: " + firstRowColumnCount);
        // 创建一个 Map 来存储列名和列索引的映射
        Map<String, Integer> headerIndexMap = new HashMap<>();
        // 遍历表头，找到“名称”和“规格参数”这两列的列索引
        for (int i = 0; i < headerCells.size(); i++) {
            JsonObject cell = headerCells.get(i).getAsJsonObject();
            String words = cell.get("words").getAsString();
            headerIndexMap.put(words, i);
        }
        // 输出表头信息
        System.out.println("表头信息: " + headerCells);

        // 获取“名称”和“规格参数”这两列的列索引
        int nameColumnIndex = -1;
        for (String alias : nameAliases) {
            System.out.println("名称列表中都含有 : " + alias);
            if (headerIndexMap.containsKey(alias)) {
                nameColumnIndex = headerIndexMap.get(alias);
                break;
            }
        }
        if (nameColumnIndex == -1) {
            throw new IllegalArgumentException("没有找到匹配的名称列");
        }
        int specColumnIndex = -1;
        for (String alias : specAliases) {
            System.out.println("型号列表中都含有 : " + alias);
            if (headerIndexMap.containsKey(alias)) {
                specColumnIndex = headerIndexMap.get(alias);
                break;
            }
        }
        if (specColumnIndex == -1) {
            throw new IllegalArgumentException("没有找到匹配的规格参数列");
        }

        int priceColumnIndex = -1;

        for (String alias : priceAliases) {
            String cleanedAlias = cleanString(alias);
            for (Map.Entry<String, Integer> entry : headerIndexMap.entrySet()) {
                String cleanedHeader = cleanString(entry.getKey());
                if (cleanedHeader.equals(cleanedAlias)) {
                    priceColumnIndex = entry.getValue();
                    break;
                }
            }
        }
        if (priceColumnIndex == -1) {
            throw new IllegalArgumentException("没有找到匹配的价格参数列");
        }
        // 输出表头信息
        System.out.println("**********************************************************************");
        System.out.println("名称列索引: " + nameColumnIndex);
        System.out.println("规格参数列索引: " + specColumnIndex);
        System.out.println("价格参数列索引: " + priceColumnIndex);
        // 创建一个 Map 来存储结果
        Map<Integer, Map<String, String>> resultMap = new HashMap<>();
        // 遍历 body 中的每一行，从第二行开始
        for (int i = 0; i < body.size(); i++) {
            JsonObject row = body.get(i).getAsJsonObject();
            int rowStart = row.get("row_start").getAsInt();
            if (rowStart == 0) {
                continue;
            }
            int colStart = row.get("col_start").getAsInt();
            String words = row.get("words").getAsString();
            // 将当前行的单元格内容放入 resultMap 中
            resultMap.putIfAbsent(rowStart, new HashMap<>());
            resultMap.get(rowStart).putIfAbsent(String.valueOf(colStart), words);
        }
        // 打印 resultMap 以验证结果
        for (Map.Entry<Integer, Map<String, String>> entry : resultMap.entrySet()) {
            System.out.println("Row " + entry.getKey() + ": " + entry.getValue());
        }
        // 根据列索引获取“名称”和“规格参数”这两列的内容
        for (Map.Entry<Integer, Map<String, String>> entry : resultMap.entrySet()) {
            Integer rowIndex = entry.getKey();
            Map<String, String> rowContent = entry.getValue();
            String name = rowContent.getOrDefault(String.valueOf(nameColumnIndex), "");
            String spec = rowContent.getOrDefault(String.valueOf(specColumnIndex), "");
            String price = rowContent.getOrDefault(String.valueOf(priceColumnIndex), "");
            if (name.isEmpty() || spec.isEmpty() || price.isEmpty()) {
                continue;
            }
            System.out.println("Row " + rowIndex + ": 名称=" + name + ", 规格参数=" + spec + ", 价格参数=" + price);
            if (!name.isEmpty() && !spec.isEmpty() && !price.isEmpty()) {
                // 创建 ProductInfo 对象并保存到数据库
                ProductInfo productInfo = new ProductInfo();
                productInfo.setProductName(name);
                productInfo.setModel(spec);
                productInfo.setUnitPrice(price);
                // 其他字段可以根据需要设置
                productInfoMapper.insert(productInfo);
                System.out.println("存储成功");
            }
        }
    }

    // 定义一个方法来清理字符串
    private String cleanString(String input) {
        if (input == null) {
            return "";
        }
        // 使用正则表达式去除换行符、转义字符以及中英文形式的括号
        return input.replaceAll("[\\n\\r\\\\（）()]", "").trim();
    }


    //     假设有一个方法 storeOcrResult 存储 OCR 结果到数据库
    @Transactional
    public void storeOcrResult(String fileName, String ocrResult, List<String> fileUrls) {
        try {
            log.info("Storing OCR result for file: {}", fileName);
            // 生成唯一的六位数 imageId
            Long imageId = generateUniqueSixDigitId();
            // 创建 FileInfo 对象
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(fileName);
            fileInfo.setContent(ocrResult);
            fileInfo.setImageId(imageId);
            fileInfo.setCreateTime(new Date());
            fileInfo.setUpdateTime(new Date());
            // 插入 FileInfo 记录并获取自增主键
            fileInfoMapper.insert(fileInfo);
            // 检查自增主键是否正确设置
            if (fileInfo.getId() == null) {
                throw new RuntimeException("Failed to insert FileInfo record, ID is null.");
            }
            log.info("Inserted FileInfo with ID: " + fileInfo.getId());

            // 过滤出属于当前 fileName 的文件路径
            List<String> filteredFileUrls = new ArrayList<>();
            for (String fileUrl : fileUrls) {
                String urlFileName = new File(fileUrl).getName();
                if (urlFileName.startsWith(FilenameUtils.getBaseName(fileName))) {
                    filteredFileUrls.add(fileUrl);
                }
            }
            // 创建 FileImage 对象列表并逐个插入
            for (String fileUrl : filteredFileUrls) {
                FileImage fileImage = new FileImage();
                fileImage.setImageId(imageId);
                fileImage.setImageUrl(fileUrl);
                fileImage.setCreateTime(new Date());
                fileImage.setUpdateTime(new Date());
                log.info("Inserting FileImage with imageId: " + fileImage.getImageId() + " and imageUrl: " + fileImage.getImageUrl());
                // 插入 FileImage 记录
                fileImageMapper.insert(fileImage);
            }
            // 如果所有图片插入成功，不需要额外操作
        } catch (Exception e) {
            log.error("Error storing OCR result for file: " + fileName + ", Exception: " + e.getMessage(), e);
            // 查询 file_info 表中是否还有相关记录
            QueryWrapper<FileInfo> fileInfoQueryWrapper = new QueryWrapper<>();
            fileInfoQueryWrapper.eq("file_name", fileName);
            FileInfo fileInfo = fileInfoMapper.selectOne(fileInfoQueryWrapper);
            if (fileInfo != null) {
                // 如果查询到 FileInfo 记录，继续查询 file_image 表
                QueryWrapper<FileImage> fileImageQueryWrapper = new QueryWrapper<>();
                fileImageQueryWrapper.eq("image_id", fileInfo.getImageId());
                List<FileImage> fileImages = fileImageMapper.selectList(fileImageQueryWrapper);
                // 删除 file_image 表中的记录
                if (!fileImages.isEmpty()) {
                    fileImageMapper.delete(fileImageQueryWrapper);
                }
                // 删除 file_info 表中的记录
                fileInfoMapper.delete(fileInfoQueryWrapper);
            }
            // 删除与该文件相关的上传文件夹
            deleteUploadFolder(fileName);
            throw new RuntimeException("Failed to store OCR result for file: " + fileName, e);
        }
    }

    private void deleteUploadFolder(String fileName) {
        // 构建文件夹路径
        File uploadDir = new File(fileProperties.getSavePath(), fileName);
        if (uploadDir.exists() && uploadDir.isDirectory()) {
            // 删除文件夹及其内容
            deleteDirectory(uploadDir);
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    private Long generateUniqueSixDigitId() {
        long currentTimeMillis = System.currentTimeMillis();
        int randomValue = counter.getAndIncrement() % 1000; // 生成一个三位数的随机值
        long base = (currentTimeMillis / 1000) % 1000; // 获取秒级时间戳的最后三位数
        return base * 1000L + randomValue; // 组合成一个六位数
    }

    private String sendToOcrAndStoreResult(byte[] imageBytes, String fileName) {
        System.out.println(fileName + " 进入到 sendToOcrAndStoreResult 方法");
        String base64String = Base64.getEncoder().encodeToString(imageBytes); // base64String不包含  data:image/png;base64, 等前缀。
        // 解析成TXT文本 "multi_para","多栏-按自然段换行"
        String data = "{\"base64\": \"" + base64String + "\", \"options\": {\"data.format\": \"text\"}}";
        // Implement HTTP POST request to OCR API
        try {
            // Send POST request and get response
            String response = sendPostRequest(data);
//            System.out.println("Response 解析的内容如下 : " + response);
            // Assuming a successful response means OCR was successful
            return response; // 直接返回响应字符串
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to process file: " + fileName);
            return "";
        }
    }

    private void processImageFile(MultipartFile file, File dir, List<String> fileUrls, List<String> failedFiles) throws IOException {

        String originalFileName = file.getOriginalFilename();
        String baseName = FilenameUtils.getBaseName(originalFileName);
        String newFileName = baseName + ".jpg"; // 将文件名改为 .jpg 后缀
        byte[] fileBytes = file.getBytes();

        // 压缩图片如果需要
        if (fileBytes.length > 1024 * 1024) {
            fileBytes = compressImage(fileBytes, newFileName);
        }

        // 调用 OCR 并获取结果
        String ocrResult = sendToOcrAndStoreResult(fileBytes, newFileName);
        System.out.println("OCR Image 文件，识别内容如下 : " + ocrResult);

//        File fileToSave = new File(subDir, newFileName); // 保存到子文件夹中
//        try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
//            fos.write(fileBytes);}
//        String fileUrl = fileToSave.getAbsolutePath();

        // 如果 OCR 成功，则保存图片到 目标目录
        if (!ocrResult.isEmpty()) {
            File fileToSave = new File(dir.getAbsolutePath() + File.separator + newFileName);
            try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                fos.write(fileBytes);
            }
            String fileUrl = fileToSave.getAbsolutePath();
            Long imageId = generateUniqueSixDigitId();
            // 创建 FileInfo 对象
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(originalFileName);
            fileInfo.setContent(ocrResult);
            fileInfo.setImageId(imageId);
            fileInfo.setCreateTime(new Date());
            fileInfo.setUpdateTime(new Date());
            // 插入 FileInfo 记录并获取自增主键
            fileInfoMapper.insert(fileInfo);
            // 创建 FileImage 对象
            FileImage fileImage = new FileImage();
            fileImage.setImageId(imageId);
            fileImage.setImageUrl(fileUrl);
            fileImage.setCreateTime(new Date());
            fileImage.setUpdateTime(new Date());
            // 插入 FileImage 记录
            fileImageMapper.insert(fileImage);
            fileUrls.add(fileUrl); // 添加文件路径到 fileUrls
        } else {
            failedFiles.add(originalFileName);
            log.warn("OCR failed for file: {}", originalFileName);
        }
    }

    private byte[] compressImage(byte[] imageBytes, String fileName) throws IOException {
        System.out.println(fileName + " 进入到 compressImage 方法");
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));

        // 读取图片的元数据
        ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
        reader.setInput(ImageIO.createImageInputStream(new ByteArrayInputStream(imageBytes)));
        IIOMetadata metadata = reader.getImageMetadata(0);

        // Try different quality parameters until the file size is less than or equal to 1MB
        float quality = 0.95f;
        while (true) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            // 创建 ImageOutputStream
            ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
            writer.setOutput(ios);

            // 写入图片和元数据
            IIOImage iioImage = new IIOImage(img, null, metadata);
            writer.write(null, iioImage, param);
            writer.dispose();
            ios.close();

            byte[] compressedBytes = baos.toByteArray();
            if (compressedBytes.length <= 1024 * 1024 || quality <= 0.1f) {
                // 记录压缩后的文件名和体积大小
                log.info("Compressed file: {}, Size: {} bytes", fileName, compressedBytes.length);
                return compressedBytes;
            }
            quality -= 0.05f;
            System.out.println("Reducing quality to: " + quality);
        }
    }


    private String sendPostRequest(String data) {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost req = new HttpPost("http://127.0.0.1:1224/api/ocr");
                req.setHeader("Content-Type", "application/json");
                req.setEntity(new StringEntity(data));
                try (CloseableHttpResponse response = httpClient.execute(req)) {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode >= 200 && statusCode < 300) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            String result = EntityUtils.toString(entity);
//                            log.info("OCR Response: {}", result);
                            return parseOcrResult(result);
                        }
                    } else {
                        log.warn("OCR Request failed with status code: {}", statusCode);
                    }
                }
            } catch (IOException e) {
                retryCount++;
                if (retryCount == maxRetries) {
                    log.error("OCR Request failed after {} retries", maxRetries, e);
                } else {
                    log.warn("OCR Request failed, retrying... ({}/{})", retryCount, maxRetries);
                }
            }
        }
        return "";
    }


    public static String parseOcrResult(String result) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(result);
            int code = rootNode.path("code").asInt();
            if (code == 100) {
                JsonNode dataNode = rootNode.path("data");
                if (dataNode.isTextual()) {
                    String ocrData = dataNode.asText();
//                    log.info("OCR Data: {}", ocrData);
                    return ocrData;
                }
            }
            log.warn("Unexpected OCR response format or code: {}", result);
        } catch (Exception e) {
            log.error("Failed to parse OCR result: {}", result, e);
        }
        return "";
    }
}
