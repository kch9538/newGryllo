package com.project.gryllo.exception.ex;

public class MyPasswordCheckException extends RuntimeException {
	private String message;

	public MyPasswordCheckException() {
		this.message = "기존 비밀번호가 틀렸습니다.";
	}

	public MyPasswordCheckException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}