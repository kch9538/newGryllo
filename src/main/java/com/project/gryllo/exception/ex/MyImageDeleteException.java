package com.project.gryllo.exception.ex;

public class MyImageDeleteException extends RuntimeException {
	private String message;

	public MyImageDeleteException() {
		this.message = "게시물 작성자만 글을 삭제 할 수 있습니다.";
	}

	public MyImageDeleteException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
