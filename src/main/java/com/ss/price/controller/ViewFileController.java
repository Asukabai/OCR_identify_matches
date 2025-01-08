package com.ss.price.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ss.price.config.FileProperties;
import com.ss.price.entity.FileImage;
import com.ss.price.entity.FileInfo;
import com.ss.price.entity.ProductInfo;
import com.ss.price.entity.dto.FileInfoResponse;
import com.ss.price.entity.dto.RequestQuery;
import com.ss.price.entity.dto.RespondDto;
import com.ss.price.entity.vo.RequestPack;
import com.ss.price.mapper.FileImageMapper;
import com.ss.price.mapper.FileInfoMapper;
import com.ss.price.mapper.ProductInfoMapper;
import com.ss.price.mapper.UploadLogMapper;
import com.ss.price.utils.RespondUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author zhangxuejin
 * @version 2.0.0 2025-1-7
 *
// * @deprecated  文件查询接口(分页)
 */

@CrossOrigin
@RestController
@RequestMapping("/ss/view")
@Slf4j
public class ViewFileController {
    @Resource
    FileImageMapper fileImageMapper;
    @Resource
    FileInfoMapper fileInfoMapper;
    @Resource
    ProductInfoMapper productInfoMapper;

    // 声明 executorService 变量
    private final ExecutorService executorService;

    // 创建一个固定大小的线程池
    // 当前线程池大小为10，这个值可能需要根据实际的系统资源和负载情况进行调整。如果系统资源有限，线程池过大可能会导致资源竞争；如果系统资源充足，线程池过小可能会导致任务排队等待。
    public ViewFileController() {
        int processors = Runtime.getRuntime().availableProcessors();
        this.executorService = new ThreadPoolExecutor(
                processors, // 核心线程数
                processors * 2, // 最大线程数
                0L, // 线程空闲时间
                TimeUnit.MILLISECONDS, // 时间单位
                new LinkedBlockingQueue<>(100), // 工作队列
                Executors.defaultThreadFactory(), // 线程工厂
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
        );
    }

    /**
     * 前端接口查询文件信息接口——将查询结果封装成分页对象，并返回给前端
     *
     * 修改原来的代码逻辑，由于现在要展示给前端的是 ProductInfo 类中的所有内容，所以需要将ProductInfo类中的所有内容，
     * 以及通过ProductInfo类中的file_name查找到file_info类中image_id，然后再根据image_id找到对应的FileImage类中的所有内容，
     * 最后一起封装到FileInfoResponse类中，再封装到Page对象中，最后返回给前端。
     *
     * @param operation
     * @return
     */

