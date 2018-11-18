package com.novery.stack;

public class NovRestResponseObjectInfo {

	public NovRestResponseObjectInfo() {
		// TODO Auto-generated constructor stub
	}
	private String code;
	private String msg;
	private Integer restCode;
	private NovRestObjectInfo data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public NovRestObjectInfo getData() {
		return data;
	}
	public void setData(NovRestObjectInfo data) {
		this.data = data;
	}
	public Integer getRestCode() {
		return restCode;
	}
	public void setRestCode(Integer restCode) {
		this.restCode = restCode;
	}
}
