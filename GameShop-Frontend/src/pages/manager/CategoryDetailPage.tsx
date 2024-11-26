import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  getCategoryById,
  updateCategory,
  deleteCategory,
  assignCategoryToGame,
  unassignCategoryFromGame,
  assignCategoryToPromotion,
  unassignCategoryFromPromotion,
  Category,
} from '../../api/categoryService';
import { getAllGames, Game } from '../../api/gameService';
import { getAllPromotions, Promotion } from '../../api/promotionsService';

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
  const [selectedPromotionId, setSelectedPromotionId] = useState<number | null>(null);
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

  const handleUpdate = async () => {
    if (!category) return;

    if (!category.name.trim()) {
      setNameError('Category name cannot be empty.');
      return;
    }

    try {
      await updateCategory(category.categoryId, {
        name: category.name,
        categoryType: category.categoryType,
        available: category.available,
      });
      setSuccessMessage('Category updated successfully.');
      setErrorMessage(null);
      setNameError(null);
    } catch (error: any) {
      setErrorMessage(error.message || 'Failed to update category.');
    }
  };

  const handleDelete = async () => {
    if (!category) return;

    const confirmDelete = window.confirm('Are you sure you want to delete this category?');
    if (!confirmDelete) return;

    try {
      await deleteCategory(category.categoryId);
      navigate('/manager/categories'); // Redirect to categories list after deletion
    } catch (error: any) {
      setErrorMessage(error.message || 'Failed to delete category.');
    }
  };

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
    const confirmUnassign = window.confirm('Are you sure you want to unassign this game?');
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

  const handleAssignPromotion = async () => {
    if (!selectedPromotionId || !category) return;
    if (category.promotionIds.includes(selectedPromotionId)) {
      setErrorMessage('Promotion is already assigned to this category.');
      return;
    }
    setActionLoading(true);
    try {
      await assignCategoryToPromotion(category.categoryId, selectedPromotionId);
      setSuccessMessage('Promotion assigned successfully.');
      setCategory({
        ...category,
        promotionIds: [...category.promotionIds, selectedPromotionId],
      });
      setSelectedPromotionId(null); // Reset selection
    } catch (error: any) {
      setErrorMessage(error.message || 'Failed to assign promotion.');
    } finally {
      setActionLoading(false);
    }
  };

  const handleUnassignPromotion = async (promotionId: number) => {
    const confirmUnassign = window.confirm('Are you sure you want to unassign this promotion?');
    if (!confirmUnassign) return;

    if (!category) return;
    setActionLoading(true);
    try {
      await unassignCategoryFromPromotion(category.categoryId, promotionId);
      setSuccessMessage('Promotion unassigned successfully.');
      setCategory({
        ...category,
        promotionIds: category.promotionIds.filter((id) => id !== promotionId),
      });
    } catch (error: any) {
      setErrorMessage(error.message || 'Failed to unassign promotion.');
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

  if (loading) return <p>Loading category details...</p>;
  if (errorMessage) return <p className="text-red-600">{errorMessage}</p>;

  return (
    <div className="p-6">
      <div className="flex items-center mb-4">
        <button
          onClick={() => navigate('/manager/categories')}
          className="bg-gray-200 text-gray-800 px-4 py-2 rounded mr-4"
        >
          Back
        </button>
        <h2 className="text-2xl font-bold tracking-tight text-gray-900">Category Details</h2>
      </div>

      {successMessage && <p className="text-green-600">{successMessage}</p>}
      {errorMessage && <p className="text-red-600">{errorMessage}</p>}

      {category && (
        <div>
          {/* Update Form */}
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700">Name:</label>
            <input
              type="text"
              value={category.name}
              onChange={(e) => {
                setCategory({ ...category, name: e.target.value });
                if (e.target.value.trim()) {
                  setNameError(null); // Clear the error if the user starts typing
                }
              }}
              className={`w-full border rounded-md p-2 ${nameError ? 'border-red-500' : ''}`}
            />
            {nameError && <p className="text-red-600 text-sm mt-1">{nameError}</p>}
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700">Category Type:</label>
            <select
              value={category.categoryType}
              onChange={(e) =>
                setCategory({ ...category, categoryType: e.target.value })
              }
              className="w-full border rounded-md p-2"
            >
              <option value="GENRE">Genre</option>
              <option value="CONSOLE">Console</option>
            </select>
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700">Available:</label>
            <input
              type="checkbox"
              checked={category.available}
              onChange={(e) => setCategory({ ...category, available: e.target.checked })}
              className="mr-2"
            />
            <span>Mark as available</span>
          </div>

          <button
            onClick={handleUpdate}
            className="mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Update Category
          </button>
          <button
            onClick={handleDelete}
            className="mt-4 bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 ml-4"
          >
            Delete Category
          </button>

          {/* Assign and Unassign Section */}
          <div className="mt-6">
            <h3 className="text-xl font-semibold">Assigned Games</h3>
            <ul>
              {category.gameIds.map((gameId) => {
                const game = games.find((g) => g.gameId === gameId);
                return (
                  <li key={gameId} className="flex items-center justify-between">
                    <span>{game?.name || `Game ID: ${gameId}`}</span>
                    <button
                      onClick={() => handleUnassignGame(gameId)}
                      className="bg-red-500 text-white px-2 py-1 rounded"
                      disabled={actionLoading}
                    >
                      Unassign
                    </button>
                  </li>
                );
              })}
            </ul>

            <h3 className="text-xl font-semibold mt-4">Assign New Game</h3>
            <select
              value={selectedGameId || ''}
              onChange={(e) => setSelectedGameId(parseInt(e.target.value))}
              className="w-full border rounded-md p-2"
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
              className="mt-2 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
              disabled={!selectedGameId || actionLoading}
            >
              Assign Game
            </button>
          </div>

          <div className="mt-6">
            <h3 className="text-xl font-semibold">Assigned Promotions</h3>
            <ul>
              {category.promotionIds.map((promotionId) => {
                const promotion = promotions.find((p) => p.promotionId === promotionId);
                return (
                  <li key={promotionId} className="flex items-center justify-between">
                    <span>
                      {promotion
                        ? `${promotion.promotionType}: ${promotion.discountPercentageValue}%`
                        : `Promotion ID: ${promotionId}`}
                    </span>
                    <button
                      onClick={() => handleUnassignPromotion(promotionId)}
                      className="bg-red-500 text-white px-2 py-1 rounded"
                      disabled={actionLoading}
                    >
                      Unassign
                    </button>
                  </li>
                );
              })}
            </ul>

            <h3 className="text-xl font-semibold mt-4">Assign New Promotion</h3>
            <select
              value={selectedPromotionId || ''}
              onChange={(e) => setSelectedPromotionId(parseInt(e.target.value))}
              className="w-full border rounded-md p-2"
            >
              <option value="" disabled>
                Select a promotion
              </option>
              {promotions.map((promotion) => (
                <option key={promotion.promotionId} value={promotion.promotionId}>
                  {promotion.promotionType}: {promotion.discountPercentageValue}%
                </option>
              ))}
            </select>
            <button
              onClick={handleAssignPromotion}
              className="mt-2 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
              disabled={!selectedPromotionId || actionLoading}
            >
              Assign Promotion
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default CategoryDetailPage;
