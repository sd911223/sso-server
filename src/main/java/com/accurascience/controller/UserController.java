package com.accurascience.controller;

import java.util.HashMap;
import java.util.Map;

import com.accurascience.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.accurascience.service.UserService;
/**
 * 获取用户信息
 * @author zhuchaojie
 *
 */
@RestController
@Api("用户管理")
public class UserController {
	@Autowired
	private UserService us;
	/**
	 * 用户信息查询
	 * @param user
	 * @return
	 */
	@ApiOperation("用户信息查询")
	@RequestMapping(value = { "/auth/user" }, produces = "application/json")
	public Map<String, Object> userInfo(OAuth2Authentication authentication) {
	    Map<String, Object> userInfo = new HashMap<>();
	    //获得用户详细信息
		String username = ((org.springframework.security.core.userdetails.User)authentication.getUserAuthentication()
				.getPrincipal()).getUsername();
//		String username = authentication.getUserAuthentication().getPrincipal().toString();//jwt获得用户名的方式
		User user = us.checkUser(username);
		userInfo.put("user",user);
	    //权限
	    userInfo.put("authorities", AuthorityUtils.authorityListToSet(
	    		authentication.getUserAuthentication().getAuthorities()));
	    return userInfo;
	}
	/**
	 * 用户注册
	 * @param userName
	 * @param password
	 * @param role
	 * @return
	 */
	@PostMapping("/oauth/add_user")
	@ResponseBody
	public String insertUser(@RequestParam(value="user_name") String userName, @RequestParam(value="password") String password,
		@RequestParam(value="eMail") String eMail, @RequestParam(value="title") String title,
		@RequestParam(value="hospital") String hospital) {
		String msg = us.insertUser(userName, password, eMail, title, hospital);
		return msg;
		
	}
	/**
	 * 用户信息更新
	 * @param userName
	 * @param password
	 * @return
	 */
	@PostMapping("/auth/alter_user")
	@ResponseBody
	public String insertUser(@RequestParam(value="id") String id
			, @RequestParam(value="user_name", required=false, defaultValue="") String userName
			, @RequestParam(value="password", required=false, defaultValue="") String password
			, @RequestParam(value="eMail", required=false, defaultValue="") String eMail
			, @RequestParam(value="title", required=false, defaultValue="") String title
			, @RequestParam(value="hospital", required=false, defaultValue="") String hospital) {
		String msg = us.alterUser(id, userName, password, eMail, title, hospital);
		return msg;

	}

}
