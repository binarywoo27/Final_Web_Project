package com.ssafy.home.model.dto;

public class HouseDto {
	String roadCode;
	String sidoName;
	String gugunName;
	String houseName;
	int hit;

	public HouseDto() {
		super();
	}

	public HouseDto(String roadCode, String sidoName, String gugunName, String houseName, int hit) {
		super();
		this.roadCode = roadCode;
		this.sidoName = sidoName;
		this.gugunName = gugunName;
		this.houseName = houseName;
		this.hit = hit;
	}

	public String getRoadCode() {
		return roadCode;
	}

	public void setRoadCode(String roadCode) {
		this.roadCode = roadCode;
	}

	public String getSidoName() {
		return sidoName;
	}

	public void setSidoName(String sidoName) {
		this.sidoName = sidoName;
	}

	public String getGugunName() {
		return gugunName;
	}

	public void setGugunName(String gugunName) {
		this.gugunName = gugunName;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	@Override
	public String toString() {
		return "HouseDto [roadCode=" + roadCode + ", sidoName=" + sidoName + ", gugunName=" + gugunName + ", houseName="
				+ houseName + ", hit=" + hit + "]";
	}

}
