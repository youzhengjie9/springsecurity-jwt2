package com.boot.service.impl;

import com.boot.config.MinioProperties;
import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import com.boot.service.OssUploadService;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * minio上传服务service impl
 *
 * @author youzhengjie
 * @date 2022/10/28 13:59:35
 */
@Service("minioUploadServiceImpl")
@Slf4j
public class MinioUploadServiceImpl implements OssUploadService {

    @Autowired
    private MinioProperties minioProperties;

    /**
     * 注入MinioClient，用于操作minio
     */
    @Autowired
    private MinioClient minioClient;



    /**
     * 检查文件是否是图片类型
     * @param originalFilename
     * @return true代表是图片，false则不是图片
     */
    private boolean isImage(String originalFilename){
        //将文件名全部变小写
        String lowerOriginalFilename = originalFilename.toLowerCase();

        return lowerOriginalFilename.endsWith(".jpg") ||
                lowerOriginalFilename.endsWith(".png") ||
                lowerOriginalFilename.endsWith(".jpeg");
    }

    /**
     * minio图片上传
     *
     * @param imageFile 图像文件
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult imageUpload(MultipartFile imageFile) {

        //封装响应结果
        ResponseResult<Object> result = new ResponseResult<>();

        try {
            //获取上传前的文件原名
            String oldFileName = imageFile.getOriginalFilename();

            //如果不是图片则直接返回
            if(!isImage(oldFileName)){
                result.setCode(ResponseType.FILE_FORMAT_UNSUPPORT.getCode());
                result.setMsg(ResponseType.FILE_FORMAT_UNSUPPORT.getMessage());
                return result;
            }

            //以日期作为目录，每一天的图片都会放到不同的目录下，方便管理
            String fileDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));

            //UUID文件名
            String uuidFileName = UUID.randomUUID().toString().replaceAll("-", "");

            //获取文件后缀名 .jpg
            String fileSuffix= oldFileName.substring(oldFileName.lastIndexOf("."));

            //上传到minio中的新的图片文件名
            String newFileName = new StringBuilder()
                    .append(fileDir)
                    .append(uuidFileName)
                    .append(fileSuffix).toString();

            //获取文件流
            InputStream inputStream = imageFile.getInputStream();
            //获取文件大小
            long size = imageFile.getSize();
            //获取内容类型
            String contentType = imageFile.getContentType();

            //构建文件上传所需要的东西（PutObjectArgs）
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(newFileName)
                    .stream(inputStream, size, -1)
                    .contentType(contentType)
                    .build();
            //开始进行minio文件上传
            minioClient.putObject(putObjectArgs);

            //获取该上传到minio的文件的url（使外网可以访问）
            String fileUrl=minioProperties.getEndpoint()+"/"+minioProperties.getBucketName()+"/"+newFileName;
            result.setCode(ResponseType.IMAGE_UPLOAD_SUCCESS.getCode());
            result.setMsg(ResponseType.IMAGE_UPLOAD_SUCCESS.getMessage());
            result.setData(fileUrl);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(ResponseType.IMAGE_UPLOAD_ERROR.getCode());
            result.setMsg(ResponseType.IMAGE_UPLOAD_ERROR.getMessage());
            return result;
        }

    }

    /**
     * minio文件删除
     *
     * @param fileFullName 文件全名 。格式例如：2022/10/28/4f74aa358a4548d4860c110ebec3831f.jpg
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult fileDelete(String fileFullName) {

        //封装响应结果
        ResponseResult<Object> result = new ResponseResult<>();

        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(fileFullName)
                    .build();
            minioClient.removeObject(removeObjectArgs);
            result.setCode(ResponseType.FILE_DELETE_SUCCESS.getCode());
            result.setMsg(ResponseType.FILE_DELETE_SUCCESS.getMessage());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(ResponseType.FILE_DELETE_ERROR.getCode());
            result.setMsg(ResponseType.FILE_DELETE_ERROR.getMessage());
            return result;
        }
    }


    /**
     * minio文件下载。
     *
     * @param fileName 文件名称 。格式例如：2022/10/28/4f74aa358a4548d4860c110ebec3831f.jpg
     * @param response 响应
     */
    @Override
    public void fileDownload(String fileName, HttpServletResponse response) {

        try {

            // 获取对象信息
            StatObjectArgs statObjectArgs = StatObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(fileName)
                    .build();
            StatObjectResponse statObject = minioClient.statObject(statObjectArgs);

            /**
             * 描述: content-type 指示响应内容的格式
             * content-disposition 指示如何处理响应内容。
             * 一般有两种方式：
             * inline：直接在页面显示-预览
             * attachment：以附件形式下载-下载
             */
            response.setContentType(statObject.contentType());
//            response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Content-Disposition", "attachment; filename=" +
                    URLEncoder.encode(fileName, "UTF-8"));

            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(fileName)
                    .build());

            IOUtils.copy(inputStream,response.getOutputStream());
        }catch (Exception e){
           throw new RuntimeException("文件下载失败");
        }

    }
}
