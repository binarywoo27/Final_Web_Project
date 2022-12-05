package com.ssafy.home.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.model.dto.BoardParameterDto;
import com.ssafy.home.model.dto.QnaDto;
import com.ssafy.home.model.service.QnaService;

//http://localhost:9999/vue/swagger-ui.html
//@CrossOrigin(origins = { "*" }, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.POST} , maxAge = 6000)
@RestController
@RequestMapping("/qna")
public class QnaController {

	private static final Logger logger = LoggerFactory.getLogger(QnaController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	private QnaService qnaService;

	@PostMapping
	public ResponseEntity<String> writeArticle(@RequestBody QnaDto qnaDto) throws Exception {
		logger.info("writeArticle - 호출");
		if (qnaService.writeArticle(qnaDto)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
	
	@GetMapping
	public ResponseEntity<List<QnaDto>> listArticle( BoardParameterDto boardParameterDto) throws Exception {
		logger.info("listArticle - 호출");
		return new ResponseEntity<List<QnaDto>>(qnaService.listArticle(boardParameterDto), HttpStatus.OK);
	}
	
	@GetMapping("/{articleno}")
	public ResponseEntity<QnaDto> getArticle(@PathVariable("articleno")  int articleno) throws Exception {
		logger.info("getArticle - 호출 : " + articleno);
		qnaService.updateHit(articleno);
		return new ResponseEntity<QnaDto>(qnaService.getArticle(articleno), HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<String> modifyArticle(@RequestBody QnaDto qnaDto) throws Exception {
		logger.info("modifyArticle - 호출 {}", qnaDto);
		
		if (qnaService.modifyArticle(qnaDto)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.OK);
	}
	
	@DeleteMapping("/{articleno}")
	public ResponseEntity<String> deleteArticle(@PathVariable("articleno")  int articleno) throws Exception {
		logger.info("deleteArticle - 호출");
		if (qnaService.deleteArticle(articleno)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
}