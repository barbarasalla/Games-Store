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

import com.store.games.model.Product;
import com.store.games.repository.CategoryRepository;
import com.store.games.repository.ProductRepository;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping("/all")
	public ResponseEntity<List<Product>> GetAll(){
		return ResponseEntity.ok(productRepository.findAll());
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<Product> GetById(@PathVariable long id){
		return productRepository.findById(id)
				.map(answer -> ResponseEntity.ok(answer))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Product>> GetByName(@PathVariable String name){
		return ResponseEntity.ok(productRepository.findAllByNameContainingIgnoreCase(name));
	}
	
	@GetMapping("/consoles/{console}")
	public ResponseEntity<List<Product>> GetByConsole(@PathVariable String console){
		return ResponseEntity.ok(productRepository.findAllByConsoleContainingIgnoreCase(console));
	}
	
	@PostMapping 
	public ResponseEntity<Product> post(@Valid @RequestBody Product product){
		if(categoryRepository.existsById(product.getCategory().getId())) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(productRepository.save(product));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@PutMapping
	public ResponseEntity<Product> put(@Valid @RequestBody Product product){
		if(productRepository.existsById(product.getId())) {
			
			if(categoryRepository.existsById(product.getCategory().getId())) {
				return  ResponseEntity.status(HttpStatus.OK)
						.body(productRepository.save(product));
			}
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();	
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)	
	public void delete(@PathVariable long id) {
		Optional<Product> product = productRepository.findById(id);
		if(product.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		productRepository.deleteById(id);
	}

}
