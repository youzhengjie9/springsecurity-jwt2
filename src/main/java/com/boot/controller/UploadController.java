package com.boot.controller;

import com.boot.annotation.OperationLog;
import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import com.boot.service.OssUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 上传控制器
 *
 * @author youzhengjie
 * @date 2022/10/13 10:17:47
 */
@RestController
@Api("上传接口")
@RequestMapping(path = "/upload")
@Slf4j
public class UploadController {

    @Autowired
    @Qualifier("minioUploadServiceImpl") //指定spring注入的实现类
    private OssUploadService ossUploadService;


    /**
     * 上传头像
     *
     * @param avatarFile 头像文件（名字一定要和el-upload的:name属性一致）
     * @return {@link ResponseResult}
     */
    @OperationLog("上传头像")
    @PostMapping(path = "/avatar")
    @ApiOperation("上传头像")
    public ResponseResult uploadAvatar(MultipartFile avatarFile){

        ResponseResult result = ossUploadService.imageUpload(avatarFile);

        return result;
    }

    /**
     * 文件删除
     *
     * @param fileFullName 文件名
     * @return {@link ResponseResult}
     */
    @OperationLog("文件删除")
    @DeleteMapping(path = "/fileDelete")
    @ApiOperation("文件删除")
    public ResponseResult fileDelete(@RequestParam("fileFullName") String fileFullName){

        return ossUploadService.fileDelete(fileFullName);
    }


    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param response 响应
     */
    @GetMapping(path = "/fileDownload")
    @ApiOperation("文件下载")
    public void fileDownload(@RequestParam("fileName") String fileName, HttpServletResponse response){

        ossUploadService.fileDownload(fileName,response);

    }


}
