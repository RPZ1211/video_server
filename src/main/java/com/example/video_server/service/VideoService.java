package com.example.video_server.service;

import com.example.video_server.common.entity.ResultBean;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {

    /**
     * @param filePath 文件路径
     * @return 视频文件
     */
    ResponseEntity<Resource> download(String remark,String filePath);

    ResultBean upload(MultipartFile file);

}
