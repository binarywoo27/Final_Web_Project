package com.ssafy.home.model.dto;

public class FavoriteDto {
	String sidoCode;
	String gugunCode;
	String id;
	String sido;
	String gugun;

	public FavoriteDto() {
		super();
	}

	public FavoriteDto(String sidoCode, String gugunCode, String id, String sido, String gugun) {
		super();
		this.sidoCode = sidoCode;
		this.gugunCode = gugunCode;
		this.id = id;
		this.sido = sido;
		this.gugun = gugun;
	}

	public String getSidoCode() {
		return sidoCode;
	}

	public void setSidoCode(String sidoCode) {
		this.sidoCode = sidoCode;
	}

	public String getGugunCode() {
		return gugunCode;
	}

	public void setGugunCode(String gugunCode) {
		this.gugunCode = gugunCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSido() {
		return sido;
	}

	public void setSido(String sido) {
		this.sido = sido;
	}

	public String getGugun() {
		return gugun;
	}

	public void setGugun(String gugun) {
		this.gugun = gugun;
	}

	@Override
	public String toString() {
		return "FavoriteDto [sidoCode=" + sidoCode + ", gugunCode=" + gugunCode + ", id=" + id + ", sido=" + sido
				+ ", gugun=" + gugun + "]";
	}

}
