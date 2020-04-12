package com.cookandroid.korconversationapp;

public class PartInfoVO {	
	
	private int part_no;
	private String part_name;
	private int unit_cnt;
	
	public int getPart_no() {
		return part_no;
	}
	public void setPart_no(int part_no) {
		this.part_no = part_no;
	}
	public String getPart_name() {
		return part_name;
	}
	public void setPart_name(String part_name) {
		this.part_name = part_name;
	}
	public int getUnit_cnt() {
		return unit_cnt;
	}
	public void setUnit_cnt(int unit_cnt) {
		this.unit_cnt = unit_cnt;
	}

	public PartInfoVO(int part_no, String part_name, int unit_cnt) {
		this.part_no = part_no;
		this.part_name = part_name;
		this.unit_cnt = unit_cnt;
	}
}
