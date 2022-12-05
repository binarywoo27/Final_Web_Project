package com.ssafy.home.model.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.home.model.dto.FavoriteDto;
import com.ssafy.home.model.mapper.FavoriteMapper;

@Service
public class FavoriteServiceImpl implements FavoriteService {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<FavoriteDto> getFavoriteList(String id) throws Exception {
//		List<FavoriteDto> result = sqlSession.getMapper(FavoriteMapper.class).getFavoriteList();
//		System.out.println(result);
//		return result;
		return sqlSession.getMapper(FavoriteMapper.class).getFavoriteList(id);
	}

	@Override
	public void addFavorite(FavoriteDto favoriteDto) throws SQLException {
		sqlSession.getMapper(FavoriteMapper.class).addFavorite(favoriteDto);		
	}

	@Override
	public void delFavorite(FavoriteDto favoriteDto) throws SQLException {
		sqlSession.getMapper(FavoriteMapper.class).delFavorite(favoriteDto);
	}
	
	
}
