package com.store.games.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.store.games.model.User;
import com.store.games.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;


	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		Optional<User> usuario = userRepository.findByUserName(userName);
	  
		usuario.orElseThrow(() -> new UsernameNotFoundException(userName + " not found."));


		return usuario.map(UserDetailsImpl::new).get();
	}
}
