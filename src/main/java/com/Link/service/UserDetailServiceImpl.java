package com.Link.service;

import com.Link.models.User;
import com.Link.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepositoty;
	@Override
    @Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepositoty.findByUsername(username)
                .orElseThrow( ()-> new UsernameNotFoundException("user not found exception"));
		return UserDetailsImpl.build(user);
	}

}
