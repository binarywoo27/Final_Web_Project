package com.ssafy.home.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.home.model.dto.FavoriteDto;

@Mapper
public interface FavoriteMapper {
	public List<FavoriteDto> getFavoriteList(String id) throws SQLException;
	public void addFavorite(FavoriteDto favoriteDto) throws SQLException;
	public void delFavorite(FavoriteDto favoriteDto) throws SQLException;	
}
