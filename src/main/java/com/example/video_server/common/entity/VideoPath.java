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
public class VideoPath {

    @ApiModelProperty(value = "待解析文件路径")
    private String inputPath;

    @ApiModelProperty(value = "文件输出路径,不包含文件名称")
    private String outPath;

    @ApiModelProperty(value = "输出文件名称,默认后缀.m3u8")
    private String newFileName;

}
