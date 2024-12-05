import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createPromotion } from "../../api/promotionsService";

const ManagerCreatePromotion: React.FC = () => {
    const [promotionType, setPromotionType] = useState<"GAME" | "CATEGORY">("GAME");
    const [discountPercentageValue, setDiscountPercentageValue] = useState<number>(0);
    const [promotedItems, setPromotedItems] = useState<string>(""); // IDs as a comma-separated string
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const promotion = {
                promotionType, // Match the expected type
                discountPercentageValue, // Numeric percentage
                promotedItems: promotedItems
                    .split(",")
                    .map((id) => parseInt(id.trim(), 10)) // Convert IDs to numbers
                    .filter((id) => !isNaN(id)), // Remove invalid IDs
            };

            console.log("Creating promotion with payload:", promotion);
            await createPromotion(promotion);
            navigate("/promotions"); // Redirect on success
        } catch (err: any) {
            console.error("Error creating promotion:", err);
            setError(err.message || "Failed to create promotion.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="p-6 bg-gray-50 min-h-screen">
            <h2 className="text-3xl font-semibold text-gray-800 mb-6">Create Promotion</h2>
            {error && <div className="text-red-500 mb-4">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="mb-4">
                    <label className="block text-gray-700">Promotion Type</label>
                    <select
                        value={promotionType}
                        onChange={(e) => setPromotionType(e.target.value as "GAME" | "CATEGORY")}
                        className="w-full border rounded-lg px-4 py-2"
                    >
                        <option value="GAME">Game</option>
                        <option value="CATEGORY">Category</option>
                    </select>
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700">Discount Percentage</label>
                    <input
                        type="number"
                        value={discountPercentageValue}
                        onChange={(e) => setDiscountPercentageValue(Number(e.target.value))}
                        className="w-full border rounded-lg px-4 py-2"
                        min="0"
                        max="100"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700">
                        Promoted {promotionType === "GAME" ? "Game" : "Category"} IDs (comma-separated)
                    </label>
                    <input
                        type="text"
                        value={promotedItems}
                        onChange={(e) => setPromotedItems(e.target.value)}
                        className="w-full border rounded-lg px-4 py-2"
                        placeholder="e.g., 1, 2, 3"
                        required
                    />
                </div>

                <div className="flex space-x-4">
                    <button
                        type="submit"
                        disabled={loading}
                        className="bg-blue-500 hover:bg-blue-600 text-white px-6 py-2 rounded-lg"
                    >
                        {loading ? "Creating..." : "Create Promotion"}
                    </button>
                    <button
                        type="button"
                        onClick={() => navigate("/promotions")}
                        className="bg-gray-300 hover:bg-gray-400 text-gray-800 px-6 py-2 rounded-lg"
                    >
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
};

export default ManagerCreatePromotion;




