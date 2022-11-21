package com.project.gryllo.user.controller;


import com.project.gryllo.auth.LoginUser;
import com.project.gryllo.auth.LoginUserAnnotation;
import com.project.gryllo.user.dto.JoinReqDto;
import com.project.gryllo.user.entity.User;
import com.project.gryllo.user.service.UserService;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	private final UserService userService;

	@GetMapping("/auth/loginForm")
	public String loginForm() {
		log.info("/auth/loginForm");
		return "auth/loginForm";
	}

	@GetMapping("/auth/joinForm")
	public String joinForm() {
		log.info("/auth/joinForm");
		return "auth/joinForm";
	}

	// 회원가입 처리
	@PostMapping("/auth/join")
	public String join(JoinReqDto joinReqDto) {
		log.info(joinReqDto.toString());
		userService.join(joinReqDto);
		return "redirect:/auth/loginForm";
	}

	// 비밀번호 변경
	@GetMapping("/auth/pwChange")
	public void pwChange(String oldPassword, boolean passwordCK, @LoginUserAnnotation LoginUser loginUser, Model model) {
		log.info("/auth/pwChange");
		User userEntity = userService.userDetail(loginUser);
		model.addAttribute("user", userEntity);

	}

	// 비밀번호 변경 요청
	@PostMapping("/auth/pwChange")
	public String pwChange(@LoginUserAnnotation LoginUser loginUser, CharSequence oldPassword, CharSequence newPassword,
			CharSequence newRePassword, boolean passwordCK, JoinReqDto joinReqDto, HttpSession session, Model model) {
		String result = null;
		log.info("비밀번호 변경 요청", joinReqDto.toString());

		userService.pwChange(loginUser, oldPassword, newPassword, newRePassword, passwordCK, joinReqDto);

		return "redirect:/auth/logout";
	}
}
