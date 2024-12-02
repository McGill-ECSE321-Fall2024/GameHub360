import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  getGameById,
  updateGame,
  archiveGame,
  reactivateGame,
  assignGameToCategory,
  assignPromotionToGame,
} from '../../api/gameService';
import { Game } from '../../model/manager/Game';

const ManagerGameDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [game, setGame] = useState<Game | null>(null);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [updateFields, setUpdateFields] = useState({
    name: '',
    description: '',
    imageUrl: '',
    price: '',
    quantityInStock: '',
  });
  const [categoryId, setCategoryId] = useState('');
  const [promotionId, setPromotionId] = useState('');

  useEffect(() => {
    const fetchGame = async () => {
      try {
        if (id) {
          const gameData = await getGameById(parseInt(id));
          setGame(gameData);
          setUpdateFields({
            name: gameData.name,
            description: gameData.description,
            imageUrl: gameData.imageUrl,
            price: gameData.price.toString(),
            quantityInStock: gameData.quantityInStock.toString(),
          });
        }
      } catch (error: any) {
        setErrorMessage(error.message || 'Failed to fetch game details.');
      } finally {
        setLoading(false);
      }
    };

    fetchGame();
  }, [id]);

  const handleUpdateGame = async () => {
    if (
      parseInt(updateFields.quantityInStock) < 0 ||
      !updateFields.quantityInStock.trim()
    ) {
      setErrorMessage('Quantity cannot be negative or empty.');
      return;
    }

    if (
      parseFloat(updateFields.price) < 0 ||
      updateFields.price.trim() === ''
    ) {
      setErrorMessage('Price cannot be negative or empty.');
      return;
    }

    try {
      if (id) {
        await updateGame(parseInt(id), {
          name: updateFields.name,
          description: updateFields.description,
          imageURL: updateFields.imageUrl,
          price: parseFloat(updateFields.price),
          quantityInStock: parseInt(updateFields.quantityInStock),
          isAvailable: game?.available,
        });
        setSuccessMessage('Game details updated successfully.');
        setErrorMessage(null);
      }
    } catch (error: any) {
      setErrorMessage(error.message || 'Failed to update game details.');
    }
  };

  const handleArchiveOrReactivate = async () => {
    try {
      if (id && game) {
        if (game.available) {
          await archiveGame(parseInt(id));
          setGame({ ...game, available: false });
          setSuccessMessage('Game has been deactivated successfully.');
        } else {
          await reactivateGame(parseInt(id));
          setGame({ ...game, available: true });
          setSuccessMessage('Game has been reactivated successfully.');
        }
        setErrorMessage(null);
      }
    } catch (error: any) {
      setErrorMessage(
        error.message ||
          `Failed to ${game?.available ? 'deactivate' : 'reactivate'} the game.`
      );
    }
  };

  const handleAssignCategory = async () => {
    try {
      if (id && categoryId.trim()) {
        await assignGameToCategory(parseInt(id), parseInt(categoryId));
        setSuccessMessage('Game assigned to category successfully.');
        setErrorMessage(null);
        setCategoryId('');
      } else {
        setErrorMessage('Category ID cannot be empty.');
      }
    } catch (error: any) {
      setErrorMessage(error.message || 'Failed to assign game to category.');
    }
  };

  const handleAssignPromotion = async () => {
    try {
      if (id && promotionId.trim()) {
        await assignPromotionToGame(parseInt(id), parseInt(promotionId));
        setSuccessMessage('Game assigned to promotion successfully.');
        setErrorMessage(null);
        setPromotionId('');
      } else {
        setErrorMessage('Promotion ID cannot be empty.');
      }
    } catch (error: any) {
      setErrorMessage(error.message || 'Failed to assign game to promotion.');
    }
  };

  if (loading) return <p>Loading game details...</p>;

  return (
    <div className="max-w-4xl mx-auto p-8 bg-gray-100 rounded-lg shadow-lg">
      <div className="flex items-center mb-6">
        <button
          onClick={() => navigate('/manager/games')}
          className="bg-gray-100 text-gray-700 px-4 py-2 rounded-md shadow hover:bg-gray-200 transition"
        >
          Back
        </button>
        <h2 className="ml-4 text-2xl font-bold tracking-tight text-gray-900">
          Game Details
        </h2>
      </div>

      {successMessage && (
        <p className="bg-green-100 text-green-800 p-3 rounded mb-4">
          {successMessage}
        </p>
      )}
      {errorMessage && (
        <p className="bg-red-100 text-red-800 p-3 rounded mb-4">
          {errorMessage}
        </p>
      )}

      {game && (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-lg font-medium text-gray-800 mb-4">
              Game Information
            </h2>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-600">
                  Name
                </label>
                <input
                  type="text"
                  value={updateFields.name}
                  onChange={(e) =>
                    setUpdateFields({ ...updateFields, name: e.target.value })
                  }
                  className="w-full border border-gray-300 rounded-md p-2 focus:ring-blue-500 focus:border-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-600">
                  Description
                </label>
                <textarea
                  value={updateFields.description}
                  onChange={(e) =>
                    setUpdateFields({
                      ...updateFields,
                      description: e.target.value,
                    })
                  }
                  className="w-full border border-gray-300 rounded-md p-2 focus:ring-blue-500 focus:border-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-600">
                  Image URL
                </label>
                <input
                  type="text"
                  value={updateFields.imageUrl}
                  onChange={(e) =>
                    setUpdateFields({
                      ...updateFields,
                      imageUrl: e.target.value,
                    })
                  }
                  className="w-full border border-gray-300 rounded-md p-2 focus:ring-blue-500 focus:border-blue-500"
                />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-600">
                    Price
                  </label>
                  <input
                    type="number"
                    value={updateFields.price}
                    onChange={(e) =>
                      setUpdateFields({
                        ...updateFields,
                        price: e.target.value,
                      })
                    }
                    className="w-full border border-gray-300 rounded-md p-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-600">
                    Quantity
                  </label>
                  <input
                    type="number"
                    value={updateFields.quantityInStock}
                    onChange={(e) =>
                      setUpdateFields({
                        ...updateFields,
                        quantityInStock: e.target.value,
                      })
                    }
                    className="w-full border border-gray-300 rounded-md p-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
              </div>
            </div>
            <div className="mt-6 flex space-x-4">
              <button
                onClick={handleUpdateGame}
                className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition"
              >
                Update Game
              </button>
              <button
                onClick={handleArchiveOrReactivate}
                className={`${
                  game.available ? 'bg-red-500' : 'bg-green-500'
                } text-white px-4 py-2 rounded-md hover:opacity-90 transition`}
              >
                {game.available ? 'Deactivate' : 'Reactivate'}
              </button>
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-lg font-medium text-gray-800 mb-4">
              Assign Actions
            </h2>
            <div className="space-y-6">
              <div>
                <h3 className="text-sm font-medium text-gray-600 mb-2">
                  Assign to Category
                </h3>
                <div className="flex gap-2">
                  <input
                    type="text"
                    value={categoryId}
                    onChange={(e) => setCategoryId(e.target.value)}
                    placeholder="Category ID"
                    className="flex-1 border border-gray-300 rounded-md p-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                  <button
                    onClick={handleAssignCategory}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
                  >
                    Assign
                  </button>
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium text-gray-600 mb-2">
                  Assign to Promotion
                </h3>
                <div className="flex gap-2">
                  <input
                    type="text"
                    value={promotionId}
                    onChange={(e) => setPromotionId(e.target.value)}
                    placeholder="Promotion ID"
                    className="flex-1 border border-gray-300 rounded-md p-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                  <button
                    onClick={handleAssignPromotion}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
                  >
                    Assign
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ManagerGameDetailPage;
