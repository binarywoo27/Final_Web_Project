package com.ssafy.home.model.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.home.model.dto.HouseDto;
import com.ssafy.home.model.mapper.HouseMapper;

@Service
public class HouseServiceImpl implements HouseService {
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public Integer searchHouse(HouseDto houseDto) throws SQLException {
		return sqlSession.getMapper(HouseMapper.class).searchHouse(houseDto);
	}

	@Override
	public void updateHouseHit(HouseDto houseDto) throws SQLException {
		sqlSession.getMapper(HouseMapper.class).updateHouseHit(houseDto);
	}

	@Override
	public void addHouseHit(HouseDto houseDto) throws SQLException {
		sqlSession.getMapper(HouseMapper.class).addHouseHit(houseDto);		
	}

	@Override
	public List<HouseDto> getHouseRank() throws SQLException {
		return sqlSession.getMapper(HouseMapper.class).getHouseRank();
	}

}
