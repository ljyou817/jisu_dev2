package com.cwk.jisu_dev2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@SpringBootApplication
@ServletComponentScan // 开启基于注解方式的Servlet组件扫描支持
//@EnableCaching
public class JisuDev2Application {

    public static void main(String[] args) {
        SpringApplication.run(JisuDev2Application.class, args);
        log.info("项目启动");
    }

}
