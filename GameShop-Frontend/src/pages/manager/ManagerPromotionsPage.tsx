import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";

interface Promotion {
    promotionId: number;
    promotionType: string;
    discountPercentageValue: number;
}

const PromotionsPage: React.FC = () => {
    const [promotions, setPromotions] = useState<Promotion[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetch("/promotions/")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch promotions.");
                }
                return response.json();
            })
            .then((data) => setPromotions(data.promotions))
            .catch((err) => setError(err.message));
    }, []);

    return (
        <div>
            <h1>Promotions</h1>
            <Link to="/promotions/create">Create Promotion</Link>
            {error && <p style={{ color: "red" }}>{error}</p>}
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Type</th>
                    <th>Discount (%)</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {promotions.map((promotion) => (
                    <tr key={promotion.promotionId}>
                        <td>{promotion.promotionId}</td>
                        <td>{promotion.promotionType}</td>
                        <td>{promotion.discountPercentageValue}</td>
                        <td>
                            <Link to={`/promotions/${promotion.promotionId}`}>Details</Link>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default PromotionsPage;
