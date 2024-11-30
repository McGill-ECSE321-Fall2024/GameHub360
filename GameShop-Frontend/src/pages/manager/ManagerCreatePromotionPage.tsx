import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const CreatePromotionPage: React.FC = () => {
    const [formData, setFormData] = useState({
        promotionType: "GAME",
        discountPercentageValue: 0,
        promotedGameIds: [] as number[],
        promotedCategoryIds: [] as number[],
        promotedGameIdsInput: "", // For handling string input
        promotedCategoryIdsInput: "", // For handling string input
    });
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = event.target;

        if (name === "promotedGameIdsInput" || name === "promotedCategoryIdsInput") {
            setFormData({ ...formData, [name]: value });
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();

        const data = { ...formData };
        data.promotedGameIds = formData.promotedGameIdsInput
            ? formData.promotedGameIdsInput.split(",").map((id) => parseInt(id.trim()))
            : [];
        data.promotedCategoryIds = formData.promotedCategoryIdsInput
            ? formData.promotedCategoryIdsInput.split(",").map((id) => parseInt(id.trim()))
            : [];

        fetch("/promotions/", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                promotionType: data.promotionType,
                discountPercentageValue: data.discountPercentageValue,
                promotedGameIds: data.promotedGameIds,
                promotedCategoryIds: data.promotedCategoryIds,
            }),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to create promotion.");
                }
                return response.json();
            })
            .then(() => navigate("/promotions"))
            .catch((err) => setError(err.message));
    };

    return (
        <div>
            <h1>Create Promotion</h1>
            {error && <p style={{ color: "red" }}>{error}</p>}
            <form onSubmit={handleSubmit}>
                <label>
                    Promotion Type:
                    <select
                        name="promotionType"
                        value={formData.promotionType}
                        onChange={handleChange}
                    >
                        <option value="GAME">Game</option>
                        <option value="CATEGORY">Category</option>
                    </select>
                </label>
                <label>
                    Discount (%):
                    <input
                        type="number"
                        name="discountPercentageValue"
                        value={formData.discountPercentageValue}
                        onChange={handleChange}
                        required
                    />
                </label>
                <label>
                    Promoted Game IDs (comma-separated):
                    <input
                        type="text"
                        name="promotedGameIdsInput"
                        value={formData.promotedGameIdsInput}
                        onChange={handleChange}
                        disabled={formData.promotionType === "CATEGORY"}
                    />
                </label>
                <label>
                    Promoted Category IDs (comma-separated):
                    <input
                        type="text"
                        name="promotedCategoryIdsInput"
                        value={formData.promotedCategoryIdsInput}
                        onChange={handleChange}
                        disabled={formData.promotionType === "GAME"}
                    />
                </label>
                <button type="submit">Create</button>
            </form>
        </div>
    );
};

export default CreatePromotionPage;

