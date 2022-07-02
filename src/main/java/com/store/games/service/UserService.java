package com.store.games.service;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.store.games.model.User;
import com.store.games.model.UserLogin;
import com.store.games.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public Optional<User> registerUser(User user) {
		
		if(userRepository.findByUserName(user.getUserName()).isPresent())
			return Optional.empty();
		
		if(checkAge(user.getBirth())<18) 
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Age less than 18 years.");
		
		if(user.getPhoto().isBlank())
			user.setPhoto("https://imgur.com/a/2AhO3I6.png");
		
		user.setPassword(encryptPassword(user.getPassword()));
		
		return Optional.ofNullable(userRepository.save(user));			
		
	}
	
	
	public Optional<UserLogin> authenticateUser(Optional<UserLogin> userLogin){
		
		Optional<User> searchUser = userRepository.findByUserName(userLogin.get().getUserName());
		
		if(searchUser.isPresent()) {
			
			if(comparePassword(userLogin.get().getPassword(), searchUser.get().getPassword())) {
				
				userLogin.get().setId(searchUser.get().getId());
				userLogin.get().setName(searchUser.get().getName());
				userLogin.get().setPhoto(searchUser.get().getPhoto());
				userLogin.get().setUserType(searchUser.get().getUserType());
				userLogin.get().setToken(createBasicToken(userLogin.get().getUserName(), userLogin.get().getPassword()));
				userLogin.get().setPassword(searchUser.get().getPassword());
				
				
				return userLogin;
			}
			
		}
		return Optional.empty();		
	
	}
	
	public Optional<User> updateUser (User user){
		
		if(userRepository.findById(user.getId()).isPresent()) {
			
		Optional<User> searchUser= userRepository.findByUserName(user.getUserName());
		
		if(searchUser.isPresent() && (searchUser.get().getId() != user.getId()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists!!", null);
		
		if(checkAge(user.getBirth()) < 18)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Age is less than 18 years");
		
		if(user.getPhoto().isBlank())
			user.setPhoto("https://imgur.com/a/2AhO3I6.png");
		
		user.setPassword(encryptPassword(user.getPassword()));
		
		return Optional.ofNullable(userRepository.save(user));
		}
		
		return Optional.empty();
		
	}
	
	private int checkAge(LocalDate birth) {

		return Period.between(birth, LocalDate.now()).getYears();
	}
	
	private String encryptPassword(String password) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(password);
	}
	
	private boolean comparePassword(String passwordTyped, String passwordDatabase) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.matches(passwordTyped, passwordDatabase);
	}
	
	private String createBasicToken(String user, String password) {
		
		String token = user + ":" + password;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII"))); 
		
		return "Basic " + new String(tokenBase64);
	
	}

}
