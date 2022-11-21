package com.project.gryllo.user.service;


import com.project.gryllo.auth.LoginUser;
import com.project.gryllo.auth.LoginUserAnnotation;
import com.project.gryllo.exception.ex.MyPasswordCheckException;
import com.project.gryllo.exception.ex.MyUserIdNotFoundException;
import com.project.gryllo.exception.ex.MyUserInfoExistException;
import com.project.gryllo.post.entity.Comment;
import com.project.gryllo.post.entity.Image;
import com.project.gryllo.post.entity.Likes;
import com.project.gryllo.post.repository.UserBoardRepository;
import com.project.gryllo.user.dto.JoinReqDto;
import com.project.gryllo.user.dto.UserProfileImageRespDto;
import com.project.gryllo.user.dto.UserProfileRespDto;
import com.project.gryllo.user.entity.User;
import com.project.gryllo.user.repository.FollowRepository;
import com.project.gryllo.user.repository.UserRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UserService {

	private final HttpSession session;

	@PersistenceContext
	private EntityManager em;
	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final UserBoardRepository userboardRepository;

	@Value("${file.path}")
	private String uploadFolder;

	@Transactional
	public void uploadProfileImg(LoginUser loginUser, MultipartFile file) {
		UUID uuid = UUID.randomUUID();
		String imageFilename = uuid + "_" + file.getOriginalFilename();
		Path imageFilepath = Paths.get(uploadFolder + imageFilename);
		try {
			Files.write(imageFilepath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		User userEntity = userRepository.findById(loginUser.getId())
			.orElseThrow(new Supplier<MyUserIdNotFoundException>() {
				@Override
				public MyUserIdNotFoundException get() {
					return new MyUserIdNotFoundException();
				}
			});

		userEntity.setProfileImage(imageFilename);

		loginUser.setImageUrl(imageFilename);
		session.setAttribute("loginUser", loginUser);
	}

	@Transactional
	public void editUser(User user) {
		// 더티 체킹
		User userEntity = userRepository.findById(user.getId())
			.orElseThrow(new Supplier<MyUserIdNotFoundException>() {
				@Override
				public MyUserIdNotFoundException get() {
					return new MyUserIdNotFoundException();
				}
			});
		userEntity.setName(user.getName());
		userEntity.setBio(user.getBio());
	}

	@Transactional(readOnly = false)
	public void pwChange(@LoginUserAnnotation LoginUser loginUser, CharSequence rawPassword,
		CharSequence newPassword,
		CharSequence newRePassword, boolean passwordCK, JoinReqDto joinReqDto)
		throws MyPasswordCheckException {

		String username = joinReqDto.getUsername();

		System.out.println("비밀번호 변경");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		User user = userDetail(loginUser);

		passwordCK = encoder.matches(rawPassword, user.getPassword());
		System.out.println("passwordCK: " + passwordCK);

		if (passwordCK == true) {

			if (newPassword.equals(newRePassword)) {
				System.out.println("변경 할 비밀번호 일치");

				joinReqDto.setPassword(newPassword.toString());

				JoinReqDto modifyUser = new JoinReqDto();
				modifyUser.setUsername(joinReqDto.getUsername());
				System.out.println("username: " + username);
				System.out.println("암호화 전 비밀번호: " + joinReqDto.getPassword());
				newPassword = encoder.encode(joinReqDto.getPassword());
				joinReqDto.setPassword(newPassword.toString());
				System.out.println("암호화 후 비밀번호  : " + joinReqDto.getPassword());
				userRepository.modifyPassword(username, newPassword.toString());
				System.out.println("비밀번호 변경 완료");
			}

		} else {
			System.out.println("oldPassword and DBpassword matches: " + passwordCK);
			System.out.println("비밀번호 변경 실패");
			throw new MyPasswordCheckException("현재 비밀번호를 확인해주세요.");
		}

	}

	@Transactional(readOnly = true)
	public User userDetail(LoginUser loginUser) {
		return userRepository.findById(loginUser.getId())
			.orElseThrow(new Supplier<MyUserIdNotFoundException>() {
				@Override
				public MyUserIdNotFoundException get() {
					return new MyUserIdNotFoundException();
				}
			});
	}

	@Transactional
	public void join(JoinReqDto joinReqDto) throws MyUserInfoExistException {
		System.out.println("서비스 회원가입 들어옴");
		System.out.println(joinReqDto);

		List<User> DuplicateCheckList = userRepository.existCheck(joinReqDto.getEmail(),
			joinReqDto.getUsername());
		System.out.println(DuplicateCheckList.size());
		if (!(DuplicateCheckList.isEmpty())) {
			System.out.println("중복 이메일 또는 아이디");
			throw new MyUserInfoExistException("이미 존재하는 Email 또는 사용자 이름 입니다.");
		} else {
			String encPassword = bCryptPasswordEncoder.encode(joinReqDto.getPassword());
			System.out.println("encPassword : " + encPassword);
			joinReqDto.setPassword(encPassword);
			userRepository.save(joinReqDto.toEntity());
		}
	}

	@Transactional
	public List<Image> oneUserPost(int BoardUserid, int loginUserId) {
		List<Image> boards = null;
		boards = userboardRepository.mUserBoard(BoardUserid);
		for (Image board : boards) {
			board.setLikeCount(board.getLikes().size());

			for (Likes like : board.getLikes()) {
				if (like.getUser().getId() == loginUserId) {
					board.setLikeState(true);
				}
			}
			for (Comment comment : board.getComments()) {
				if (comment.getUser().getId() == loginUserId) {
					comment.setCommentHost(true);
				}
			}
		}
		return boards;
	}
	@Transactional(readOnly = true)
	public UserProfileRespDto userProfile(int id, LoginUser loginUser) {

		int imageCount;
		int followerCount;
		int followingCount;
		boolean followState;

		User userEntity = userRepository.findById(id)
			.orElseThrow(new Supplier<MyUserIdNotFoundException>() {
				@Override
				public MyUserIdNotFoundException get() {
					return new MyUserIdNotFoundException();
				}
			});

		StringBuilder sb = new StringBuilder();
		sb.append("select im.id, im.imageUrl, im.userId,");
		sb.append("(select count(*) from likes lk where lk.imageId = im.id) as likeCount, ");
		sb.append("(select count(*) from comment ct where ct.imageId = im.id) as commentCount ");
		sb.append("from image im where im.userId = ? order by createDate desc");
		String q = sb.toString();
		Query query = em.createNativeQuery(q, "UserProfileImageRespDtoMapping").setParameter(1, id);
		List<UserProfileImageRespDto> imagesEntity = query.getResultList();

		imageCount = imagesEntity.size();


		followerCount = followRepository.mCountByFollower(id);
		followingCount = followRepository.mCountByFollowing(id);

		followState = followRepository.mFollowState(loginUser.getId(), id) == 1 ? true : false;

		UserProfileRespDto userProfileRespDto = UserProfileRespDto.builder()
			.pageHost(id == loginUser.getId())
			.followState(followState).followerCount(followerCount).followingCount(followingCount)
			.imageCount(imageCount).user(userEntity).images(imagesEntity)
			.build();

		return userProfileRespDto;
	}
	@Transactional(readOnly = true)
	public List<User> recommendedUser(int loginUserId) {
		return userRepository.mRecommendationImage(loginUserId);
	}

	// DM 전체 회원
	@Transactional(readOnly = true)
	public List<User> allUsers(int loginUserId) {
		return userRepository.mAllUserList(loginUserId);
	}

	// DM 특정 회원
	@Transactional(readOnly = true)
	public List<User> dmUser(String username, int id) {
		username = "%" + username + "%";
		return userRepository.mSearchUserList(username, id);
	}

	// DM 특정 회원 불러오기
	@Transactional(readOnly = true)
	public User selectUser(int selectedUserId) {
		return userRepository.mSelectedUser(selectedUserId);
	}

}
