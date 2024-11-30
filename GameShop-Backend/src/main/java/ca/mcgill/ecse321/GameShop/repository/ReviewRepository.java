package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.Review;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;


public interface ReviewRepository extends CrudRepository<Review, Integer> {
    // Find review by review id
    public Review findReviewByReviewId(Integer reviewId);

    // Retrieve all reviews along with their associated games, organized by game
    @Query("SELECT r.reviewId, r.rating, r.comment, g.gameEntityId, g.name FROM Review r JOIN r.reviewedGame rg JOIN rg.game g ORDER BY g.name")
    public List<Object[]> findAllReviewsOrganizedByGame();

    // Retrieve all reply IDs associated with a review
    //@Query("SELECT r.replyId FROM Reply r WHERE r.review.reviewId = :reviewId")
    @Query("SELECT r FROM Review r WHERE r.reviewId = :reviewId")
    List<Integer> findRepliesByReviewId(@Param("reviewId") Integer reviewId);
}
