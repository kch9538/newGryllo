package com.project.gryllo.user.controller;

import com.project.gryllo.auth.LoginUser;
import com.project.gryllo.auth.LoginUserAnnotation;
import com.project.gryllo.user.dto.FollowRespDto;
import com.project.gryllo.user.service.FollowService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class FollowController {

	private final FollowService followService;
	
	@GetMapping("/follow/followingList/{pageUserId}")
	public String followingList(@PathVariable int pageUserId, @LoginUserAnnotation LoginUser loginUser, Model model) {
		model.addAttribute("followingList", followService.followList(loginUser.getId(), pageUserId));
		return "follow/following-list";
	}
	
	@GetMapping("/follow/followerList/{pageUserId}")
	public String followerList(@PathVariable int pageUserId, @LoginUserAnnotation LoginUser loginUser, Model model) {
		model.addAttribute("followerList", followService.followerList(loginUser.getId(), pageUserId));
		return "follow/follower-list";
	}
	
	@GetMapping("test/follow/followingList/{pageUserId}")
	public @ResponseBody List<FollowRespDto> testFollowingList(@PathVariable int pageUserId, @LoginUserAnnotation LoginUser loginUser, Model model) {
		
		return followService.followList(loginUser.getId(), pageUserId);
	}
	
	@PostMapping("/follow/{id}")
	public ResponseEntity<?> follow(@PathVariable int id,
			@LoginUserAnnotation LoginUser loginUser) {
		
		followService.follow(loginUser.getId(), id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@DeleteMapping("/follow/{id}")
	public ResponseEntity<?> unFollow(@PathVariable int id,
			@LoginUserAnnotation LoginUser loginUser) {
		
		followService.unFollow(loginUser.getId(), id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
}
