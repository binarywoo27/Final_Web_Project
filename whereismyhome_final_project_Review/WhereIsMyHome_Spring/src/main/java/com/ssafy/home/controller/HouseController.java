package com.ssafy.home.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.model.dto.FavoriteDto;
import com.ssafy.home.model.dto.HouseDto;
import com.ssafy.home.model.service.HouseService;

@RestController
@RequestMapping("/house")
public class HouseController {
	private final Logger logger = LoggerFactory.getLogger(HouseController.class);
	
	@Autowired
	private HouseService houseService;
	
	@GetMapping(produces = "application/json;charset=utf-8")
	public ResponseEntity<String> houseList(@RequestParam("LAWD_CD") String lawdCd, @RequestParam("DEAL_YMD") String dealYmd) throws IOException {
		logger.info("sido - 호출");
		String serviceKey = "NxKCBsOBrW5%2BX3g4DKZM%2BOXtNAdNqhWeAlnGbTYTDo6b4U7redi2crXF0%2FWQ40x4vcr3WnFzK9pLr0nsrXNGgg%3D%3D";
		StringBuilder urlBuilder = new StringBuilder(
				"http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev"); /*
																															 */
		urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8")
				+ "=" + serviceKey);
		urlBuilder
				.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 페이지번호 */
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
				+ URLEncoder.encode("100", "UTF-8")); /* 한 페이지 결과 수 */
		urlBuilder.append(
				"&" + URLEncoder.encode("LAWD_CD", "UTF-8") + "=" + URLEncoder.encode(lawdCd, "UTF-8")); /* 지역코드 */
		urlBuilder.append(
				"&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode(dealYmd, "UTF-8")); /* 계약월 */
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		System.out.println("get apt list Response code: " + conn.getResponseCode());
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
//		System.out.println(sb.toString());
		JSONObject json = XML.toJSONObject(sb.toString());
		String jsonStr = json.toString();

		return new ResponseEntity<String>(jsonStr, HttpStatus.OK);
	}
	
	@GetMapping(value="/rank", produces = "application/json;charset=utf-8")
	public ResponseEntity<?> houseRank() throws Exception {
		logger.info("house Rank - 호출");
		
//		System.out.println("house rank");
		
		try {
			return new ResponseEntity<List<HouseDto>>(houseService.getHouseRank(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("서버오류", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(produces = "application/json;charset=utf-8")
	public ResponseEntity<?> houseHit(@RequestBody HouseDto houseDto) throws Exception {
		logger.info("aptHit - 호출");
		
//		System.out.println(houseDto.toString());
//		System.out.println(houseService.searchHouse(houseDto));

		try {
			int result = houseService.searchHouse(houseDto);
//			System.out.println("searchHouse result : " + result);
			if (result > 0) {
				houseService.updateHouseHit(houseDto);
			}
			else {
				houseService.addHouseHit(houseDto);	
			}
			return new ResponseEntity<String>("조회수 추가 성공!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("서버오류", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
