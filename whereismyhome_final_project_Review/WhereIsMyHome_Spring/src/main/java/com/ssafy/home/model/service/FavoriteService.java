package com.ssafy.home.model.service;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.home.model.dto.FavoriteDto;

public interface FavoriteService {
	public List<FavoriteDto> getFavoriteList(String id) throws Exception;
	public void addFavorite(FavoriteDto favoriteDto) throws SQLException; 
	public void delFavorite(FavoriteDto favoriteDto) throws SQLException; 
}
