package com.ss.price.entity.dto;


import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OcrResult {

    private boolean success;
    private String wordResult;
    private String tableResult;
}
