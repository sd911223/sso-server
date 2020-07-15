package com.accurascience.service.impl;

import com.accurascience.dao.UserDao;
import com.accurascience.dao.VerificationCodeDao;
import com.accurascience.entity.User;
import com.accurascience.entity.VerificationCode;
import com.accurascience.service.VerificationCodeService;
import com.accurascience.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    @Autowired
    private UserDao ud;
    @Autowired
    private VerificationCodeDao vcd;
    @Autowired
    private EmailUtil eu;
    @Transactional//事务控制
    @Override
    public String saveAndSendCode(String email) {
        //查询用户ID
        String userId = ud.checkId(email);
        if(userId == null){//用户不存在

            return "user-not-exist";
        }
        //清空用户验证码信息
        vcd.deleteCode(userId);
        //生成验证码
        Integer code = (int)((Math.random()*9+1)*100000);
        //验证码存入数据库
        VerificationCode vc = new VerificationCode();
        vc.setCode(code);
        UUID uuid = UUID.randomUUID();
        vc.setCodeId(uuid.toString().replaceAll("-", ""));
        vc.setUserId(userId);//id
        vc.setValidTimes(5);//有效次数（默认五次）
        vc.setValidDate(Timestamp.valueOf(LocalDateTime.now().plusMinutes(5)/*当前日期加五分钟*/));//有期日期，默认五分钟
        Integer result = vcd.insertCode(vc);
        //发送验证码邮件
        if(result == 1){
            eu.sendHtmlMail(email, "忘记密码验证", "这是重置密码需要的验证码："+code
                    +"<br/>备注：验证码五分钟内有效，最多输入错误五次。");
            return "success";
        }else{
            return "failed";
        }


    }
    @Transactional//事务控制
    @Override
    public String verifyCodeAndResetPassword(String email, Integer inputCode, String password) {
        //查询用户ID
        String userId = ud.checkId(email);
        if(userId == null){//用户不存在

            return "user-not-exist";
        }
        //验证用户输入验证码合法性
        String codeId = vcd.checkCode(inputCode, userId);
        if(codeId == null){
            //有效次数-1
            vcd.updateValidTimes(userId);
            return "code-invalid";
        }
        //更新密码
        User user = new User();
        user.setUserId(userId);
        //不需要更新，设置为空字符串
        user.setHospital("");
        user.setTitle("");
        user.seteMail("");
        user.setUserName("");
        //设置密码
        if(password == null || password.equals("")){
            return "password-invalid";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        Integer result = ud.alterUser(user);
        //清空用户验证码信息
        vcd.deleteCode(userId);
        return result==1?"success":"failed";
    }
}