    @RequestMapping(value = "/getQueryFileInfoWithImages", method = {RequestMethod.GET, RequestMethod.POST})
    public RespondDto<Page<FileInfoResponse>> getQueryFileInfoWithImages(@RequestBody RequestPack<RequestQuery> operation) {
        System.out.println("进入分页查询方法");
        Page<ProductInfo> productInfoPage = new Page<>(operation.getReqData().getPageNum(), operation.getReqData().getPageSize());
        QueryWrapper<ProductInfo> productInfoQueryWrapper = new QueryWrapper<>();
        // 获取查询字符串
        String query = operation.getReqData().getQuery();
        // 打印查询字符串
        log.info("打印查询字符串：Search query: {}", query);

        if ("all".equalsIgnoreCase(query)) {
            // 如果查询参数为 "all"，则不应用任何查询条件
            log.info("查询所有记录");
        } else {
            // 否则，应用查询条件
            productInfoQueryWrapper.like("product_name", query)
                    .orderByDesc("id");
            // 打印生成的 SQL 语句
            log.info("打印生成的 SQL 语句：Generated SQL: {}", productInfoQueryWrapper.getSqlSegment());
        }

        Page<ProductInfo> productInfoPageResult = null;
        try {
            productInfoPageResult = productInfoMapper.selectPage(productInfoPage, productInfoQueryWrapper);
            // 打印查询结果
            log.info("查询结果数量: {}", productInfoPageResult.getRecords().size());
        } catch (Exception e) {
            log.error("查询出现异常: ", e);
            productInfoPageResult = new Page<>(); // 初始化一个空的 Page 对象
        }

        // 将查询到的 ProductInfo 记录封装到 FileInfoResponse 对象中
        List<FileInfoResponse> fileInfoResponseList = new ArrayList<>();
        for (ProductInfo productInfo : productInfoPageResult.getRecords()) {
            String fileName = productInfo.getFileName();

            // 根据 fileName 查询关联的 FileInfo 记录
            QueryWrapper<FileInfo> fileInfoQueryWrapper = new QueryWrapper<>();
            fileInfoQueryWrapper.eq("file_name", fileName);
            FileInfo fileInfo = fileInfoMapper.selectOne(fileInfoQueryWrapper);

            if (fileInfo != null) {
                // 根据 imageId 查询关联的 FileImage 记录
                QueryWrapper<FileImage> fileImageQueryWrapper = new QueryWrapper<>();
                fileImageQueryWrapper.eq("image_id", fileInfo.getImageId());
                List<FileImage> fileImages = fileImageMapper.selectList(fileImageQueryWrapper);

                // 使用 CompletableFuture 异步处理图片转换
                List<CompletableFuture<String>> futures = new ArrayList<>();
                for (FileImage fileImage : fileImages) {
                    String imageUrl = fileImage.getImageUrl();
                    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(imageUrl));
                                     ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                                    byte[] buffer = new byte[1024];
                                    int bytesRead;
                                    while ((bytesRead = bis.read(buffer)) != -1) {
                                        baos.write(buffer, 0, bytesRead);
                                    }
                                    byte[] imageBytes = baos.toByteArray();
                                    return Base64.getEncoder().encodeToString(imageBytes);
                                } catch (IOException e) {
                                    log.error("Failed to read image file: " + imageUrl, e);
                                    return null;
                                }
                            }, executorService) // 使用自定义线程池
                            .exceptionally(ex -> {
                                log.error("Exception occurred while converting image to Base64: " + imageUrl, ex);
                                return null;
                            });
                    futures.add(future);
                }
                // 等待所有异步任务完成
                CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
                allFutures.join();
                // 收集结果
                List<String> base64Images = futures.stream()
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                // 将 ProductInfo、FileInfo 和关联的 FileImage 记录封装到 FileInfoResponse 对象中
                FileInfoResponse fileInfoResponse = new FileInfoResponse(productInfo, fileInfo, fileImages);
                fileInfoResponse.setImageUrls(base64Images); // 假设 FileInfoResponse 有一个 setBase64Images 方法
                fileInfoResponseList.add(fileInfoResponse);
            }
        }

        // 创建返回的 Page 对象
        Page<FileInfoResponse> responsePage = new Page<>(productInfoPageResult.getCurrent(), productInfoPageResult.getSize(), productInfoPageResult.getTotal());
        responsePage.setRecords(fileInfoResponseList);
        // 返回封装好的数据
        return RespondUtils.success(responsePage, "查询成功！");
    }




    //通过这种方式，你可以确保在应用程序关闭时，线程池中的所有任务都被正确处理，避免资源泄漏和其他潜在的问题。
    // @PreDestroy 注解的方法会在 Spring 容器销毁 ViewFileController 实例之前被调用
    @PreDestroy
    public void shutdownExecutorService() {
        executorService.shutdown();    //这个方法会平滑地关闭线程池，不再接受新的任务，但会等待已经提交的任务执行完毕。
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {//如果在 60 秒内所有任务都完成了，awaitTermination 方法会返回 true。
                executorService.shutdownNow();//如果 awaitTermination 返回 false，表示在指定时间内没有完成所有任务，此时会调用 shutdownNow() 方法。shutdownNow() 方法会尝试立即停止所有正在执行的任务，并返回一个等待执行的任务列表。
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

}



