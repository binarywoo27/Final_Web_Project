package com.ssafy.home.model.dto;

public class UserDto {
	private String id;
	private String password;
	private String name;
	private String address;
	private String phone;

	public UserDto() {
		super();
	}

	public UserDto(String id, String password, String name, String address, String phone) {
		super();
		this.id = id;
		this.password = password;
		this.name = name;
		this.address = address;
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", password=" + password + ", name=" + name + ", address=" + address + ", phone="
				+ phone + "]";
	}

}
