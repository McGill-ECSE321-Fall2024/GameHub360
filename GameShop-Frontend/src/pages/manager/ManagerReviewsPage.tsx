import React, { useEffect, useState } from "react";
import { deleteReview, getGameReviews } from "../../api/reviewService";

interface Review {
    reviewId: number;
    comment: string;
    rating: string;
    customerId: number;
    createdAt: string;
}

const ManagerReviewsPage: React.FC = () => {
    const [reviews, setReviews] = useState<Review[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchReviews = async () => {
            setLoading(true);
            setError(null);
            try {
                // Assuming `1` is a placeholder for a specific game ID to fetch reviews.
                const data = await getGameReviews(1);
                setReviews(data);
            } catch (err: any) {
                console.error("Error fetching reviews:", err);
                setError("Failed to load reviews. Please try again later.");
            } finally {
                setLoading(false);
            }
        };

        fetchReviews();
    }, []);

    const handleDelete = async (reviewId: number) => {
        if (!window.confirm("Are you sure you want to delete this review?")) return;

        try {
            await deleteReview(reviewId);
            setReviews(reviews.filter((review) => review.reviewId !== reviewId));
        } catch (err: any) {
            console.error("Error deleting review:", err);
            setError("Failed to delete the review. Please try again.");
        }
    };

    return (
        <div className="p-6 bg-gray-50 min-h-screen">
            <h2 className="text-3xl font-semibold text-gray-800 mb-6">Manage Reviews</h2>
            {error && <div className="text-red-500 mb-4">{error}</div>}
            {loading ? (
                <p className="text-gray-700">Loading reviews...</p>
            ) : reviews.length === 0 ? (
                <p className="text-gray-700">No reviews found.</p>
            ) : (
                <table className="min-w-full bg-white rounded-lg shadow-md">
                    <thead>
                    <tr className="bg-gray-100">
                        <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600">ID</th>
                        <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600">Comment</th>
                        <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600">Rating</th>
                        <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600">Customer ID</th>
                        <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {reviews.map((review) => (
                        <tr key={review.reviewId} className="border-b hover:bg-gray-50">
                            <td className="px-6 py-4 text-sm text-gray-800">{review.reviewId}</td>
                            <td className="px-6 py-4 text-sm text-gray-800">{review.comment}</td>
                            <td className="px-6 py-4 text-sm text-gray-800">{review.rating}</td>
                            <td className="px-6 py-4 text-sm text-gray-800">{review.customerId}</td>
                            <td className="px-6 py-4 text-sm text-gray-800">
                                <button
                                    onClick={() => handleDelete(review.reviewId)}
                                    className="text-red-500 hover:text-red-600"
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default ManagerReviewsPage;




