package com.accurascience.dao;

import com.accurascience.entity.User;
import com.accurascience.entity.VerificationCode;
import org.apache.ibatis.annotations.*;

/**
 * 验证码持久层（mybatis实现）
 * @author zhuchaojie
 *
 */

@Mapper
public interface VerificationCodeDao {
    /**
     * 添加新验证码
     * @param verificationCode
     * @return
     */
    @Insert("INSERT INTO verification_code(code_id, code, valid_date, valid_times, user_id) VALUES "+
            "(#{verificationCode.codeId}, #{verificationCode.code}, #{verificationCode.validDate}" +
            ", #{verificationCode.validTimes}, #{verificationCode.userId})")
    Integer insertCode(@Param("verificationCode") VerificationCode verificationCode);

    /**
     * 验证用户输入验证码合法性
     * @param inputCode 用户输入验证码
     * @param userId 用户ID
     * @return 验证码ID
     */
    @Select("SELECT code_id FROM verification_code where user_id=#{userId} and code=#{inputCode} and valid_times > 0" +
            " and valid_date >= now()")
    String checkCode(@Param("inputCode")Integer inputCode, @Param("userId")String userId);

    /**
     * 有效次数-1
     * @param userId 用户ID
     * @return
     */
    @Update("update verification_code set valid_times=valid_times-1 where user_id=#{userId} and valid_times > 0")
    Integer updateValidTimes(@Param("userId")String userId);

    /**
     * 清楚验证码数据
     * @param userId 用户ID
     * @return
     */
    @Delete("delete from verification_code where user_id=#{userId}")
    Integer deleteCode(@Param("userId")String userId);

}
