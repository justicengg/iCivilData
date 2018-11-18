package com.novery.stack;

import java.util.Date;

public class NovRawData {
/*
  `rawID` varchar(36) NOT NULL,
  `rawNo` int(11) DEFAULT '0' COMMENT '数据序列号',
  `rawProtocol` int(11) DEFAULT '0',
  `rawSize` int(11) DEFAULT '0',
  `rawStationCode` int(11) DEFAULT '0' COMMENT '基站编号',
  `rawCommandCode` int(11) DEFAULT '0' COMMENT '命令字',
  `rawNodeCode` int(11) DEFAULT '0' COMMENT '节点编号',
  `rawSensorCount` int(11) DEFAULT '0' COMMENT '节点编号',
  `rawSensorData` varchar(1024) DEFAULT '',
  `rawContent` varchar(1024) DEFAULT '' COMMENT '原始数据内容',
  `rawCreated` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '接收时间',
  `rawState` int(11) DEFAULT '0',
  `rawSocket` int(11) DEFAULT '0',
  `rawIP` varchar(16) DEFAULT '' COMMENT '基站的IP地址',
  `rawPort` int(11) DEFAULT '0' COMMENT '接收数据的端口',
  `rawClientIP` varchar(45) DEFAULT '',
  `rawClientPort` int(11) DEFAULT '0',
 * */
	private String 	rawID;//主键
	private Integer 	rawNo;//数据序列号
	private Integer 	rawProtocol;
    private 	Integer rawSize;
    private Integer 	rawStationCode;//基站编号
    private Integer 	rawCommandCode;//命令字
    private Integer 	rawNodeCode;//节点编号
    private Integer 	rawSensorCount;
    private String 	rawSensorData;//解析后的数据
    private String 	rawContent;//原始数据内容
    private Date 	rawCreated;//接收时间  
    private Integer 	rawState;  
    private Integer 	rawSocket;    
    private String 	rawIP;//基站的IP地址
    private Integer 	rawPort;//接收数据的端口
    private String 	rawClientIP;
    private String 	rawClientPort;
    
    
	public String getRawID() {
		return rawID;
	}
	public void setRawID(String rawID) {
		this.rawID = rawID;
	}
	public Integer getRawNo() {
		return rawNo;
	}
	public void setRawNo(Integer rawNo) {
		this.rawNo = rawNo;
	}
	public Integer getRawProtocol() {
		return rawProtocol;
	}
	public void setRawProtocol(Integer rawProtocol) {
		this.rawProtocol = rawProtocol;
	}
	public Integer getRawSize() {
		return rawSize;
	}
	public void setRawSize(Integer rawSize) {
		this.rawSize = rawSize;
	}
	public Integer getRawStationCode() {
		return rawStationCode;
	}
	public void setRawStationCode(Integer rawStationCode) {
		this.rawStationCode = rawStationCode;
	}
	public Integer getRawCommandCode() {
		return rawCommandCode;
	}
	public void setRawCommandCode(Integer rawCommandCode) {
		this.rawCommandCode = rawCommandCode;
	}
	public Integer getRawNodeCode() {
		return rawNodeCode;
	}
	public void setRawNodeCode(Integer rawNodeCode) {
		this.rawNodeCode = rawNodeCode;
	}
	public Integer getRawSensorCount() {
		return rawSensorCount;
	}
	public void setRawSensorCount(Integer rawSensorCount) {
		this.rawSensorCount = rawSensorCount;
	}
	public String getRawSensorData() {
		return rawSensorData;
	}
	public void setRawSensorData(String rawSensorData) {
		this.rawSensorData = rawSensorData;
	}
	public String getRawContent() {
		return rawContent;
	}
	public void setRawContent(String rawContent) {
		this.rawContent = rawContent;
	}
	public Date getRawCreated() {
		return rawCreated;
	}
	public void setRawCreated(Date rawCreated) {
		this.rawCreated = rawCreated;
	}
	public Integer getRawState() {
		return rawState;
	}
	public void setRawState(Integer rawState) {
		this.rawState = rawState;
	}
	public Integer getRawSocket() {
		return rawSocket;
	}
	public void setRawSocket(Integer rawSocket) {
		this.rawSocket = rawSocket;
	}
	public String getRawIP() {
		return rawIP;
	}
	public void setRawIP(String rawIP) {
		this.rawIP = rawIP;
	}
	public Integer getRawPort() {
		return rawPort;
	}
	public void setRawPort(Integer rawPort) {
		this.rawPort = rawPort;
	}
	public String getRawClientIP() {
		return rawClientIP;
	}
	public void setRawClientIP(String rawClientIP) {
		this.rawClientIP = rawClientIP;
	}
	public String getRawClientPort() {
		return rawClientPort;
	}
	public void setRawClientPort(String rawClientPort) {
		this.rawClientPort = rawClientPort;
	}
    

}
