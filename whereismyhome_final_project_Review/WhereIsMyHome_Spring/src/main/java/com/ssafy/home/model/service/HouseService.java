package com.ssafy.home.model.service;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.home.model.dto.HouseDto;

public interface HouseService {
	public Integer searchHouse(HouseDto houseDto) throws SQLException;
	public void updateHouseHit(HouseDto houseDto) throws SQLException;
	public void addHouseHit(HouseDto houseDto) throws SQLException;
	public List<HouseDto> getHouseRank() throws SQLException;
}
