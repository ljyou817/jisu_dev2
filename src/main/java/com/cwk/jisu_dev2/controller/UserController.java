package com.cwk.jisu_dev2.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cwk.jisu_dev2.common.R;
import com.cwk.jisu_dev2.entity.User;
import com.cwk.jisu_dev2.service.UserService;
import com.cwk.jisu_dev2.utils.SMSUtils;
import com.cwk.jisu_dev2.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author zzb04
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送手机验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user,HttpSession session){
        //获取客户手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            //生成随机的6位验证码
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info("验证码：{}",code);
            //调用阿里云提供的断行发送服务API，完成发送
            //SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);
            SMSUtils.sendMessage("阿里测试","SMS_150909",phone,code);
            //先保存生成的验证码，保存在Session中，为了验证过程
            session.setAttribute(phone,code);
            return R.success("手机短信发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info("所传递的数据：{}",map.toString());
        //获取客户手机号
        String phone = map.get("phone").toString();
        //获取客户的验证码code
        String code = map.get("code").toString();
        //进行验证码比对，比对Session中的验证码
        Object codeSession = session.getAttribute(phone);
        //比对相同则，登陆成功
        if (codeSession!=null && codeSession.equals(code)){
            //判断当前手机号对应用户是否为新用户，如果是将手机号保存
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            //由于过滤器，会根据Session中用户id，进行判断是否存在登录
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登陆失败");
    }

    /**
     * 移动端用户退出
     * @param
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginOut(HttpSession session){
        //session.removeAttribute("userPhone");
        session.removeAttribute("user");
        return R.success("退出成功");
    }
}
