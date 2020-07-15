package com.accurascience.entity;

import java.sql.Timestamp;

/**
 * 验证码实体类
 */
public class VerificationCode {
    private String codeId;
    private Integer code;
    private Timestamp validDate;
    private Integer validTimes;
    private String userId;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Timestamp getValidDate() {
        return validDate;
    }

    public void setValidDate(Timestamp validDate) {
        this.validDate = validDate;
    }

    public Integer getValidTimes() {
        return validTimes;
    }

    public void setValidTimes(Integer validTimes) {
        this.validTimes = validTimes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
