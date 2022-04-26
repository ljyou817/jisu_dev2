package com.cwk.jisu_dev2.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 重写元数据对象处理器
 *
 * @author zzb04
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入时自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert,公共字段开始填充");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        //由于在该类中无法使用HttpServletRequest,所以无法直接利用request,Session获取id
        //补充知识：
        //客户端发送的每次Http请求，在对应服务器中都分配了一个新的线程来处理
        //在一次请求中通过一个线程依次经过loginFilter，Controller中的update和myMetaObjectHandler中的updateFill
        //可通过Thread.currentThread().getId() 验证以上方法为一个线程

        //故可以使用ThreadLocal,线程的局部变量，能保存数据进本线程，进行获取id
        //方法：set，get

        //实现，1.编写BaseContext工具类，基于TreadLocal
        //2.在loginFilter中电泳工具类中的set方法设置当前登录用户的id
        //3.在myMetaObjectHandler中调用get获取id

        //注意。尽量不要直接使用ThreadLocal中的原生方法set和get。
        //应为只会导致该类下只能在update中调用，不能再insert中使用（不同线程）
        metaObject.setValue("createUser",BaseContext.getTreadId());
        metaObject.setValue("updateUser",BaseContext.getTreadId());

    }

    /**
     * 更新时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update,公共字段开始填充");
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getTreadId());
    }
}
