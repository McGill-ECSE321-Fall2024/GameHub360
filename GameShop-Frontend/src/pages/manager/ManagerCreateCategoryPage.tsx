import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createCategory } from '../../api/categoryService';

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
    <div className="max-w-3xl mx-auto p-6 bg-gray-50 shadow-md rounded-md">
      <div className="flex items-center mb-6">
        <button
          onClick={() => navigate('/manager/categories')}
          className="bg-gray-100 text-gray-700 px-4 py-2 rounded-md shadow hover:bg-gray-200 transition"
        >
          Back
        </button>
        <h2 className="ml-4 text-2xl font-bold tracking-tight text-gray-900">
          Create New Category
        </h2>
      </div>

      {successMessage && (
        <p className="mb-4 text-green-600 bg-green-50 p-3 rounded-md border border-green-500">
          {successMessage}
        </p>
      )}
      {errorMessage && (
        <p className="mb-4 text-red-600 bg-red-50 p-3 rounded-md border border-red-500">
          {errorMessage}
        </p>
      )}

      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Name:
          </label>
          <input
            type="text"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            className="w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 p-2"
            placeholder="Enter category name"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Category Type:
          </label>
          <select
            value={formData.categoryType}
            onChange={(e) =>
              setFormData({ ...formData, categoryType: e.target.value })
            }
            className="w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 p-2"
            required
          >
            <option value="" disabled>
              Select category type
            </option>
            <option value="GENRE">Genre</option>
            <option value="CONSOLE">Console</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Available:
          </label>
          <div className="flex items-center">
            <input
              type="checkbox"
              checked={formData.available}
              onChange={(e) =>
                setFormData({ ...formData, available: e.target.checked })
              }
              className="h-5 w-5 text-blue-500 border-gray-300 rounded focus:ring-blue-500"
            />
            <span className="ml-2 text-gray-700 text-sm">
              Mark as available
            </span>
          </div>
        </div>
        <div className="flex justify-start">
          <button
            type="submit"
            className="bg-blue-500 text-white px-6 py-2 rounded-md shadow-md hover:bg-blue-600 transition"
          >
            Create Category
          </button>
        </div>
      </form>
    </div>
  );
};

export default ManagerCreateCategoryPage;
