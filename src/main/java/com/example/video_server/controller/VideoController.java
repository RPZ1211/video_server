package com.example.video_server.controller;


import com.example.video_server.common.entity.ResultBean;
import com.example.video_server.service.VideoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("video")
@CrossOrigin
public class VideoController {

    
    @javax.annotation.Resource
    VideoService videoService;

    @GetMapping("/file/{remark}/{filePath}")
    @ApiOperation(value = "读取视频文件")
    @CrossOrigin
    public ResponseEntity<Resource> download(@PathVariable String remark,@PathVariable String filePath) {
        return videoService.download(remark,filePath);
    }

    @PostMapping(value="/upload",consumes = "multipart/*",headers = "content-type=multipart/form-data")
    @ApiOperation(value = "上传视频",consumes = "multipart/form-data",produces = "multipart/form-data")
    @ApiImplicitParam(name = "file", value = "文件", dataTypeClass = MultipartFile.class)
    public ResultBean upload(@ApiParam(name = "file",value = "file", required = true) MultipartFile file) {
        return videoService.upload(file);
    }


}
