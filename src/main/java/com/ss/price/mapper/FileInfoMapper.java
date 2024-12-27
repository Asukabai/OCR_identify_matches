package com.ss.price.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ss.price.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfo> {

    @Select("SELECT * FROM file_info WHERE MATCH(content) AGAINST(#{query}) OR file_name LIKE CONCAT('%', #{query}, '%') ORDER BY id DESC LIMIT #{page.getOffset()}, #{page.getSize()}")
    List<FileInfo> searchFilesByQuery(@Param("query") String query, @Param("page") Page<FileInfo> page);

    @Select("SELECT * FROM file_info WHERE content LIKE CONCAT('%', #{query}, '%') OR file_name LIKE CONCAT('%', #{query}, '%') ORDER BY id DESC LIMIT #{page.getOffset()}, #{page.getSize()}")
    List<FileInfo> likeSearchFilesByQuery(@Param("query") String query, @Param("page") Page<FileInfo> page);

}
