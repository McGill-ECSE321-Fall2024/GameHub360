import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  getCategoryById,
  assignCategoryToGame,
  unassignCategoryFromGame,
} from '../../api/categoryService';
import { getAllGames } from '../../api/gameService';
import { Game } from '../../model/manager/Game';
import { getAllPromotions, Promotion } from '../../api/promotionsService';
import { Category } from '../../model/manager/Category';

const CategoryDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [category, setCategory] = useState<Category | null>(null);
  const [games, setGames] = useState<Game[]>([]);
  const [promotions, setPromotions] = useState<Promotion[]>([]);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(false); // For assign/unassign actions
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const [selectedGameId, setSelectedGameId] = useState<number | null>(null);
  const [selectedPromotionId, setSelectedPromotionId] = useState<number | null>(
    null
  );
  const [nameError, setNameError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        if (id) {
          const categoryData = await getCategoryById(parseInt(id));
          setCategory({
            ...categoryData,
            gameIds: categoryData.gameIds || [], // Ensure gameIds is always an array
            promotionIds: categoryData.promotionIds || [], // Ensure promotionIds is always an array
          });
        }
        const gamesData = await getAllGames();
        setGames(gamesData || []); // Default to an empty array
        const promotionsData = await getAllPromotions();
        setPromotions(promotionsData || []); // Default to an empty array
      } catch (error: any) {
        setErrorMessage(error.message || 'Failed to fetch data.');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id]);

  const handleAssignGame = async () => {
    if (!selectedGameId || !category) return;
    if (category.gameIds.includes(selectedGameId)) {
      setErrorMessage('Game is already assigned to this category.');
      return;
    }
    setActionLoading(true);
    try {
      await assignCategoryToGame(category.categoryId, selectedGameId);
      setSuccessMessage('Game assigned successfully.');
      setCategory({
        ...category,
        gameIds: [...category.gameIds, selectedGameId],
      });
      setSelectedGameId(null); // Reset selection
    } catch (error: any) {
      setErrorMessage(error.message || 'Failed to assign game.');
    } finally {
      setActionLoading(false);
    }
  };

  const handleUnassignGame = async (gameId: number) => {
    const confirmUnassign = window.confirm(
      'Are you sure you want to unassign this game?'
    );
    if (!confirmUnassign) return;

    if (!category) return;
    setActionLoading(true);
    try {
      await unassignCategoryFromGame(category.categoryId, gameId);
      setSuccessMessage('Game unassigned successfully.');
      setCategory({
        ...category,
        gameIds: category.gameIds.filter((id) => id !== gameId),
      });
    } catch (error: any) {
      setErrorMessage(error.message || 'Failed to unassign game.');
    } finally {
      setActionLoading(false);
    }
  };

  useEffect(() => {
    if (successMessage || errorMessage) {
      const timer = setTimeout(() => {
        setSuccessMessage(null);
        setErrorMessage(null);
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [successMessage, errorMessage]);

  if (loading)
    return (
      <p className="text-center text-lg text-gray-600">
        Loading category details...
      </p>
    );
  if (errorMessage)
    return <p className="text-center text-lg text-red-600">{errorMessage}</p>;

  return (
    <div className="container mx-auto p-6 bg-gray-100 rounded shadow-md">
      <div className="flex items-center mb-4">
        <button
          onClick={() => navigate('/manager/categories')}
          className="bg-gray-200 text-gray-800 px-4 py-2 rounded mr-4"
        >
          Back
        </button>
        <h2 className="text-2xl font-bold tracking-tight text-gray-900">
          Category Details
        </h2>
      </div>

      {successMessage && (
        <div className="p-4 mb-4 text-green-800 bg-green-200 rounded">
          {successMessage}
        </div>
      )}
      {errorMessage && (
        <div className="p-4 mb-4 text-red-800 bg-red-200 rounded">
          {errorMessage}
        </div>
      )}

      {category && (
        <div>

          {/* Assigned Games */}
          <div className="bg-white p-4 rounded shadow-sm mb-6">
            <h3 className="text-lg font-medium text-gray-800 mb-4">
              Assigned Games
            </h3>
            <div className="overflow-x-auto">
              <table className="table-auto w-full border-collapse border border-gray-200">
                <thead className="bg-gray-100">
                  <tr>
                    <th className="px-4 py-2 text-left text-gray-600 font-medium">
                      Game Name
                    </th>
                    <th className="px-4 py-2 text-right text-gray-600 font-medium">
                      Action
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {category.gameIds.map((gameId) => {
                    const game = games.find((g) => g.gameId === gameId);
                    return (
                      <tr key={gameId} className="border-t">
                        <td className="px-4 py-2 text-gray-700">
                          {game?.name || `Game ID: ${gameId}`}
                        </td>
                        <td className="px-4 py-2 text-right">
                          <button
                            onClick={() => handleUnassignGame(gameId)}
                            className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition"
                            disabled={actionLoading}
                          >
                            Unassign
                          </button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
            <h4 className="text-sm font-medium mt-6 mb-2">Assign New Game</h4>
            <select
              value={selectedGameId || ''}
              onChange={(e) => setSelectedGameId(parseInt(e.target.value))}
              className="w-full border rounded-md p-2 mb-3"
            >
              <option value="" disabled>
                Select a game
              </option>
              {games.map((game) => (
                <option key={game.gameId} value={game.gameId}>
                  {game.name}
                </option>
              ))}
            </select>
            <button
              onClick={handleAssignGame}
              className="w-full bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
              disabled={!selectedGameId || actionLoading}
            >
              Assign Game
            </button>
          </div>

          {/* Assigned Promotions */}
          <div className="bg-white p-4 rounded shadow-sm">
            <h3 className="text-lg font-medium text-gray-800 mb-4">
              Assigned Promotions
            </h3>
            <div className="overflow-x-auto">
              <table className="table-auto w-full border-collapse border border-gray-200">
                <thead className="bg-gray-100">
                  <tr>
                    <th className="px-4 py-2 text-left text-gray-600 font-medium">
                      Promotion
                    </th>
                    <th className="px-4 py-2 text-right text-gray-600 font-medium">
                      Action
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {category.promotionIds.map((promotionId) => {
                    const promotion = promotions.find(
                      (p) => p.promotionId === promotionId
                    );
                    return (
                      <tr key={promotionId} className="border-t">
                        <td className="px-4 py-2 text-gray-700">
                          {promotion
                            ? `${promotion.promotionType}: ${promotion.discountPercentageValue}%`
                            : `Promotion ID: ${promotionId}`}
                        </td>
                        <td className="px-4 py-2 text-right">
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CategoryDetailPage;