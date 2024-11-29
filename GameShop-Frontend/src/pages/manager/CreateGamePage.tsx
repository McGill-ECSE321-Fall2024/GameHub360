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

  // Handle form submission
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (
      parseFloat(formData.price) < 0 ||
      parseInt(formData.quantityInStock) < 0
    ) {
      setErrorMessage('Price and Quantity cannot be negative.');
      return;
    }

    // Split category IDs by comma and convert to array of integers
    try {
      const categoryIds = formData.categoryIds
        .split(',')
        .map((id) => parseInt(id.trim()));
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
    } catch (error: any) {
      setErrorMessage(error.message || 'Failed to create game.');
    }
  };

  return (
    <div className="max-w-3xl mx-auto p-6 bg-gray-50 shadow-md rounded-md">
      <div className="flex items-center mb-6">
        <button
          onClick={() => navigate('/manager/games')}
          className="bg-gray-100 text-gray-700 px-4 py-2 rounded-md shadow hover:bg-gray-200 transition"
        >
          Back
        </button>
        <h2 className="ml-4 text-2xl font-bold tracking-tight text-gray-900">
          Create New Game
        </h2>
      </div>

      {errorMessage && (
        <p className="mb-4 text-red-600 bg-red-50 p-3 rounded-md border border-red-500">
          {errorMessage}
        </p>
      )}

      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label className="block mb-2 text-sm font-medium text-gray-700">
            Name:
          </label>
          <input
            type="text"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            className="w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 p-2"
            placeholder="Enter game name"
            required
          />
        </div>
        <div>
          <label className="block mb-2 text-sm font-medium text-gray-700">
            Description:
          </label>
          <textarea
            value={formData.description}
            onChange={(e) =>
              setFormData({ ...formData, description: e.target.value })
            }
            className="w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 p-2"
            placeholder="Enter game description"
            rows={3}
            required
          ></textarea>
        </div>
        <div>
          <label className="block mb-2 text-sm font-medium text-gray-700">
            Image URL:
          </label>
          <input
            type="text"
            value={formData.imageUrl}
            onChange={(e) =>
              setFormData({ ...formData, imageUrl: e.target.value })
            }
            className="w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 p-2"
            placeholder="Enter image URL"
            required
          />
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Quantity in Stock:
            </label>
            <input
              type="number"
              value={formData.quantityInStock}
              onChange={(e) =>
                setFormData({ ...formData, quantityInStock: e.target.value })
              }
              className="w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 p-2"
              placeholder="Enter quantity"
              required
            />
          </div>
          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Price:
            </label>
            <input
              type="number"
              value={formData.price}
              onChange={(e) =>
                setFormData({ ...formData, price: e.target.value })
              }
              className="w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 p-2"
              placeholder="Enter price"
              required
            />
          </div>
        </div>
        <div>
          <label className="block mb-2 text-sm font-medium text-gray-700">
            Category IDs (comma-separated):
          </label>
          <input
            type="text"
            value={formData.categoryIds}
            onChange={(e) =>
              setFormData({ ...formData, categoryIds: e.target.value })
            }
            className="w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 p-2"
            placeholder="Enter category IDs (e.g., 1,2,3)"
            required
          />
        </div>
        <div className="flex items-center">
          <input
            type="checkbox"
            checked={formData.isAvailable}
            onChange={(e) =>
              setFormData({ ...formData, isAvailable: e.target.checked })
            }
            className="h-5 w-5 text-blue-500 border-gray-300 rounded focus:ring-blue-500"
          />
          <label className="ml-2 text-sm text-gray-700">Available</label>
        </div>
        <div className="flex justify-start">
          <button
            type="submit"
            className="bg-green-500 text-white px-6 py-2 rounded-md shadow-md hover:bg-green-600 transition"
          >
            Create Game
          </button>
        </div>
      </form>
    </div>
  );
};

export default ManagerCreateGamePage;
