package com.itmk.result;

import lombok.Data;

/**
 * @Author: MK
 * @Description: 统一返回结果类
 * @Date: 2024/3/20
 */
@Data
public class ResultVo<T> {
    private Integer code;  // 200表示成功，其他表示失败
    private String msg;    // 提示信息
    private T data;       // 数据

    public static <T> ResultVo<T> success(T data) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> ResultVo<T> fail(String message) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(500);
        result.setMsg(message);
        return result;
    } 
    // getters and setters
}