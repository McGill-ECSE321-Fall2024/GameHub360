import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

interface ReviewDetails {
    reviewId: number;
    rating: string;
    comment: string;
    reviewDate: string;
    reviewReplies: string[];
}

const ReviewDetailsPage: React.FC = () => {
    const { reviewId } = useParams<{ reviewId: string }>();
    const [review, setReview] = useState<ReviewDetails | null>(null);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetch(`/games/reviews/${reviewId}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch review details.");
                }
                return response.json();
            })
            .then((data) => setReview(data))
            .catch((err) => setError(err.message));
    }, [reviewId]);

    if (error) {
        return <p style={{ color: "red" }}>{error}</p>;
    }

    if (!review) {
        return <p>Loading...</p>;
    }

    return (
        <div>
            <h1>Review Details</h1>
            <p>Rating: {review.rating}</p>
            <p>Comment: {review.comment}</p>
            <p>Date: {review.reviewDate}</p>
            <h2>Replies</h2>
            <ul>
                {review.reviewReplies.map((reply, index) => (
                    <li key={index}>{reply}</li>
                ))}
            </ul>
        </div>
    );
};

export default ReviewDetailsPage;
