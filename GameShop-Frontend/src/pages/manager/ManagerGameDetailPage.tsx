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
import { Game } from '../../api/gameService';

const ManagerGameDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [game, setGame] = useState<Game | null>(null);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [updateFields, setUpdateFields] = useState({
    name: '',
    description: '',
    imageUrl: '',
    price: '',
    quantityInStock: '',
  });
  const [categoryId, setCategoryId] = useState('');
  const [promotionId, setPromotionId] = useState('');
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

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
          setGame({
            ...game,
            available: false,
          });
          setSuccessMessage('Game has been deactivated successfully.');
        } else {
          await reactivateGame(parseInt(id));
          setGame({
            ...game,
            available: true,
          });
          setSuccessMessage('Game has been reactivated successfully.');
        }
        setErrorMessage(null);
      }
    } catch (error: any) {
      setErrorMessage(error.message || `Failed to ${game?.available ? 'deactivate' : 'reactivate'} the game.`);
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
    <div className="p-6">
      <div className="flex items-center mb-4">
        <button
          onClick={() => navigate('/manager/games')}
          className="bg-gray-200 text-gray-800 px-4 py-2 rounded mr-4"
        >
          Back
        </button>
        <h2 className="text-2xl font-bold tracking-tight text-gray-900">Game Details</h2>
      </div>

      {successMessage && <p className="text-green-600">{successMessage}</p>}
      {errorMessage && <p className="text-red-600">{errorMessage}</p>}

      {game && (
        <div>
          <div>
            <label>Name:</label>
            <input
              type="text"
              value={updateFields.name}
              onChange={(e) => setUpdateFields({ ...updateFields, name: e.target.value })}
              className="w-full border rounded-md p-2"
            />
          </div>
          <div>
            <label>Description:</label>
            <textarea
              value={updateFields.description}
              onChange={(e) => setUpdateFields({ ...updateFields, description: e.target.value })}
              className="w-full border rounded-md p-2"
            />
          </div>
          <div>
            <label>Image URL:</label>
            <input
              type="text"
              value={updateFields.imageUrl}
              onChange={(e) => setUpdateFields({ ...updateFields, imageUrl: e.target.value })}
              className="w-full border rounded-md p-2"
            />
          </div>
          <div>
            <label>Price:</label>
            <input
              type="number"
              value={updateFields.price}
              onChange={(e) => setUpdateFields({ ...updateFields, price: e.target.value })}
              className="w-full border rounded-md p-2"
            />
          </div>
          <div>
            <label>Quantity in Stock:</label>
            <input
              type="number"
              value={updateFields.quantityInStock}
              onChange={(e) => setUpdateFields({ ...updateFields, quantityInStock: e.target.value })}
              className="w-full border rounded-md p-2"
            />
          </div>
          <button
            onClick={handleUpdateGame}
            className="mt-4 bg-blue-500 text-white px-4 py-2 rounded"
          >
            Update Game
          </button>
          <button
            onClick={handleArchiveOrReactivate}
            className={`mt-4 ml-4 ${
              game.available ? 'bg-red-500' : 'bg-green-500'
            } text-white px-4 py-2 rounded`}
          >
            {game.available ? 'Deactivate' : 'Reactivate'}
          </button>
          <div className="mt-6">
            <h3 className="font-bold">Assign to Category</h3>
            <input
              type="text"
              value={categoryId}
              onChange={(e) => setCategoryId(e.target.value)}
              placeholder="Category ID"
              className="w-full border rounded-md p-2 mt-2"
            />
            <button
              onClick={handleAssignCategory}
              className="mt-2 bg-blue-500 text-white px-4 py-2 rounded"
            >
              Assign Category
            </button>
          </div>
          <div className="mt-6">
            <h3 className="font-bold">Assign to Promotion</h3>
            <input
              type="text"
              value={promotionId}
              onChange={(e) => setPromotionId(e.target.value)}
              placeholder="Promotion ID"
              className="w-full border rounded-md p-2 mt-2"
            />
            <button
              onClick={handleAssignPromotion}
              className="mt-2 bg-blue-500 text-white px-4 py-2 rounded"
            >
              Assign Promotion
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default ManagerGameDetailPage;
