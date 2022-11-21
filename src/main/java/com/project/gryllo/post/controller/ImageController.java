package com.project.gryllo.post.controller;

import com.project.gryllo.auth.LoginUser;
import com.project.gryllo.auth.LoginUserAnnotation;
import com.project.gryllo.notification.service.NotiService;
import com.project.gryllo.post.dto.ImageReqDto;
import com.project.gryllo.post.entity.Image;
import com.project.gryllo.post.service.ImageService;
import com.project.gryllo.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ImageController {

	private final ImageService imageService;
	@Autowired
	private UserService userService;

	private final NotiService notiService;

	@GetMapping({ "", "/", "/image/feed" })
	public String feed(String tag, @LoginUserAnnotation LoginUser loginUser, Model model) {
		System.out.println("loginUser : " + loginUser);
		model.addAttribute("images", imageService.postImage(loginUser.getId(), tag));
		model.addAttribute("recommendation", userService.recommendedUser(loginUser.getId()));
		model.addAttribute("notis", notiService.notiList(loginUser.getId()));

		return "image/feed";
	}

	@GetMapping("/test/image/feed")
	public @ResponseBody List<Image> testFeed(String tag, @LoginUserAnnotation LoginUser loginUser) {
		return imageService.postImage(loginUser.getId(), tag);
	}

	@GetMapping("/image/uploadForm")
	public String imageUploadForm() {
		return "image/image-upload";
	}

	@PostMapping("/image")
	public String imageUpload(@LoginUserAnnotation LoginUser loginUser, ImageReqDto imageReqDto) {

		imageService.uploadImage(imageReqDto, loginUser.getId());

		return "redirect:/user/" + loginUser.getId();
	}

	@GetMapping("/image/explore")
	public String imageExplore(@LoginUserAnnotation LoginUser loginUser, Model model) {
		model.addAttribute("images", imageService.mostLiked(loginUser.getId()));
		model.addAttribute("notis", notiService.notiList(loginUser.getId()));
		return "image/explore";
	}

	// 단독게시물 데이터 가져오는부분
	@GetMapping("image/{imageid}")
	public String board(Model model, @PathVariable int imageid, @LoginUserAnnotation LoginUser loginUser) {
		model.addAttribute("board", imageService.post(loginUser.getId(), imageid));
		model.addAttribute("notis", notiService.notiList(loginUser.getId()));
		return "image/board";
	}

	// 특정 게시물 삭제기능
	@DeleteMapping("image/{imageid}/{ImageUserId}")
	public ResponseEntity<?> boardDelete(Model model, @PathVariable int imageid, @PathVariable int ImageUserId,
			@LoginUserAnnotation LoginUser loginUser) {
		imageService.delPost(imageid, ImageUserId, loginUser.getId());

		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

}
