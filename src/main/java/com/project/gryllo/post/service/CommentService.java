package com.project.gryllo.post.service;

import com.project.gryllo.exception.ex.MyImageIdNotFoundException;
import com.project.gryllo.notification.entity.NotiType;
import com.project.gryllo.notification.repository.NotiRepository;
import com.project.gryllo.post.dto.CommentRespDto;
import com.project.gryllo.post.entity.Image;
import com.project.gryllo.post.repository.CommentRepository;
import com.project.gryllo.post.repository.ImageRepository;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
	private final CommentRepository commentRepository;
	private final NotiRepository notiRepository;
	private final ImageRepository imageRepository;
	
	@Transactional
	public void comment(CommentRespDto commentRespDto) {
		commentRepository.mSave( 
				commentRespDto.getUserId(), 
				commentRespDto.getImageId(), 
				commentRespDto.getContent());
		Image imageEntity = imageRepository.findById(commentRespDto.getImageId()).orElseThrow(new Supplier<MyImageIdNotFoundException>() {
			@Override
			public MyImageIdNotFoundException get() {
				return new MyImageIdNotFoundException();
			}
		});
		notiRepository.mSave(commentRespDto.getUserId(), imageEntity.getUser().getId(), NotiType.COMMENT.name());
	}
	
	@Transactional
	public void delComment(int id) {
		commentRepository.deleteById(id);
	}
}
