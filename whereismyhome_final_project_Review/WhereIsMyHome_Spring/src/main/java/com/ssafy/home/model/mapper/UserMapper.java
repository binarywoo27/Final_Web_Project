package com.ssafy.home.model.mapper;

import java.sql.SQLException;
import java.util.Map;

import com.ssafy.home.model.dto.UserDto;

public interface UserMapper {
	public UserDto login(UserDto userDto) throws SQLException;
	public UserDto userInfo(String id) throws SQLException;
	public void saveRefreshToken(Map<String, String> map) throws SQLException;
	public Object getRefreshToken(String userid) throws SQLException;
	public void deleteRefreshToken(Map<String, String> map) throws SQLException;
	public int regist(UserDto userDto) throws SQLException;
	public int update(UserDto userDto) throws SQLException;
	public int delete(String id) throws SQLException;
	public UserDto check(String id, String phone) throws SQLException;
	public int resetPw(String id, String password) throws SQLException;
}
