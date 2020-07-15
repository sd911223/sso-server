package com.accurascience.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import com.accurascience.entity.User;

/**
 * 用户持久层（mybatis实现）
 * @author zhuchaojie
 *
 */

@Mapper
public interface UserDao {
	
	//表字段与实体类映射
	@Results({
		@Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR, id=true),
		@Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR),
		@Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
		@Result(column="role", property="role", jdbcType=JdbcType.VARCHAR),
		@Result(column="is_available", property="isAvailable", jdbcType=JdbcType.INTEGER),
		@Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
		@Result(column="e_mail", property="eMail", jdbcType=JdbcType.VARCHAR),
		@Result(column="hospital", property="hospital", jdbcType=JdbcType.INTEGER),
				})
	/**
	 * 查询用户信息
	 * @param vcfFile
	 * @return
	 */
    @Select("select user_name, password, role, is_available from user_info where user_name=#{userName}")
	User loadUserByUsername(@Param("userName")String userName);
	/**
	 * 注册新用户(医生)
	 * @param user
	 * @return
	 */
	@Insert("INSERT INTO user_info(user_id, user_name, password, role, is_available,e_mail,title,hospital) VALUES "
			+ "(#{user.userId}, #{user.userName}, #{user.password}, #{user.role}, #{user.isAvailable}, #{user.eMail}," +
			" #{user.title}, #{user.hospital})")
	Integer insertUser(@Param("user") User user);
	/**
	 * 修改注册信息(医生)
	 * @param user
	 * @return
	 */
	@Update("<script>" +
			"update user_info " +
			"<trim prefix=\"set\" suffixOverrides=\",\" suffix=\" where user_id=#{user.userId} \">"+
			"<if test='user.userName!=\"\"'> user_name=#{user.userName}, </if>" +
			"<if test='user.password!=\"\"'> password=#{user.password}, </if>" +
			"<if test='user.eMail!=\"\"'> e_mail=#{user.eMail}, </if>" +
			"<if test='user.title!=\"\"'> title=#{user.title}, </if>" +
			"<if test='user.hospital!=\"\"'> hospital=#{user.hospital} </if>" +
			"</trim>"+
			"</script>")
	Integer alterUser(@Param("user") User user);
	/**
	 * 查询用户信息
	 * @param vcfFile
	 * @return
	 */
	@Results({
			@Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR, id=true),
			@Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR),
			@Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
			@Result(column="role", property="role", jdbcType=JdbcType.VARCHAR),
			@Result(column="is_available", property="isAvailable", jdbcType=JdbcType.INTEGER),
			@Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
			@Result(column="e_mail", property="eMail", jdbcType=JdbcType.VARCHAR),
			@Result(column="hospital", property="hospital", jdbcType=JdbcType.INTEGER),
	})
	@Select("select user_id,user_name,e_mail,title,hospital from user_info where user_name=#{userName}")
	User checkUser(@Param("userName")String userName);

	/**
	 * 根据email查询id
	 * @param email
	 * @return
	 */
	@Select("select user_id from user_info where e_mail=#{email}")
	String checkId(@Param("email")String email);
}
