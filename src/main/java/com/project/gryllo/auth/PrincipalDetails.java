package com.project.gryllo.auth;

import com.project.gryllo.user.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class PrincipalDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private User user;
	private Map<String, Object> attributes;

	public PrincipalDetails(User user) {
		this.user = user;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<GrantedAuthority>();
		collection.add(() -> user.getRole().getKey());
		return collection;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
