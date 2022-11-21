package com.project.gryllo.post.controller;

import com.project.gryllo.auth.LoginUser;
import com.project.gryllo.auth.LoginUserAnnotation;
import com.project.gryllo.notification.service.NotiService;
import com.project.gryllo.post.service.ChatService;
import com.project.gryllo.post.service.CommentService;
import com.project.gryllo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class ChatController {

	@Autowired
	private UserService userService;
	@Autowired
	private ChatService chatService;
	
	private final NotiService notiService;

	@GetMapping("/chat")
	public String Chatting(@LoginUserAnnotation LoginUser loginUser, Model model,
			@RequestParam(required = false, defaultValue = "") String searchInput) {
		if (searchInput.equals("")) {
			model.addAttribute("AllUserList", userService.allUsers(loginUser.getId()));
		} else {
			model.addAttribute("AllUserList", userService.dmUser(searchInput, loginUser.getId()));
		}
		model.addAttribute("notis", notiService.notiList(loginUser.getId()));
		return "chat/direct-message";
	}

	@GetMapping("/chat/{SelectedUserId}")
	public String OneToOneChat(@LoginUserAnnotation LoginUser loginUser, @PathVariable int SelectedUserId, Model model,
			@RequestParam(required = false, defaultValue = "") String searchInput) {
		if (searchInput.equals("")) {
			model.addAttribute("AllUserList", userService.allUsers(loginUser.getId()));
		} else {
			model.addAttribute("AllUserList", userService.dmUser(searchInput, loginUser.getId()));
		}
		model.addAttribute("SelectedUser", userService.selectUser(SelectedUserId));
		model.addAttribute("MessageList", chatService.ChattingLoad(loginUser.getId(), SelectedUserId));
		model.addAttribute("notis", notiService.notiList(loginUser.getId()));
		return "chat/direct-message";
	}

	private final CommentService commentService;

	@PostMapping("/chatSave/{SelectedUserId}")
	public ResponseEntity<?> ChattingInsert(@RequestBody String message, @LoginUserAnnotation LoginUser loginUser,
			@PathVariable int SelectedUserId, Model model) {

		System.out.println("ChattingInsert진입 : " + message);
		System.out.println("ChattingInsert진입 : " + loginUser.getId());
		System.out.println("ChattingInsert진입 : " + SelectedUserId);
		chatService.ChattingSave(loginUser.getId(), SelectedUserId, message);
		return new ResponseEntity<String>("ok", HttpStatus.CREATED);
	}

	// test
	@GetMapping("/chatTemplate")
	public String ChatTemplate(@LoginUserAnnotation LoginUser loginUser, Model model) {

		return "chat/chatTemplate";
	}
}
