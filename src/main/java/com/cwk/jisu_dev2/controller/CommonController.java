package com.cwk.jisu_dev2.controller;

import com.cwk.jisu_dev2.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 主要用于文件的上传和下载
 * @author zzb04
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {


    @Value("${jisu.path}")
    private String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info("开始上传文件。。");
        log.info(file.toString());
        //当前file为临时文件，存储在临时空间中。

        //获取原始文件名(不建议，可能出现重名，覆盖)
        String originalFileName = file.getOriginalFilename();

        //获取原始文件名后缀
        String suffix =  originalFileName.substring(originalFileName.lastIndexOf("."));
        //故使用UUID重新生成文件名
        String fileName =  UUID.randomUUID().toString() + suffix;

        //创建目录
        File dir = new File(basePath,fileName);
        if (!dir.getParentFile().exists()){
            //目录不存在
            dir.getParentFile().mkdirs();
        }

        //故需要对上传文件进行转存
        try {
            file.transferTo(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        return R.success(fileName);
    }

    /**
     * 文件下载
     *
     * 同时，也实现了上传后，在页面中图片预览
     *
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流，获取文件
            FileInputStream fileInputStream = new FileInputStream(basePath+'\\'+name);
            //输出流，将文件输出到浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            //将读取的数据放在bytes数组中
            while((len = fileInputStream.read(bytes)) != -1){
                //将数据写入输出流中
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            //关闭两个数据流资源通道
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
