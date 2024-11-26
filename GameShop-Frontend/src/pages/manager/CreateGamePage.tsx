import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createGame } from '../../api/gameService';

const ManagerCreateGamePage = () => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    imageUrl: '',
    quantityInStock: '',
    isAvailable: true,
    price: '',
    categoryIds: '',
  });
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (parseFloat(formData.price) < 0 || parseInt(formData.quantityInStock) < 0) {
      setErrorMessage('Price and Quantity cannot be negative.');
      return;
    }

    try {
      const categoryIds = formData.categoryIds.split(',').map((id) => parseInt(id.trim()));
      await createGame({
        name: formData.name,
        description: formData.description,
        imageURL: formData.imageUrl,
        quantityInStock: parseInt(formData.quantityInStock),
        isAvailable: formData.isAvailable,
        price: parseFloat(formData.price),
        categoryIds,
      });
      navigate('/manager/games'); // Redirect to games page
    } catch (error) {
      console.error('Error creating game:', error);
      setErrorMessage('Failed to create game. Please check your input.');
    }
  };

  return (
    <div className="p-6">
      <h2 className="mb-6 text-2xl font-bold tracking-tight text-gray-900">Create New Game</h2>
      {errorMessage && <p className="text-red-600">{errorMessage}</p>}
      <form onSubmit={handleSubmit}>
        <div>
          <label>Name:</label>
          <input
            type="text"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            className="w-full border rounded-md p-2"
            required
          />
        </div>
        <div>
          <label>Description:</label>
          <textarea
            value={formData.description}
            onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            className="w-full border rounded-md p-2"
            required
          />
        </div>
        <div>
          <label>Image URL:</label>
          <input
            type="text"
            value={formData.imageUrl}
            onChange={(e) => setFormData({ ...formData, imageUrl: e.target.value })}
            className="w-full border rounded-md p-2"
            required
          />
        </div>
        <div>
          <label>Quantity in Stock:</label>
          <input
            type="number"
            value={formData.quantityInStock}
            onChange={(e) => setFormData({ ...formData, quantityInStock: e.target.value })}
            className="w-full border rounded-md p-2"
            required
          />
        </div>
        <div>
          <label>Price:</label>
          <input
            type="number"
            value={formData.price}
            onChange={(e) => setFormData({ ...formData, price: e.target.value })}
            className="w-full border rounded-md p-2"
            required
          />
        </div>
        <div>
          <label>Category IDs (comma-separated):</label>
          <input
            type="text"
            value={formData.categoryIds}
            onChange={(e) => setFormData({ ...formData, categoryIds: e.target.value })}
            className="w-full border rounded-md p-2"
            required
          />
        </div>
        <button type="submit" className="mt-4 bg-green-500 text-white px-4 py-2 rounded">
          Create Game
        </button>
      </form>
    </div>
  );
};

export default ManagerCreateGamePage;
