package com.ssafy.home.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.model.dto.FavoriteDto;
import com.ssafy.home.model.dto.UserDto;
import com.ssafy.home.model.service.FavoriteService;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
	private final Logger logger = LoggerFactory.getLogger(FavoriteController.class);
	
	@Autowired
	private FavoriteService favoriteService;

	@GetMapping(produces = "application/json;charset=utf-8")
	public ResponseEntity<?> favoriteList(@RequestParam("id") String id) throws Exception {
		logger.info("favorite list - 호출");
		
//		System.out.println(id);

		try {
			return new ResponseEntity<List<FavoriteDto>>(favoriteService.getFavoriteList(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("서버오류", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping
	public ResponseEntity<?> addFavorite(@RequestBody FavoriteDto favoriteDto) throws Exception {
		logger.info("favorite add - 호출");
		
//		System.out.println(favoriteDto);

		try {
			favoriteService.addFavorite(favoriteDto);
			return new ResponseEntity<String>("관심지역 추가 성공 !", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("서버오류", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping
	public ResponseEntity<?> delFavorite(@RequestParam("id") String id, @RequestParam("gugunCode") String gugunCode) throws Exception {
		logger.info("favorite delete - 호출");
		FavoriteDto favoriteDto = new FavoriteDto();
		favoriteDto.setId(id);
		favoriteDto.setGugunCode(gugunCode);
		
//		System.out.println(favoriteDto);

		try {
			favoriteService.delFavorite(favoriteDto);
			return new ResponseEntity<String>("관심지역 삭제 성공 !", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("서버오류", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
