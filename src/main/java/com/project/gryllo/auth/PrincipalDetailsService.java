package com.project.gryllo.auth;

import com.project.gryllo.user.entity.User;
import com.project.gryllo.user.repository.UserRepository;
import java.util.Optional;
import java.util.function.Function;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(PrincipalDetailsService.class);
	private final UserRepository userRepository;
	private final HttpSession session;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUserByUsername : username : " + username);

		Optional<User> searchNullUser = userRepository.findByUsername(username);
		log.info("searchUser : " + searchNullUser);

		if (searchNullUser.equals(Optional.empty())) {
			throw new UsernameNotFoundException(username + "is not found.");
		}

		User userEntity = userRepository.findByUsername(username).map(new Function<User, User>() {
			@Override
			public User apply(User t) {
				session.setAttribute("loginUser", new LoginUser(t));
				return t;
			}
		}).orElse(null);
		return new PrincipalDetails(userEntity);
	}

}
