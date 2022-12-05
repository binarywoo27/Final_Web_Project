package com.ssafy.home.model.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.home.model.dto.UserDto;
import com.ssafy.home.model.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public UserDto login(UserDto userDto) throws Exception {
		return sqlSession.getMapper(UserMapper.class).login(userDto);
	}

	@Override
	public void saveRefreshToken(String userid, String refreshToken) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id",  userid);
		map.put("token", refreshToken);
		sqlSession.getMapper(UserMapper.class).saveRefreshToken(map);
	}

	@Override
	public Object getRefreshToken(String userid) throws Exception {
		return sqlSession.getMapper(UserMapper.class).getRefreshToken(userid);
	}

	@Override
	public void deleteRefreshToken(String userid) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("token", null);
		sqlSession.getMapper(UserMapper.class).deleteRefreshToken(map);
	}

	@Override
	public UserDto userInfo(String id) throws Exception {
		return sqlSession.getMapper(UserMapper.class).userInfo(id);
	}

	@Override
	public int registUser(UserDto userDto) throws Exception {
		return sqlSession.getMapper(UserMapper.class).regist(userDto);
	}

	@Override
	public int updateUser(UserDto userDto) throws Exception {
		return sqlSession.getMapper(UserMapper.class).update(userDto);
	}

	@Override
	public int deleteUser(String id) throws Exception {
		return sqlSession.getMapper(UserMapper.class).delete(id);
	}

	@Override
	public UserDto checkUser(String id, String phone) throws Exception {
		return sqlSession.getMapper(UserMapper.class).check(id, phone);
	}

	@Override
	public int resetPw(String id, String password) throws Exception {
		return sqlSession.getMapper(UserMapper.class).resetPw(id, password);
	}

}
