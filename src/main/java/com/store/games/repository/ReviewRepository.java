package com.store.games.repository;

import com.store.games.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    public List<Review> findAllByRatingContaining(@Param("rating") Integer rating);

}
