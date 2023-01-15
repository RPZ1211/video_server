package com.example.video_server.service.impl;

import com.example.video_server.common.entity.ResultBean;
import com.example.video_server.common.entity.VideoFile;
import com.example.video_server.common.entity.enums.ResultEnum;
import com.example.video_server.service.VideoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Log4j2
@Service
public class VideoServiceImpl implements VideoService {

    @Value("${input.file.path}")
    private String input_file_path;
    @Value("${output.file.path}")
    private String out_file_path;

    @Override
    public ResponseEntity<Resource> download(String remark,String filePath) {
        String path=out_file_path+remark+"/"+filePath;
        log.info("视频路径：{}",path);
        String contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(path)
                .build().toString();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.IMAGE_JPEG)
                .body(new FileSystemResource(path));
    }

    public final static String avatarFileSuffix = ".mp4.jpg.png";
    @Override
    public ResultBean upload(MultipartFile file) {
        // 判断是否有文件
        if (file.isEmpty()) {
            return ResultBean.error(ResultEnum.UNKNOWN_ABNORMAL);
        }
        // 获取文件名
        String originalFilename = file.getOriginalFilename();

        // 获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        // 判断是不是可以接收的文件类型
        if (!avatarFileSuffix.contains(suffix)) {
            return ResultBean.error(ResultEnum.FILE_TYPE_ERROR);
        }
        // 用uuid作为新的文件名
        String fileName = UUID.randomUUID() +"."+suffix;

        try {
            // 获取静态资源路径
            File path = new File("");
            // 设置存放路径
            File upload=new File(input_file_path+fileName);
            // 如果目录不存在就创建目录
            if(!upload.exists()){
                upload.mkdirs();
            }
            // 写入
            file.transferTo(upload);
            String newFileName= UUID.randomUUID() + ".m3u8";//剪切后生成的m3u8的文件名
            log.info("文件接收成功，存储路径：{}",input_file_path+fileName);
            VideoFile videoFile=new VideoFile(input_file_path+fileName
                    ,out_file_path+"cessssss/","123.m3u8");
            VideoCuttingService.ConvertVideo(videoFile);
            return ResultBean.success("cessssss/1.m3u8");
        } catch (IOException e) {
            e.printStackTrace();
            return ResultBean.error(ResultEnum.UPLOAD_ERROR);
        }
    }
}