package com.ssafy.home.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.home.model.dto.BoardParameterDto;
import com.ssafy.home.model.dto.QnaDto;

@Mapper
public interface QnaMapper {
	
	public int writeArticle(QnaDto qnaDto) throws SQLException;
	public List<QnaDto> listArticle(BoardParameterDto boardParameterDto) throws SQLException;
	public int getTotalCount(BoardParameterDto boardParameterDto) throws SQLException;
	public QnaDto getArticle(int articleno) throws SQLException;
	public void updateHit(int articleno) throws SQLException;
	public int modifyArticle(QnaDto qnaDto) throws SQLException;
	public void deleteMemo(int articleno) throws SQLException;
	public int deleteArticle(int articleno) throws SQLException;
	
}