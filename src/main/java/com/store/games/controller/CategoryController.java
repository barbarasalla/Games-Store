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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.store.games.model.Category;
import com.store.games.repository.CategoryRepository;

@RestController
@CrossOrigin(origins="*", allowedHeaders="*")
@RequestMapping("/categories")
public class CategoryController {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping("/all")
	public ResponseEntity<List<Category>> GetAll(){
		return ResponseEntity.ok(categoryRepository.findAll());
	}
	
	@GetMapping("/category/{id}")
	public ResponseEntity<Category> GetById(@PathVariable long id){
		return categoryRepository.findById(id)
				.map(answer -> ResponseEntity.ok(answer))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Category>> GetByName(@PathVariable String name){
		return ResponseEntity.ok(categoryRepository.findAllByNameContainingIgnoreCase(name));
	}
	
	@PostMapping
	public ResponseEntity<Category> post (@Valid @RequestBody Category category){
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryRepository.save(category));
	}
	
	@PutMapping
	public ResponseEntity<Category> put (@Valid @RequestBody Category category){
		return categoryRepository.findById(category.getId())
				.map(answer -> ResponseEntity.status(HttpStatus.OK).body(categoryRepository.save(category)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());				
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete (@PathVariable long id) {
		Optional<Category> category = categoryRepository.findById(id);
		if(category.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		categoryRepository.deleteById(id);
	}

}
