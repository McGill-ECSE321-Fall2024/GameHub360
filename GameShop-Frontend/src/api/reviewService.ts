import apiService from "../config/axiosConfig";

export interface ReviewRequestDto {
    rating: string; 
    customerId: number; 
    comment: string; 
  }  

// Submit a review
export async function submitReview(orderGameId: number, reviewData: ReviewRequestDto): Promise<any> {
    try {
      const response = await apiService.post(`/games/${orderGameId}/reviews`, reviewData);
      return response.data;
    } catch (error) {
      console.error("Error submitting review:", error);
      throw error;
    }
  }  

// View reviews for a game
export async function getGameReviews(gameId: number) {
  try {
    const response = await apiService.get(`/games/${gameId}/reviews`);
    return response.data.reviews; // Assuming `reviews` is the key in the `ReviewListDto`
  } catch (error) {
    console.error("Error fetching reviews:", error);
    throw new Error("Unable to fetch reviews.");
  }
}

// Delete a review
export async function deleteReview(reviewId: number) {
  try {
    await apiService.delete(`/games/reviews/${reviewId}`);
  } catch (error) {
    console.error("Error deleting review:", error);
    throw new Error("Unable to delete review.");
  }
}
