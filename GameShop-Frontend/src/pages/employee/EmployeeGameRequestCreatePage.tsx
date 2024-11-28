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
        if (!authUser || !authUser.id) {
          console.error('No authenticated user found or user ID is missing.');
          setErrorMessage('Failed to retrieve employee ID. Please log in again.');
          return;
        }
        setStaffId(authUser.id);
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
        staffId, // Use the retrieved employee ID
        categoryIds,
      });
      navigate('/employee/game-requests'); // Redirect to game requests page
    } catch (error) {
      console.error('Error creating game request:', error);
      setErrorMessage('Failed to create game request. Please check your input.');
    }
  };

  return (
    <div className="p-6">
      <h2 className="mb-6 text-2xl font-bold tracking-tight text-gray-900">Create New Game Request</h2>
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
          Create Game Request
        </button>
      </form>
    </div>
  );
};

export default EmployeeGameRequestCreatePage;