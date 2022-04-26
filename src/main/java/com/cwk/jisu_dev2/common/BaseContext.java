package com.cwk.jisu_dev2.common;

/**
 *
 * 基于TreadLocal的封装工具类
 *
 * @author zzb04
 */
public class BaseContext {
    private static ThreadLocal<Long> treadLocal = new ThreadLocal<>();

    public static void setTreadId(Long id){
        treadLocal.set(id);
    }

    public static Long getTreadId(){
        return treadLocal.get();
    }
}
