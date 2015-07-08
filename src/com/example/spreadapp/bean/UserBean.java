package com.example.spreadapp.bean;

import java.io.Serializable;

public class UserBean implements Serializable {

	private static final long serialVersionUID = 3500764748087650138L;
	private String user_id;
	private String account;
	private String password;
	private String nickname;
	private String age_group;// 0,1,2
	private String gender;// 男0，女1
	private String cellphone;
	private String email;
	private String registe_date;
	private String is_valid = "1";
	private String token;

	public UserBean() {
		super();
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAge_group() {
		return age_group;
	}

	public void setAge_group(String age_group) {
		this.age_group = age_group;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegiste_date() {
		return registe_date;
	}

	public void setRegiste_date(String registe_date) {
		this.registe_date = registe_date;
	}

	public String getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(String is_valid) {
		this.is_valid = is_valid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
