package com.project.gryllo.post.service;

import com.project.gryllo.exception.ex.MyImageDeleteException;
import com.project.gryllo.post.dto.ImageReqDto;
import com.project.gryllo.post.entity.Comment;
import com.project.gryllo.post.entity.Image;
import com.project.gryllo.post.entity.Likes;
import com.project.gryllo.post.entity.Tag;
import com.project.gryllo.post.repository.ImageRepository;
import com.project.gryllo.post.repository.TagRepository;
import com.project.gryllo.user.dto.UserProfileImageRespDto;
import com.project.gryllo.user.entity.Follow;
import com.project.gryllo.user.entity.User;
import com.project.gryllo.user.repository.FollowRepository;
import com.project.gryllo.user.repository.UserRepository;
import com.project.gryllo.util.Utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ImageService {
	@PersistenceContext
	private EntityManager em;
	private final ImageRepository imageRepository;
	private final TagRepository tagRepository;
	private final UserRepository userRepository;
	private final FollowRepository followRepository;

	@Transactional(readOnly = true)
	public List<Image> postImage(int loginUserId, String tag) {
		List<Image> images = null;
		if (tag == null || tag.equals("")) {
			images = imageRepository.mFeeds(loginUserId);
			if (images.size() == 0) {
				images = imageRepository.mAllFeeds(loginUserId);
			}
		} else {
			tag="%"+tag+"%";
			images = imageRepository.mFeeds(tag);
		}

		for (Image image : images) {
			image.setLikeCount(image.getLikes().size());

			for (Likes like : image.getLikes()) {
				if (like.getUser().getId() == loginUserId) {
					image.setLikeState(true);
				}
			}
			for (Comment comment : image.getComments()) {
				if (comment.getUser().getId() == loginUserId) {
					comment.setCommentHost(true);
				}
			}
		}

		return images;
	}

	@Transactional
	public void delPost(int imageid, int ImageUserId, int loginuserid) throws MyImageDeleteException {

		if (ImageUserId != loginuserid) {
			throw new MyImageDeleteException("게시물 작성자만 글을 삭제 할 수 있습니다.");
		} else {
			imageRepository.deleteById(imageid);
		}
	}

	@Transactional(readOnly = true)
	public List<Image> post(int loginUserId, int imageId) {
		List<Image> boards = null;
		boards = imageRepository.mBoardImage(imageId);

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
	public List<UserProfileImageRespDto> mostLiked(int loginUserId) {

		StringBuilder sb = new StringBuilder();
		sb.append("select im.id, im.imageUrl, im.userId, ");
		sb.append("(select count(*) from likes lk where lk.imageId = im.id) as likeCount, ");
		sb.append("(select count(*) from comment ct where ct.imageId = im.id) as commentCount ");
		sb.append("from image im where im.userId != ? ");
		String q = sb.toString();
		Query query = em.createNativeQuery(q, "UserProfileImageRespDtoMapping").setParameter(1, loginUserId);
		List<UserProfileImageRespDto> imagesEntity = query.getResultList();

		List<Follow> LoginUserFollowingList = followRepository.findByfromUserId(loginUserId);

		for (Follow asdf : LoginUserFollowingList) {
			for (int i = 0; i < imagesEntity.size(); i++) {
				if (imagesEntity.get(i).getUserId() == asdf.getToUser().getId()) {
					imagesEntity.remove(i);
				}
			}
		}

		return imagesEntity;
	}

	@Value("${file.path}")
	private String uploadFolder;

	@Transactional
	public void uploadImage(ImageReqDto imageReqDto, int userId) {
		User userEntity = userRepository.findById(userId).orElseThrow(null);

		UUID uuid = UUID.randomUUID();
		String imageFilename = uuid + "_" + imageReqDto.getFile().getOriginalFilename();
		Path imageFilepath = Paths.get(uploadFolder + imageFilename);
		try {
			Files.write(imageFilepath, imageReqDto.getFile().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		Image image = imageReqDto.toEntity(imageFilename, userEntity);
		Image imageEntity = imageRepository.save(image);

		List<String> tagNames = Utils.tagParse(imageReqDto.getTags());
		for (String name : tagNames) {
			Tag tag = Tag.builder().image(imageEntity).name(name).build();
			tagRepository.save(tag);
		}
	}
}
