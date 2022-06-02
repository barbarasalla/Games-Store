package com.store.games.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.games.model.User;
import com.store.games.model.UserLogin;
import com.store.games.repository.UserRepository;
import com.store.games.service.UserService;

@RestController
@CrossOrigin(origins="*", allowedHeaders="*")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/all")
	public ResponseEntity<List<User>> GetAll(){
		return ResponseEntity.ok(userRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> GetById(@PathVariable long id){
		return userRepository.findById(id)
				.map(answer -> ResponseEntity.ok(answer))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}
	
	@PostMapping("/login")
	public ResponseEntity<UserLogin> loginUser(@RequestBody Optional <UserLogin> userLogin){
		
		return userService.authenticateUser(userLogin)
			.map(answer -> ResponseEntity.status(HttpStatus.OK).body(answer))
			.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
	
	@PostMapping("/register")
	public ResponseEntity<User> postUsuario(@Valid @RequestBody User user){
		
		return userService.registerUser(user)
			.map(answer -> ResponseEntity.status(HttpStatus.CREATED).body(answer))
			.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}
	
	@PutMapping("/update")
	public ResponseEntity<User> putUser (@Valid @RequestBody User user){
		return userService.updateUser(user)
				.map(answer -> ResponseEntity.status(HttpStatus.OK).body(answer))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		
		return userRepository.findById(id)
			.map(answer -> {
				userRepository.deleteById(id);
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			})
			.orElse(ResponseEntity.notFound().build());
	}	
	
}

