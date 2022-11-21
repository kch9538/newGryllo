package com.project.gryllo.post.controller;

import com.project.gryllo.post.dto.CommentRespDto;
import com.project.gryllo.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class CommentController {

	private final CommentService commentService;
	
	@PostMapping("/comment")
	public ResponseEntity<?> comment(CommentRespDto commentRespDto) {
		System.out.println("commentRespDto : "+commentRespDto);
		commentService.comment(commentRespDto);
		return new ResponseEntity<String>("ok", HttpStatus.CREATED);
	}
	
	@DeleteMapping("/comment/{id}")
	public ResponseEntity<?> commentDelete(@PathVariable int id) {
		commentService.delComment(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
}



