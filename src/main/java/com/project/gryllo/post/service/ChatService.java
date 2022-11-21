package com.project.gryllo.post.service;


import com.project.gryllo.post.entity.Chatting;
import com.project.gryllo.post.repository.ChattingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatService {
	private final ChattingRepository chattingRepository;

	@Transactional
	public void ChattingSave(int fromUserId, int toUserId, String message) {
		int result = chattingRepository.mChatSave(fromUserId, toUserId, message);
		System.out.println("대화 저장 : " + result);
	}

	@Transactional
	public List<Chatting> ChattingLoad(int fromUserId, int toUserId) {
		List<Chatting> result = chattingRepository.mChatList(fromUserId, toUserId);
		System.out.println("불러온대화 : " + result.size());
		return result;

	}
}
