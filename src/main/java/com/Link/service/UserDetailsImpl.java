package com.Link.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Link.models.User;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID=1;

	private Long id;
	private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
	

	public UserDetailsImpl(Long id, String username,String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
        this.email=email;
		this.password = password;
		this.authorities = authorities;
	}

	
	public  static UserDetailsImpl build(User user) {
		GrantedAuthority authorities= new SimpleGrantedAuthority(user.getRole()); 
			
			return new UserDetailsImpl(
					user.getId(),
					 user.getUsername(),
                     user.getEmail(),
					 user.getPassword(),
					Collections.singletonList(authorities));
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return authorities;
	}

	@Override
	public String getPassword() {

		return password;
	}

	@Override
	public String getUsername() {
		
		return username;
	}



}
