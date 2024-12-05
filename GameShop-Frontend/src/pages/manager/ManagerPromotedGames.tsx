import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getPromotedGames } from "../../api/promotionsService";

interface Game {
    gameId: number;
    name: string;
    description: string;
    price: number;
    imageUrl: string;
}

const ManagerPromotedGamesPage: React.FC = () => {
    const { promotionId } = useParams<{ promotionId: string }>();
    const [games, setGames] = useState<Game[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchGames = async () => {
            // Check if promotionId is valid
            if (!promotionId) {
                setError("Invalid or missing promotion ID.");
                setLoading(false);
                return;
            }

            setLoading(true);
            setError(null);

            try {
                const games = await getPromotedGames(promotionId);
                setGames(games);
            } catch (error: any) {
                console.error("Error fetching promoted games:", error);
                setError("Failed to fetch promoted games. Please try again.");
            } finally {
                setLoading(false);
            }
        };

        fetchGames();
    }, [promotionId]);

    return (
        <div className="p-6 bg-gray-50 min-h-screen">
            <h2 className="text-3xl font-semibold text-gray-800 mb-6">Promoted Games</h2>
            <Link to="/promotions" className="text-blue-500 mb-4 block">Back to Promotions</Link>

            {error && (
                <div className="text-red-500 mb-4">{error}</div>
            )}

            {loading ? (
                <p className="text-gray-700 text-lg">Loading games...</p>
            ) : games.length === 0 ? (
                <p className="text-gray-700 text-lg">No games found for this promotion.</p>
            ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                    {games.map((game) => (
                        <div key={game.gameId} className="bg-white p-4 rounded-lg shadow-md">
                            <img
                                src={game.imageUrl}
                                alt={game.name}
                                className="w-full h-48 object-cover rounded-t-md"
                            />
                            <div className="p-4">
                                <h3 className="text-xl font-semibold">{game.name}</h3>
                                <p className="text-gray-600 text-sm mb-2">{game.description}</p>
                                <p className="text-green-600 font-bold">${game.price.toFixed(2)}</p>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default ManagerPromotedGamesPage;










