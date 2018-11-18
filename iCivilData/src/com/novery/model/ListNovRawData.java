package com.novery.model;

import java.util.ArrayList;
import java.util.List;

public class ListNovRawData {
	private List<NovRawData> lstData;
	public ListNovRawData(){
		lstData = new ArrayList<NovRawData> ();
	}
	public List<NovRawData> getLstData() {
		return lstData;
	}

	public void setLstData(List<NovRawData> lstData) {
		this.lstData = lstData;
	}
}
