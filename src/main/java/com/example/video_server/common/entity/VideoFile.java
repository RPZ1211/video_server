package com.example.video_server.common.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "视频文件处理类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoFile {

    private String oldFilePath;

    private String newFilePath;

    private String newFileName;

}
