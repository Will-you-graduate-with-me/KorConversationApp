package com.cookandroid.korconversationapp;

public class UserVO {

	private String user_id;
	private String nickname;
	private int character_id;
	private int age;

	private int stay_duration;
	private int interest;
	private int point;
	private char app_theme;

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getCharacter_id() {
		return character_id;
	}
	public void setCharacter_id(int character_id) {
		this.character_id = character_id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	public int getInterest() {
		return interest;
	}
	public void setInterest(int interest) {
		this.interest = interest;
	}

	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public char getApp_theme() {
		return app_theme;
	}
	public void setApp_theme(char app_theme) {
		this.app_theme = app_theme;
	}
	public int getStay_duration() {
		return stay_duration;
	}
	public void setStay_duration(int stay_duration) {
		this.stay_duration = stay_duration;
	}

}
