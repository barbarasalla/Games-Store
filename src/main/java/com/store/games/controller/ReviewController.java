package com.store.games.controller;

import com.store.games.model.Product;
import com.store.games.model.Review;
import com.store.games.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins="*", allowedHeaders="*")
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Review>> getByAllReview (){
        return ResponseEntity.ok(reviewRepository.findAll());
    }

    @GetMapping("/id")
    public ResponseEntity<Review> getByIdReview (@PathVariable long id){
        return reviewRepository.findById(id).map(answer -> ResponseEntity.ok(answer))
                .orElse((ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    @PostMapping
    public ResponseEntity<Review> postReview(@Valid @RequestBody Review review){
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewRepository.save(review));
    }

    @PutMapping
    public ResponseEntity<Review> putReview (@Valid @RequestBody Review review){
        return reviewRepository.findById(review.getId())
                .map(answer -> ResponseEntity.status(HttpStatus.OK).body(reviewRepository.save(review)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview (@PathVariable long id){
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        reviewRepository.deleteById(id);
    }

}
