package com.example.video_server.common.entity.enums;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

@Getter
@ApiModel(value = "返回信息")
public enum ResultEnum {

    UNKNOWN_ABNORMAL(0,"未知异常"),

    NOT_FILE(414,"文件不存在"),

    FILE_TYPE_ERROR(415,"文件格式错误"),

    UPLOAD_ERROR(416,"上传失败"),
    SUCCESS(200,"请求成功");


    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
