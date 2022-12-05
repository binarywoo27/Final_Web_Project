package com.ssafy.home.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.home.model.dto.HouseDto;

@Mapper
public interface HouseMapper {
	public Integer searchHouse(HouseDto houseDto) throws SQLException;
	public void updateHouseHit(HouseDto houseDto) throws SQLException;
	public void addHouseHit(HouseDto houseDto) throws SQLException;
	public List<HouseDto> getHouseRank() throws SQLException;
}
