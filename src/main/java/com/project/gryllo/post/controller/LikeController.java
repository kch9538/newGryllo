package com.project.gryllo.post.controller;

import com.project.gryllo.auth.LoginUser;
import com.project.gryllo.auth.LoginUserAnnotation;
import com.project.gryllo.post.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class LikeController {

	private final LikesService likesService;

	@PostMapping("/likes/{imageId}")
	public ResponseEntity<?> like(@PathVariable int imageId,
		@LoginUserAnnotation LoginUser loginUser) {
		likesService.like(imageId, loginUser.getId());
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@DeleteMapping("/likes/{imageId}")
	public ResponseEntity<?> unLike(@PathVariable int imageId,
		@LoginUserAnnotation LoginUser loginUser) {
		likesService.dislike(imageId, loginUser.getId());
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
}
