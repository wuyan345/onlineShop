package com.shop.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Message <T> {

	private String msg;
	private int status;
	private T data;
	
	private Message(int status) {
		super();
		this.status = status;
	}
	private Message(String msg, int status) {
		super();
		this.msg = msg;
		this.status = status;
	}
	private Message(T data, int status) {
		super();
		this.status = status;
		this.data = data;
	}
	private Message(String msg, int status, T data) {
		super();
		this.msg = msg;
		this.status = status;
		this.data = data;
	}
	
	public T getData() {
		return data;
	}
	public String getMsg() {
		return msg;
	}
	public int getStatus() {
		return status;
	}
	
	// json里不会出现"success : true"或"success : false"
	@JsonIgnore
	public boolean isSuccess(){
		if(status == Const.SUCCESS)
			return true;
		return false;
	}
	
	public static <T> Message<T> success(){
		return new Message<T>(Const.SUCCESS);
	}
	public static <T> Message<T> successMsg(String msg){
		return new Message<T>(msg, Const.SUCCESS);
	}
	public static <T> Message<T> successData(T data){
		return new Message<T>(data, Const.SUCCESS);
	}
	public static <T> Message<T> successMsgData(String msg, T data){
		return new Message<T>(msg, Const.SUCCESS, data);
	}
	
	public static <T> Message<T> errorMsg(String msg){
		return new Message<T>(msg, Const.FAILED);
	}
	public static <T> Message<T> errorMsgData(String msg, T data){
		return new Message<T>(msg, Const.FAILED, data);
	}
}
