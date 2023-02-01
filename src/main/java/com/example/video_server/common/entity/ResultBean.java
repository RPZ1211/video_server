package com.example.video_server.common.entity;

import com.example.video_server.common.entity.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResultBean<T> implements Serializable {
    private ResultEnum result;
    private String msg;
    private T data;


    public ResultBean(){
    }

    public ResultBean(T data){
        this.data=data;
    }

    public static ResultBean success(){
        ResultBean result=new ResultBean<>();
        result.setResult(ResultEnum.SUCCESS);
        result.setMsg("成功");
        return result;
    }

    public static <T> ResultBean<T> success(T data){
        ResultBean<T> result=new ResultBean<>(data);
        result.setResult(ResultEnum.SUCCESS);
        result.setMsg("成功");
        return result;
    }

    public static ResultBean error(ResultEnum result, String msg){
        ResultBean res=new ResultBean();
        res.setResult(result);
        res.setMsg(msg);
        return res;
    }

    public static ResultBean error(ResultEnum result){
        ResultBean res=new ResultBean();
        res.setResult(result);
        return res;
    }
}
