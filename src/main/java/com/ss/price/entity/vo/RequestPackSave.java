package com.ss.price.entity.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;


@Data
@Getter
@Setter
public class RequestPackSave implements Serializable {

    private static final long serialVersionUID = -7103946653683777721L;

    private Long id;
    private String productId;
    private String isStored;
    private String isUsed;
    private String currentLocation;
    private String belongContent;
    private List<String> weldingContent; // 焊接具体内容
    private String failureReason; // 测试失败原因
    private String testingContent; // 测试内容
    private String questReason; // 问题描述
    private String operation; // 操作分类
    private List<String> assembleContent; // 装配描述
    private List<ContentFile> fileList;
    private BigInteger userIndex;
    private String userName;
    private MultipartFile[] multipartFiles;
}
