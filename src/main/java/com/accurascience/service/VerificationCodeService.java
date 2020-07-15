package com.accurascience.service;

/**
 * 验证码业务逻辑层
 */
public interface VerificationCodeService {
    /**
     * 保存验证码并且通过email发送给用户
     * @param email
     * @return
     */
    String saveAndSendCode(String email);

    /**
     * 验证验证码并重置密码
     * @param email
     * @param inputCode
     * @param password
     * @return
     */
    String verifyCodeAndResetPassword(String email, Integer inputCode, String password);
}
