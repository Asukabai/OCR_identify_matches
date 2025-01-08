package com.ss.price.entity.dto;

import com.ss.price.entity.FileImage;
import com.ss.price.entity.FileInfo;
import com.ss.price.entity.ProductInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@Getter
@Setter
//public class FileInfoResponse {
//    private String fileName;
//    private String content;
//    private List<String> imageUrls;
//    private Date createTime;
//
//    public FileInfoResponse(FileInfo fileInfo, List<FileImage> fileImages) {
//        this.fileName = fileInfo.getFileName();
//        this.content = fileInfo.getWordContent();
//        this.createTime = fileInfo.getCreateTime();
//        this.imageUrls = new ArrayList<>();
//        for (FileImage fileImage : fileImages) {
//            this.imageUrls.add(fileImage.getImageUrl());
//        }
//    }
//}

public class FileInfoResponse {
    private ProductInfo productInfo;
    private FileInfo fileInfo;
    private List<FileImage> fileImages;
    private List<String> imageUrls;

    public FileInfoResponse(ProductInfo productInfo, FileInfo fileInfo, List<FileImage> fileImages) {
        this.productInfo = productInfo;
        this.fileInfo = fileInfo;
        this.fileImages = fileImages;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
