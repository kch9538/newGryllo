package com.project.gryllo.post.service;


import com.project.gryllo.exception.ex.MyImageIdNotFoundException;
import com.project.gryllo.notification.entity.NotiType;
import com.project.gryllo.notification.repository.NotiRepository;
import com.project.gryllo.post.entity.Image;
import com.project.gryllo.post.repository.ImageRepository;
import com.project.gryllo.post.repository.LikesRepository;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikesService {

	private final LikesRepository likesRepository;
	private final NotiRepository notiRepository;
	private final ImageRepository imageRepository;

	@Transactional
	public void like(int imageId, int loginUserId) {
		likesRepository.mSave(imageId, loginUserId);
		Image imageEntity = imageRepository.findById(imageId)
			.orElseThrow(new Supplier<MyImageIdNotFoundException>() {
				@Override
				public MyImageIdNotFoundException get() {
					return new MyImageIdNotFoundException();
				}
			});
		notiRepository.mSave(loginUserId, imageEntity.getUser().getId(), NotiType.LIKE.name());
	}

	@Transactional
	public void dislike(int imageId, int loginUserId) {
		likesRepository.mDelete(imageId, loginUserId);
	}
}
