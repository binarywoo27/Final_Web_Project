package com.ssafy.home.model.dto;


public class QnaDto {
	private int articleno;
	private String userid;
	private String subject;
	private String content;
	private String filename;
	private String dbfilename;
	private int hit;
	private String regtime;

	public int getArticleno() {
		return articleno;
	}

	public void setArticleno(int articleno) {
		this.articleno = articleno;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDbfilename() {
		return dbfilename;
	}

	public void setDbfilename(String dbfilename) {
		this.dbfilename = dbfilename;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getRegtime() {
		return regtime;
	}

	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}

	@Override
	public String toString() {
		return "BoardDto [articleno=" + articleno + ", userid=" + userid + ", subject=" + subject + ", content="
				+ content + ", hit=" + hit + ", regtime=" + regtime + "]";
	}

}