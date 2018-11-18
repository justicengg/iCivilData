package com.novery.stack;

public class NovRestResponse {
	private String code;
	private String msg;
	private Integer restCode;
	private Object data;
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
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Integer getRestCode() {
		return restCode;
	}
	public void setRestCode(Integer restCode) {
		this.restCode = restCode;
	}
}
