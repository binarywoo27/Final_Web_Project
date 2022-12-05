package com.ssafy.home.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import com.ssafy.home.model.dto.UserDto;
import com.ssafy.home.model.service.JwtServiceImpl;
import com.ssafy.home.model.service.UserServiceImpl;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private JwtServiceImpl jwtService;

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto userDto) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			UserDto loginUser = userService.login(userDto);
			System.out.println(userDto.getId());
			if (loginUser != null) {
				String accessToken = jwtService.createAccessToken("id", loginUser.getId());
				String refreshToken = jwtService.createRefreshToken("id", loginUser.getId());
				userService.saveRefreshToken(userDto.getId(), refreshToken);
				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			} else {
				System.out.println("정보 조회 실패");
				resultMap.put("message", FAIL);
				status = HttpStatus.ACCEPTED;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("토큰 사용 불가능");
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping("/info/{id}")
	public ResponseEntity<Map<String, Object>> getInfo(@PathVariable("id") String id, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		if (jwtService.checkToken(request.getHeader("access-token"))) {
			System.out.println("사용가능한 토큰입니다");
			try {
				UserDto userDto = userService.userInfo(id);
				resultMap.put("userInfo", userDto);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			} catch (Exception e) {
				System.out.println("사용자 정보 조회 실패" + " " +e);
				resultMap.put("message", FAIL);
				status = HttpStatus.UNAUTHORIZED;
			} 
		} else {
			System.out.println("사용 불가능한 토큰입니다.");
			resultMap.put("message", FAIL);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String,Object>>(resultMap, status);
	}
	
	@GetMapping("/logout/{id}")
	public ResponseEntity<?> removeToken(@PathVariable("id") String id) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			userService.deleteRefreshToken(id);
			resultMap.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("로그아웃 실패");
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody UserDto userDto, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refresh-token");
		if (jwtService.checkToken(token)) {
			if (token.equals(userService.getRefreshToken(userDto.getId()))) {
				String accessToken = jwtService.createAccessToken("id", userDto.getId());
				resultMap.put("access-token", accessToken);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
				System.out.println("액세스 토큰 재발급 완료");
			}
		} else {
			System.out.println("리프레쉬 토큰 사용 불가");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@PostMapping("/regist")
	public ResponseEntity<?> regist(@RequestBody UserDto userDto) {
		try {
			userService.registUser(userDto);
			return new ResponseEntity<String>("회원가입 성공", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestBody UserDto userDto) {
		try {
			userService.updateUser(userDto);
			return new ResponseEntity<String>("회원정보 수정 성공", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("회원정보 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		try {
			userService.deleteUser(id);
			return new ResponseEntity<String>("회원정보 삭제 성공", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("회원정보 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/check/{id}/{phone}")
	public ResponseEntity<?> check(@PathVariable String id, @PathVariable String phone) {
		try {
			UserDto user = userService.checkUser(id, phone);
			System.out.println(user.getId());
			return new ResponseEntity<String>("회원정보 조회 성공", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("회원정보 조회 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/resetPw/{id}/{password}")
	public ResponseEntity<?> reset(@PathVariable String id, @PathVariable String password) {
		try {
			userService.resetPw(id, password);
			return new ResponseEntity<String>("비밀번호 변경 성공", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("비밀번호 변경 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
