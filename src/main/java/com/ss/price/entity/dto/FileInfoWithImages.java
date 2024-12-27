package com.ss.price.entity.dto;

import com.ss.price.entity.FileImage;
import com.ss.price.entity.FileInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
@Getter
@Setter
public class FileInfoWithImages {
    private FileInfo fileInfo;
    private List<FileImage> fileImages;

    public FileInfoWithImages(FileInfo fileInfo, List<FileImage> fileImages) {
        this.fileInfo = fileInfo;
        this.fileImages = fileImages;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public List<FileImage> getFileImages() {
        return fileImages;
    }

    public void setFileImages(List<FileImage> fileImages) {
        this.fileImages = fileImages;
    }
}
