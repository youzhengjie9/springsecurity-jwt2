package com.boot.service;

import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * oss上传service接口
 * @author youzhengjie
 * @date 2022-10-06 23:13:28
 */
public interface OssUploadService {

    /**
     * oss图片上传
     * @param imageFile
     * @return 上传结果
     */
    ResponseResult imageUpload(MultipartFile imageFile);

    /**
     * oss文件删除
     * @param fileFullName 文件全名，
     *
     * @return 删除结果
     */
    ResponseResult fileDelete(String fileFullName);

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param response 响应
     */
    default void fileDownload(String fileName, HttpServletResponse response){

        throw new UnsupportedOperationException("该实现类暂不支持文件下载操作,请切换到其他实现类！");
    }


}
