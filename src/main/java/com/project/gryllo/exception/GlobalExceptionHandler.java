package com.project.gryllo.exception;


import com.project.gryllo.exception.ex.MyImageDeleteException;
import com.project.gryllo.exception.ex.MyImageIdNotFoundException;
import com.project.gryllo.exception.ex.MyPasswordCheckException;
import com.project.gryllo.exception.ex.MyUserIdNotFoundException;
import com.project.gryllo.exception.ex.MyUserInfoExistException;
import com.project.gryllo.exception.ex.MyUsernameNotFoundException;
import com.project.gryllo.util.Script;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = MyUserIdNotFoundException.class)
	public String myUserIdNotFoundException(Exception e) {
		return Script.back(e.getMessage());
	}

	@ExceptionHandler(value = MyUsernameNotFoundException.class)
	public String myUsernameNotFoundException(Exception e) {
		return Script.alert(e.getMessage());
	}

	@ExceptionHandler(value = MyImageIdNotFoundException.class)
	public String myImageIdNotFoundException(Exception e) {
		return Script.alert(e.getMessage());
	}

	@ExceptionHandler(value = IllegalArgumentException.class)
	public String myIllegalArgumentException(Exception e) {
		return Script.alert(e.getMessage());
	}

	// 회원가입 중복체크 에러 핸들러
	@ExceptionHandler(value = MyUserInfoExistException.class)
	public String MyUserInfoExistException(Exception e) {
		return Script.back(e.getMessage());
	}

	// 게시물 삭제 시도시 작성자가 아니면 삭제할수 없음.
	@ExceptionHandler(value = MyImageDeleteException.class)
	public String MyImageDeleteException(Exception e) {
		return (e.getMessage());
	}

	// 현재 비밀번호를 확인
	@ExceptionHandler(value = MyPasswordCheckException.class)
	public String MyPasswordCheckException(Exception e) {
		return Script.back(e.getMessage());
	}
}
