package com.example.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author ly
 */
@Service
public class UserServiceImpl implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		return new User(null,null,null);
	}

}
