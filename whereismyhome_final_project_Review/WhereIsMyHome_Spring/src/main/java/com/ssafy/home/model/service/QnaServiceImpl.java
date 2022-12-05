package com.ssafy.home.model.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.home.model.dto.BoardParameterDto;
import com.ssafy.home.model.dto.QnaDto;
import com.ssafy.home.model.mapper.QnaMapper;
import com.ssafy.home.util.PageNavigation;

@Service
public class QnaServiceImpl implements QnaService {
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public boolean writeArticle(QnaDto qnaDto) throws Exception {
		if(qnaDto.getSubject() == null || qnaDto.getContent() == null) {
			throw new Exception();
		}
		return sqlSession.getMapper(QnaMapper.class).writeArticle(qnaDto) == 1;
	}

	@Override
	public List<QnaDto> listArticle(BoardParameterDto boardParameterDto) throws Exception {
		int start = boardParameterDto.getPg() == 0 ? 0 : (boardParameterDto.getPg() - 1) * boardParameterDto.getSpp();
		boardParameterDto.setStart(start);
		return sqlSession.getMapper(QnaMapper.class).listArticle(boardParameterDto);
	}

	@Override
	public PageNavigation makePageNavigation(BoardParameterDto boardParameterDto) throws Exception {
		int naviSize = 5;
		PageNavigation pageNavigation = new PageNavigation();
		pageNavigation.setCurrentPage(boardParameterDto.getPg());
		pageNavigation.setNaviSize(naviSize);
		int totalCount = sqlSession.getMapper(QnaMapper.class).getTotalCount(boardParameterDto);//총글갯수  269
		pageNavigation.setTotalCount(totalCount);  
		int totalPageCount = (totalCount - 1) / boardParameterDto.getSpp() + 1;//27
		pageNavigation.setTotalPageCount(totalPageCount);
		boolean startRange = boardParameterDto.getPg() <= naviSize;
		pageNavigation.setStartRange(startRange);
		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < boardParameterDto.getPg();
		pageNavigation.setEndRange(endRange);
		pageNavigation.makeNavigator();
		return pageNavigation;
	}

	@Override
	public QnaDto getArticle(int articleno) throws Exception {
		return sqlSession.getMapper(QnaMapper.class).getArticle(articleno);
	}
	
	@Override
	public void updateHit(int articleno) throws Exception {
		sqlSession.getMapper(QnaMapper.class).updateHit(articleno);
	}

	@Override
	@Transactional
	public boolean modifyArticle(QnaDto qnaDto) throws Exception {
		return sqlSession.getMapper(QnaMapper.class).modifyArticle(qnaDto) == 1;
	}

	@Override
	@Transactional
	public boolean deleteArticle(int articleno) throws Exception {
//		sqlSession.getMapper(QnaMapper.class).deleteMemo(articleno);
		return sqlSession.getMapper(QnaMapper.class).deleteArticle(articleno) == 1;
	}
}