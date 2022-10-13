package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.service.OssUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传控制器
 *
 * @author youzhengjie
 * @date 2022/10/13 10:17:47
 */
@RestController
@RequestMapping(path = "/upload")
public class UploadController {

    @Autowired
    @Qualifier("qiniuOssUploadServiceImpl") //指定spring注入的实现类为七牛云oss实现类
    private OssUploadService ossUploadService;


    /**
     * 上传头像
     *
     * @param avatar 头像
     * @return {@link ResponseResult}
     */
    @PostMapping(path = "/avatar")
    public ResponseResult uploadAvatar(MultipartFile avatar){

        return ossUploadService.imageUpload(avatar);
    }

    /**
     * 文件删除
     * @return
     */
    @DeleteMapping(path = "/fileDelete")
    public ResponseResult fileDelete(String fileFullName){

        return ossUploadService.fileDelete(fileFullName);
    }

}
