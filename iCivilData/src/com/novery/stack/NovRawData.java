package com.novery.stack;

import java.util.Date;

public class NovRawData {
/*
  `rawID` varchar(36) NOT NULL,
  `rawNo` int(11) DEFAULT '0' COMMENT '�������к�',
  `rawProtocol` int(11) DEFAULT '0',
  `rawSize` int(11) DEFAULT '0',
  `rawStationCode` int(11) DEFAULT '0' COMMENT '��վ���',
  `rawCommandCode` int(11) DEFAULT '0' COMMENT '������',
  `rawNodeCode` int(11) DEFAULT '0' COMMENT '�ڵ���',
  `rawSensorCount` int(11) DEFAULT '0' COMMENT '�ڵ���',
  `rawSensorData` varchar(1024) DEFAULT '',
  `rawContent` varchar(1024) DEFAULT '' COMMENT 'ԭʼ��������',
  `rawCreated` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `rawState` int(11) DEFAULT '0',
  `rawSocket` int(11) DEFAULT '0',
  `rawIP` varchar(16) DEFAULT '' COMMENT '��վ��IP��ַ',
  `rawPort` int(11) DEFAULT '0' COMMENT '�������ݵĶ˿�',
  `rawClientIP` varchar(45) DEFAULT '',
  `rawClientPort` int(11) DEFAULT '0',
 * */
	private String 	rawID;//����
	private Integer 	rawNo;//�������к�
	private Integer 	rawProtocol;
    private 	Integer rawSize;
    private Integer 	rawStationCode;//��վ���
    private Integer 	rawCommandCode;//������
    private Integer 	rawNodeCode;//�ڵ���
    private Integer 	rawSensorCount;
    private String 	rawSensorData;//�����������
    private String 	rawContent;//ԭʼ��������
    private Date 	rawCreated;//����ʱ��  
    private Integer 	rawState;  
    private Integer 	rawSocket;    
    private String 	rawIP;//��վ��IP��ַ
    private Integer 	rawPort;//�������ݵĶ˿�
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
