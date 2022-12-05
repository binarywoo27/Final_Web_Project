package com.ssafy.home.model.service;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ssafy.home.exception.UnAuthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtServiceImpl implements JwtService {
	private static final String SALT = "hhSecret";
	private static final int ACCESS_TOKEN_EXPIRE_MINUTES = 1; // 1분
	private static final int REFRESH_TOKEN_EXPIRE_WEEKS = 2; // 2주

	@Override
	public <T> String createAccessToken(String key, T data) {
		return create(key, data, "access-token", 1000 * 60 * ACCESS_TOKEN_EXPIRE_MINUTES);
	}

	@Override
	public <T> String createRefreshToken(String key, T data) {
		return create(key, data, "refresh-token", 1000 * 60 * 60 * 24 * 7 * REFRESH_TOKEN_EXPIRE_WEEKS);
	}

	@Override
	public <T> String create(String key, T data, String subject, long expire) {
		String jwt = Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("regDate", System.currentTimeMillis())
				.setExpiration(new Date(System.currentTimeMillis() + expire))
				.setSubject(subject)
				.claim(key, data)
				.signWith(SignatureAlgorithm.HS256, this.generateKey())
				.compact();
		return jwt;
	}
	
	private byte[] generateKey() {
		byte[] key = null;
		try {
			key = SALT.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return key;
	}

	@Override
	public Map<String, Object> get(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String jwt = request.getHeader("access-token");
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser().setSigningKey(SALT.getBytes("UTF-8")).parseClaimsJws(jwt);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnAuthorizedException();
		}
		Map<String, Object> value = claims.getBody();
		return value;
	}

	@Override
	public String getUserId() {
		return (String) this.get("user").get("userid");
	}

	@Override
	public boolean checkToken(String jwt) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(jwt);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	} 

}
