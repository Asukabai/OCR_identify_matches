package com.ss.price.entity.dto;

import com.ss.price.entity.FileImage;
import com.ss.price.entity.FileInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@Getter
@Setter
public class FileInfoResponse {
    private String fileName;
    private String content;
    private List<String> imageUrls;
    private Date createTime;

    public FileInfoResponse(FileInfo fileInfo, List<FileImage> fileImages) {
        this.fileName = fileInfo.getFileName();
        this.content = fileInfo.getContent();
        this.createTime = fileInfo.getCreateTime();
        this.imageUrls = new ArrayList<>();
        for (FileImage fileImage : fileImages) {
            this.imageUrls.add(fileImage.getImageUrl());
        }
    }
}
