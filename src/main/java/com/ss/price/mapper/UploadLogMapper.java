package com.ss.price.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ss.price.entity.FileInfo;
import com.ss.price.entity.UploadLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UploadLogMapper extends BaseMapper<UploadLog> {

}
