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
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchReviews = async () => {
            setLoading(true);
            setError(null);
            try {
                const response = await fetch("http://localhost:8080/games/reviews");
                if (!response.ok) {
                    throw new Error("Failed to fetch reviews.");
                }
                const data = await response.json();
                setReviews(data.reviews || []); // Ensure `data.reviews` exists
            } catch (err: any) {
                setError(err.message || "An unexpected error occurred.");
            } finally {
                setLoading(false);
            }
        };

        fetchReviews();
    }, []);

    if (loading) {
        return <p className="text-gray-700 text-lg">Loading reviews...</p>;
    }

    if (error) {
        return (
            <div className="p-4 mb-4 text-red-700 bg-red-100 rounded-lg">
                {error}
            </div>
        );
    }

    if (!reviews || reviews.length === 0) {
        return (
            <p className="text-gray-700 text-lg">
                No reviews found. Please check back later.
            </p>
        );
    }

    return (
        <div className="p-6 bg-gray-50 min-h-screen">
            {/* Header */}
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-3xl font-semibold text-gray-800">Reviews</h2>
            </div>

            {/* Reviews List */}
            <div className="overflow-x-auto bg-white rounded-lg shadow-md">
                <table className="min-w-full border-collapse">
                    <thead className="bg-gray-100 border-b">
                    <tr>
                        <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                            ID
                        </th>
                        <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                            Rating
                        </th>
                        <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                            Comment
                        </th>
                        <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                            Details
                        </th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {reviews.map((review) => (
                        <tr key={review.reviewId} className="hover:bg-gray-50">
                            <td className="px-6 py-4 text-sm text-gray-800">
                                {review.reviewId}
                            </td>
                            <td className="px-6 py-4 text-sm text-gray-800">
                                {review.rating}
                            </td>
                            <td className="px-6 py-4 text-sm text-gray-800">
                                {review.comment}
                            </td>
                            <td className="px-6 py-4 text-sm text-gray-800">
                                <Link
                                    to={`/games/reviews/${review.reviewId}`}
                                    className="text-blue-600 hover:text-blue-800"
                                >
                                    View Details
                                </Link>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default ReviewsPage;


