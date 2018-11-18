package com.novery.stack;

import java.util.List;

public class NovRestObjectInfo  {
	private String cliID;
	private String cliName;
	private String objID;
	private String objName;
	private String objType;
	private int dataTotal;
	private List<NovRestDataItem> lstData;
	public String getObjID() {
		return objID;
	}
	public void setObjID(String objID) {
		this.objID = objID;
	}
	public String getObjName() {
		return objName;
	}
	public void setObjName(String objName) {
		this.objName = objName;
	}
	public List<NovRestDataItem> getLstData() {
		return lstData;
	}
	public void setLstData(List<NovRestDataItem> lstData) {
		this.lstData = lstData;
	}
	public String getObjType() {
		return objType;
	}
	public void setObjType(String objType) {
		this.objType = objType;
	}
	public String getCliID() {
		return cliID;
	}
	public void setCliID(String cliID) {
		this.cliID = cliID;
	}
	public String getCliName() {
		return cliName;
	}
	public void setCliName(String cliName) {
		this.cliName = cliName;
	}
	public int getDataTotal() {
		return dataTotal;
	}
	public void setDataTotal(int dataTotal) {
		this.dataTotal = dataTotal;
	}
	

}
