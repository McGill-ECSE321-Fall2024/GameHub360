import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";

interface Promotion {
    promotionId: number;
    promotionType: string;
    discountPercentageValue: number;
    promotedItems: string[]; // List of associated games or categories
}

const PromotionsPage: React.FC = () => {
    const [promotions, setPromotions] = useState<Promotion[]>([]);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const [filter, setFilter] = useState<"Game" | "Category">("Game");
    const navigate = useNavigate();

    useEffect(() => {
        const fetchPromotions = async () => {
            setLoading(true);
            setErrorMessage(null);

            try {
                const response = await fetch("http://localhost:8080/manager/promotions");
                if (!response.ok) {
                    throw new Error("Failed to fetch promotions.");
                }
                const data = await response.json();
                setPromotions(data.promotions);
            } catch (error) {
                console.error("Error fetching promotions:", error);
                setErrorMessage("Failed to load promotions.");
            } finally {
                setLoading(false);
            }
        };

        fetchPromotions();
    }, []);

    const handleFilter = (type: "Game" | "Category") => {
        setFilter(type);
    };

    return (
        <div className="p-6 bg-gray-50 min-h-screen">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-3xl font-semibold text-gray-800">Promotions</h2>
                <button
                    onClick={() => navigate("/promotions/create")}
                    className="bg-blue-500 hover:bg-blue-600 text-white font-medium px-5 py-2 rounded-lg shadow-md transition duration-200"
                >
                    Create Promotion
                </button>
            </div>

            {errorMessage && (
                <div className="mb-4 p-4 text-red-700 bg-red-100 rounded-lg">
                    {errorMessage}
                </div>
            )}

            {/* Filter Buttons */}
            <div className="mb-6 flex space-x-4">
                {["Game", "Category"].map((status) => (
                    <button
                        key={status}
                        className={`px-5 py-2 rounded-lg shadow-sm font-medium transition duration-200 ${
                            filter === status
                                ? "bg-blue-500 text-white"
                                : "bg-gray-200 text-gray-700 hover:bg-gray-300"
                        }`}
                        onClick={() => handleFilter(status as "Game" | "Category")}
                    >
                        {status}
                    </button>
                ))}
            </div>

            {loading ? (
                <p className="text-gray-700 text-lg">Loading promotions...</p>
            ) : promotions.length === 0 ? (
                <p className="text-gray-700 text-lg">No promotions found.</p>
            ) : (
                <div className="overflow-x-auto bg-white rounded-lg shadow-md">
                    <table className="min-w-full border-collapse">
                        <thead className="bg-gray-100 border-b">
                        <tr>
                            <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                                ID
                            </th>
                            <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                                Discount (%)
                            </th>
                            <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                                {filter === "Game" ? "Games" : "Categories"}
                            </th>
                        </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200">
                        {promotions.map((promotion) => (
                            <tr
                                key={promotion.promotionId}
                                className="border-b hover:bg-gray-50 cursor-pointer transition"
                                onClick={() =>
                                    navigate(`/promotions/${promotion.promotionId}/items`)
                                }
                            >
                                <td className="px-6 py-4 text-sm text-gray-800">
                                    {promotion.promotionId}
                                </td>
                                <td className="px-6 py-4 text-sm text-gray-800">
                                    {promotion.discountPercentageValue}
                                </td>
                                <td className="px-6 py-4 text-sm text-blue-500">
                                    <Link to={`/promotions/${promotion.promotionId}/items`}>
                                        {filter === "Game" ? "View Games" : "View Categories"}
                                    </Link>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default PromotionsPage;











