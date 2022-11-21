package com.project.gryllo.user.dto;


import com.project.gryllo.user.entity.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileRespDto {

	private boolean pageHost; // 페이지 주인 확인
	private boolean followState; // true(팔로우 취소), false(팔로우)
	private User user;
	private List<UserProfileImageRespDto> images;
	private int followerCount;
	private int followingCount;
	private int imageCount;
}
