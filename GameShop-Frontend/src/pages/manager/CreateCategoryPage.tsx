import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createCategory } from '../../api/gameCategoryService';

const ManagerCreateCategoryPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: '',
    categoryType: '', // 'GENRE' or 'CONSOLE'
    available: true,
  });
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.name.trim()) {
      setErrorMessage('Category name cannot be empty.');
      return;
    }

    if (!['GENRE', 'CONSOLE'].includes(formData.categoryType)) {
      setErrorMessage('Category type must be "GENRE" or "CONSOLE".');
      return;
    }

    try {
      await createCategory({
        name: formData.name,
        categoryType: formData.categoryType,
        available: formData.available,
      });
      setSuccessMessage('Category created successfully.');
      setErrorMessage(null);
      navigate('/manager/categories'); // Redirect to categories page
    } catch (error: any) {
      setErrorMessage(error.message || 'Failed to create category.');
    }
  };

  return (
    <div className="p-6">
      <div className="flex items-center mb-4">
        <button
          onClick={() => navigate('/manager/categories')}
          className="bg-gray-200 text-gray-800 px-4 py-2 rounded mr-4"
        >
          Back
        </button>
        <h2 className="text-2xl font-bold tracking-tight text-gray-900">Create New Category</h2>
      </div>

      {successMessage && <p className="text-green-600">{successMessage}</p>}
      {errorMessage && <p className="text-red-600">{errorMessage}</p>}

      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label className="block mb-2 text-sm font-medium text-gray-700">Name:</label>
          <input
            type="text"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            className="w-full border rounded-md p-2"
            placeholder="Category name"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2 text-sm font-medium text-gray-700">Category Type:</label>
          <select
            value={formData.categoryType}
            onChange={(e) => setFormData({ ...formData, categoryType: e.target.value })}
            className="w-full border rounded-md p-2"
            required
          >
            <option value="" disabled>
              Select category type
            </option>
            <option value="GENRE">Genre</option>
            <option value="CONSOLE">Console</option>
          </select>
        </div>
        <div className="mb-4">
          <label className="block mb-2 text-sm font-medium text-gray-700">Available:</label>
          <div className="flex items-center gap-2">
            <input
              type="checkbox"
              checked={formData.available}
              onChange={(e) => setFormData({ ...formData, available: e.target.checked })}
              className="h-5 w-5 text-blue-500 border-gray-300 rounded focus:ring-blue-500"
            />
            <span className="text-sm text-gray-700">Mark as available</span>
          </div>
        </div>
        <button
          type="submit"
          className="mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          Create Category
        </button>
      </form>
    </div>
  );
};

export default ManagerCreateCategoryPage;
