package com.accurascience.service;

import java.util.UUID;

import com.accurascience.util.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.accurascience.dao.UserDao;
import com.accurascience.entity.User;
/**
 * 自定义user业务逻辑层
 * @author zhuchaojie
 *
 */
@Service
@Slf4j
public class UserService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);
	
    @Autowired
    private UserDao ud;
	@Autowired
	private EmailUtil eu;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = ud.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        //user对象转换
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User
        		(username, user.getPassword(), user.getIsAvailable().equals("1")?true:false, true/*为过期*/, true/*凭证为过期*/, true/*未锁定*/, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        logger.info(username+"执行了登录操作。");
        return userDetails;
	}
    /**
     * 注册新用户
     * @param userName
     * @return
     */
	public String  insertUser(String userName, String password, String eMail, String title, String hospital) {
		User user = null;
		//加锁，防止并发情况下出现重名现象
		synchronized (this) {
			user = ud.loadUserByUsername(userName);
			//重名
			if (user != null) {
				logger.warn("管理员执行了添加用户的操作。说明：" + userName + " 用户名重复。");
				return "username-already-exist";
			}
		}
		if(!userName.equals("") && !password.equals("") && !eMail.equals("")) {
			//执行注册
			user = new User();
			//id
			UUID uuid = UUID.randomUUID();
			user.setUserId(uuid.toString().replaceAll("-", ""));
			//有效性
			user.setIsAvailable("1");
			user.setPassword(password.equals("")?"":new BCryptPasswordEncoder().encode(password));
			//角色
			user.setRole("user");
			//医生名
			user.setUserName(userName);
			//邮件
			user.seteMail(eMail);
			//头衔
			user.setTitle(title);
			//医院
			user.setHospital(hospital);
			Integer count = ud.insertUser(user);
			String msg = count==1?"success":"failed";
			logger.warn("管理员执行了添加用户的操作。成功。");
			eu.sendHtmlMail(eMail,"友情前提", "<h2>"+userName+"，您已经成功注册了！</h2>");
			return msg;
		}
		logger.warn("管理员执行了添加用户的操作失败。说明：数据格式有问题");
		return "data-format-error";
	}

	/**
	 * 用户信息更新
	 * @param id
	 * @return
	 */
	public String alterUser(String id, String userName, String password, String eMail, String title, String hospital){

		//执行更新
		User user = new User();
		//id
		user.setUserId(id);
        //密码
		user.setPassword(password.equals("")?"":new BCryptPasswordEncoder().encode(password));
		//医生名
		user.setUserName(userName);
		//邮件
		user.seteMail(eMail);
		//头衔
		user.setTitle(title);
		//医院
		user.setHospital(hospital);
		Integer count = ud.alterUser(user);
		String msg = count==1?"success":"failed";
		logger.warn("用户执行了更新信息的操作。成功。");
		return msg;

	}

	/**
	 * 查询用户详细信息
	 * @param username
	 * @return
	 */
	public User checkUser(String username){
       User user = ud.checkUser(username);
       return user;
	}
}
