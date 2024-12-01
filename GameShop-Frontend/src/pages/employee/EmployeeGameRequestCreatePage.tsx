import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { createGameRequest } from '../../api/gameRequestService';
import { getAuthUser } from '../../state/authState';

const EmployeeGameRequestCreatePage = () => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    imageUrl: '',
    categoryIds: '',
  });
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [staffId, setStaffId] = useState<number | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchAuthUser = async () => {
      try {
        const authUser = getAuthUser();
        if (!authUser || !authUser.staffId) {
          console.error('No authenticated user found or user ID is missing.');
          setErrorMessage('Failed to retrieve employee ID. Please log in again.');
          return;
        }
        setStaffId(authUser.staffId);
      } catch (error) {
        console.error('Error retrieving authenticated user:', error);
        setErrorMessage('Failed to retrieve employee ID. Please log in again.');
      }
    };

    fetchAuthUser();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!staffId) {
      setErrorMessage('Failed to retrieve employee ID. Please log in again.');
      return;
    }

    try {
      const categoryIds = formData.categoryIds.split(',').map((id) => parseInt(id.trim()));
      await createGameRequest({
        name: formData.name,
        description: formData.description,
        imageUrl: formData.imageUrl,
        requestDate: new Date().toISOString(),
        staffId,
        categoryIds,
      });
      navigate('/employee/game-requests'); // Redirect to game requests page
    } catch (error) {
      console.error('Error creating game request:', error);
      setErrorMessage('Failed to create game request. Please check your input.');
    }
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h2 className="mb-6 text-3xl font-semibold text-gray-800">Create New Game Request</h2>
      {errorMessage && (
        <div className="mb-4 p-4 text-red-700 bg-red-100 rounded-lg">{errorMessage}</div>
      )}
      <form onSubmit={handleSubmit} className="space-y-6 bg-white p-6 shadow-md rounded-lg">
        <div className="space-y-1">
          <label className="block text-gray-700 font-medium">Name:</label>
          <input
            type="text"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            className="w-full border border-gray-300 rounded-md p-3 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Enter the name"
            required
          />
        </div>
        <div className="space-y-1">
          <label className="block text-gray-700 font-medium">Description:</label>
          <textarea
            value={formData.description}
            onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            className="w-full border border-gray-300 rounded-md p-3 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Enter the description"
            rows={4}
            required
          />
        </div>
        <div className="space-y-1">
          <label className="block text-gray-700 font-medium">Image URL:</label>
          <input
            type="text"
            value={formData.imageUrl}
            onChange={(e) => setFormData({ ...formData, imageUrl: e.target.value })}
            className="w-full border border-gray-300 rounded-md p-3 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Enter the image URL"
            required
          />
        </div>
        <div className="space-y-1">
          <label className="block text-gray-700 font-medium">Category IDs (comma-separated):</label>
          <input
            type="text"
            value={formData.categoryIds}
            onChange={(e) => setFormData({ ...formData, categoryIds: e.target.value })}
            className="w-full border border-gray-300 rounded-md p-3 text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Enter category IDs (e.g., 1, 2, 3)"
            required
          />
        </div>
        <button
          type="submit"
          className="w-full py-3 px-5 bg-blue-500 text-white font-medium text-lg rounded-lg shadow-md hover:bg-blue-600 transition duration-200"
        >
          Create Game Request
        </button>
      </form>
      <button
        onClick={() => navigate('/employee/game-requests')}
        className="mt-4 w-full py-3 px-5 bg-gray-500 text-white font-medium text-lg rounded-lg shadow-md hover:bg-gray-600 transition duration-200"
      >
        Back
      </button>
    </div>
  );
};

export default EmployeeGameRequestCreatePage;