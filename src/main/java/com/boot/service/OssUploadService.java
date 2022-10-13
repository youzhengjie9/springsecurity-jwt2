package com.boot.service;

import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import org.springframework.web.multipart.MultipartFile;

/**
 * oss上传service接口，目前只实现七牛云这种oss上传
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
     * @param fileFullName 文件全名，也就是下面这个代码生成的名字（记住不要加上域名），例如：
     *         String newFileName = new StringBuilder()
     *                 .append(fileDir)
     *                 .append(uuidFileName)
     *                 .append(fileSuffix).toString();
     *
     * @return 删除结果
     */
    ResponseResult fileDelete(String fileFullName);


}
