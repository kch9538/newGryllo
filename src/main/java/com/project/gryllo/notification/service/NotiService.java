package com.project.gryllo.notification.service;


import com.project.gryllo.notification.entity.Noti;
import com.project.gryllo.notification.repository.NotiRepository;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotiService {
	private final NotiRepository notiRepository;

	HttpServletRequest req;

	@Autowired
	private HttpSession session;


	@Transactional(readOnly = true)
	public List<Noti> notiList(int loginUserId) {
		List<Noti> Noti = notiRepository.mNotiForHeader(loginUserId);

		session.setAttribute("staticNoti", Noti);
		return Noti;
	}

}
