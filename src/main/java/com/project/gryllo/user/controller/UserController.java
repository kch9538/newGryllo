package com.project.gryllo.user.controller;


import com.project.gryllo.auth.LoginUser;
import com.project.gryllo.auth.LoginUserAnnotation;
import com.project.gryllo.notification.service.NotiService;
import com.project.gryllo.post.entity.Image;
import com.project.gryllo.post.entity.Message;
import com.project.gryllo.user.dto.UserProfileRespDto;
import com.project.gryllo.user.entity.User;
import com.project.gryllo.user.service.FollowService;
import com.project.gryllo.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private FollowService followService;
	
	private final NotiService notiService;

	@GetMapping("/user/{pageUserId}")
	public String profile(@PathVariable int pageUserId, @LoginUserAnnotation LoginUser loginUser, Model model) {
		UserProfileRespDto userProfileRespDto = userService.userProfile(pageUserId, loginUser);
		model.addAttribute("respDto", userProfileRespDto);
		model.addAttribute("followingList", followService.followList(loginUser.getId(), pageUserId));
		model.addAttribute("followerList", followService.followerList(loginUser.getId(), pageUserId));
		model.addAttribute("notis", notiService.notiList(loginUser.getId()));

		List<Image> UserBoard = userService.oneUserPost(pageUserId, loginUser.getId());
		model.addAttribute("board", UserBoard);
		return "user/profile";
	}

	@GetMapping("/user/profileEdit")
	public String profileEdit(@LoginUserAnnotation LoginUser loginUser, Model model) {
		User userEntity = userService.userDetail(loginUser);
		model.addAttribute("respDto",userService.userProfile(loginUser.getId(), loginUser));
		model.addAttribute("user", userEntity);
		model.addAttribute("notis", notiService.notiList(loginUser.getId()));
		return "user/profile-edit";
	}
	
	@PostMapping("/user/profileEditUpload")
	public String profileEdit(@RequestParam("profileImage") MultipartFile file, int userId,
			@LoginUserAnnotation LoginUser loginUser) {
		if (userId == loginUser.getId()) {
			userService.uploadProfileImg(loginUser, file);
		}

		return "redirect:/user/profile-edit";
	}

	@PutMapping("/user")
	public ResponseEntity<?> userUpdate(User user) {
		userService.editUser(user);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	//Post로 간단히 구현
	@PostMapping("/user/profileUpload")
	public String userProfileUpload(@RequestParam("profileImage") MultipartFile file, int userId,
			@LoginUserAnnotation LoginUser loginUser) {
		if (userId == loginUser.getId()) {
			userService.uploadProfileImg(loginUser, file);
		}

		return "redirect:/user/" + userId;
	}
	@MessageMapping("/topic/user/{userid}")
	@SendTo("/topic/user/{userid}")
	public Message sendToUser(@DestinationVariable String userid, Message message) {
		System.out.println(message.toString());
		Message msg = new Message(message.getId(), message.getMessage());
		return msg;
	}


}
