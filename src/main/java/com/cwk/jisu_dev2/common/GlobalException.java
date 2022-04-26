package com.cwk.jisu_dev2.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 *
 * 全局异常捕获类
 *
 * @ControllerAdvice给Controller控制器添加统一的操作或处理。
 * @author zzb04
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalException {
    /**
     * 处理数据库语句异常
     *
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
        log.error(e.getMessage());
        if (e.getMessage().contains("Duplicate entry")){
            //将异常进行提取，并将其显示在前端
            String[] spilt = e.getMessage().split(" ");
            String msg = spilt[2] + "已存在";
            return R.error(msg);
        }
        return R.error("出现未知错误");
    }

    /**
     * 处理自定义异常
     * 即处理删除分类时异常信息
     *
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException e){
        log.error(e.getMessage());
        return R.error(e.getMessage());
    }
}
