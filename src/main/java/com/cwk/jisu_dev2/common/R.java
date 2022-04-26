package com.cwk.jisu_dev2.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用，前端结果返回类
 * 其中字段与前端相互对应，进行数据封装
 * @param <T>
 */
@Data
public class R<T> {

    //编码：1成功，0和其它数字为失败
    private Integer code;

    //错误信息
    private String msg;

    //数据
    private T data;

    //封装一些额外的动态数据
    private Map map = new HashMap();

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
