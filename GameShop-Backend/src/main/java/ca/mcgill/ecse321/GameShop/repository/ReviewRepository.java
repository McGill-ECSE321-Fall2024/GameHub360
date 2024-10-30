package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    // Find review by review id
    public Review findReviewByReviewId(Integer reviewId);
}
