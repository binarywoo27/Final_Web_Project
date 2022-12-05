package com.ssafy.home.model.service;

import com.ssafy.home.model.dto.UserDto;

public interface UserService {
	public UserDto login(UserDto userDto) throws Exception;
	public UserDto userInfo(String id) throws Exception;
	public void saveRefreshToken(String userid, String refreshToken) throws Exception;
	public Object getRefreshToken(String userid) throws Exception;
	public void deleteRefreshToken(String userid) throws Exception;
	public int registUser(UserDto userDto) throws Exception;
	public int updateUser(UserDto userDto) throws Exception;
	public int deleteUser(String id) throws Exception;
	public UserDto checkUser(String id, String phone) throws Exception;
	public int resetPw(String id, String password) throws Exception;
}
