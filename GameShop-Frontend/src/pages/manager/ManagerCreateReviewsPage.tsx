import React, { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { submitReview } from "../../api/reviewService";

const CreateReviewPage: React.FC = () => {
    const { gameId } = useParams<{ gameId: string }>(); // Assuming the gameId is passed in the route
    const [rating, setRating] = useState<number>(0);
    const [comment, setComment] = useState<string>("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            if (!gameId) {
                throw new Error("Game ID is missing.");
            }

            const reviewData = {
                rating: rating.toString(),
                customerId: 1, // Replace with the actual logged-in customer ID
                comment,
            };

            console.log("Submitting review:", reviewData);
            await submitReview(parseInt(gameId, 10), reviewData);
            navigate(`/games/${gameId}/reviews`); // Redirect to the game's review page on success
        } catch (err: any) {
            console.error("Error submitting review:", err);
            setError(err.message || "Failed to submit review.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="p-6 bg-gray-50 min-h-screen">
            <h2 className="text-3xl font-semibold text-gray-800 mb-6">Write a Review</h2>
            {error && <div className="text-red-500 mb-4">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="mb-4">
                    <label className="block text-gray-700">Rating (1-5)</label>
                    <select
                        value={rating}
                        onChange={(e) => setRating(Number(e.target.value))}
                        className="w-full border rounded-lg px-4 py-2"
                        required
                    >
                        <option value="" disabled>
                            Select a rating
                        </option>
                        {[1, 2, 3, 4, 5].map((value) => (
                            <option key={value} value={value}>
                                {value}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700">Comment</label>
                    <textarea
                        value={comment}
                        onChange={(e) => setComment(e.target.value)}
                        className="w-full border rounded-lg px-4 py-2"
                        rows={5}
                        placeholder="Write your review here..."
                        required
                    ></textarea>
                </div>

                <div className="flex space-x-4">
                    <button
                        type="submit"
                        disabled={loading}
                        className="bg-blue-500 hover:bg-blue-600 text-white px-6 py-2 rounded-lg"
                    >
                        {loading ? "Submitting..." : "Submit Review"}
                    </button>
                    <button
                        type="button"
                        onClick={() => navigate(-1)} // Navigate back to the previous page
                        className="bg-gray-300 hover:bg-gray-400 text-gray-800 px-6 py-2 rounded-lg"
                    >
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
};

export default CreateReviewPage;
