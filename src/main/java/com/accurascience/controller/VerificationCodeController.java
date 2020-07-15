package com.accurascience.controller;

import com.accurascience.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码相关控制器
 * @author zhuchaojie
 */
@RestController
public class VerificationCodeController {
    @Autowired
    private VerificationCodeService vcs;
    /**
     * 保存验证码和发送验证码邮件
     * @param email
     * @return 说明信息
     */
    @PostMapping("/password/code")
    public String insertCodeAndSendCode(@RequestParam(value="email") String email) {
        String msg = vcs.saveAndSendCode(email);
        return msg;

    }
    /**
     * 保存验证码和发送验证码邮件
     * @param email
     * @param inputCode 用户输入验证码
     * @param password 新密码
     * @return 说明信息
     */
    @PostMapping("/password/reset")
    public String insertUser(@RequestParam(value="email") String email, @RequestParam(value="inputCode") Integer inputCode
            , @RequestParam(value="password") String password) {
        String msg = vcs.verifyCodeAndResetPassword(email, inputCode, password);
        return msg;

    }
}
