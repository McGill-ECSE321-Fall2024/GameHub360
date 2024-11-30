import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";

interface Review {
    reviewId: number;
    rating: string;
    comment: string;
    reviewedOrderGameId: number;
}

const ReviewsPage: React.FC = () => {
    const [reviews, setReviews] = useState<Review[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetch("/games/reviews")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch reviews.");
                }
                return response.json();
            })
            .then((data) => setReviews(data.reviews))
            .catch((err) => setError(err.message));
    }, []);

    return (
        <div>
            <h1>Reviews</h1>
            {error && <p style={{ color: "red" }}>{error}</p>}
            <ul>
                {reviews.map((review) => (
                    <li key={review.reviewId}>
                        <p>Rating: {review.rating}</p>
                        <p>Comment: {review.comment}</p>
                        <Link to={`/games/reviews/${review.reviewId}`}>Details</Link>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ReviewsPage;
