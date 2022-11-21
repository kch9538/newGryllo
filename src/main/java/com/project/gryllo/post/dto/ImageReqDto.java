package com.project.gryllo.post.dto;

import com.project.gryllo.post.entity.Image;
import com.project.gryllo.user.entity.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageReqDto {
	private MultipartFile file;
	private String caption;
	private String location;
	private String tags;
	
	
	public Image toEntity(String imageUrl, User userEntity) {
		return Image.builder()
				.location(location)
				.caption(caption)
				.imageUrl(imageUrl)
				.user(userEntity)
				.build();
	}
}





