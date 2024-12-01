import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getAllCategories } from '../../api/categoryService';
import { ManagerRouteNames } from '../../model/routeNames/ManagerRouteNames';
import { Category } from '../../model/manager/Category';

const ManagerCategoriesPage = () => {
  const [categories, setCategories] = useState<Category[]>([]);
  const [filteredCategories, setFilteredCategories] = useState<Category[]>([]);
  const [filter, setFilter] = useState<'ALL' | 'GENRE' | 'CONSOLE'>('ALL'); // Filter by category type or show all
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCategories = async () => {
      setLoading(true);
      setErrorMessage(null);

      try {
        const data = await getAllCategories();
        setCategories(data);
        setFilteredCategories(data);
      } catch (error: any) {
        setErrorMessage(error.message || 'Failed to load categories.');
      } finally {
        setLoading(false);
      }
    };

    fetchCategories();
  }, []);

  // Update filtered categories whenever the filter changes
  useEffect(() => {
    if (filter === 'ALL') {
      setFilteredCategories(categories);
    } else {
      setFilteredCategories(
        categories.filter((cat) => cat.categoryType === filter)
      );
    }
  }, [filter, categories]);

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-2xl font-bold tracking-tight text-gray-900">
          Categories
        </h2>
        <Link
          to={ManagerRouteNames.CREATE_CATEGORY}
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          Create Category
        </Link>
      </div>

      {errorMessage && <p className="text-red-600">{errorMessage}</p>}

      <div className="mb-4">
        <button
          className={`px-4 py-2 rounded-md ${
            filter === 'ALL'
              ? 'bg-blue-600 text-white'
              : 'bg-gray-200 text-gray-700'
          }`}
          onClick={() => setFilter('ALL')}
        >
          All
        </button>
        <button
          className={`px-4 py-2 rounded-md ml-2 ${
            filter === 'GENRE'
              ? 'bg-blue-600 text-white'
              : 'bg-gray-200 text-gray-700'
          }`}
          onClick={() => setFilter('GENRE')}
        >
          Genre
        </button>
        <button
          className={`px-4 py-2 rounded-md ml-2 ${
            filter === 'CONSOLE'
              ? 'bg-blue-600 text-white'
              : 'bg-gray-200 text-gray-700'
          }`}
          onClick={() => setFilter('CONSOLE')}
        >
          Console
        </button>
      </div>

      {loading ? (
        <p className="text-gray-700">Loading categories...</p>
      ) : (
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead>
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  ID
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Name
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Type
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Status
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {filteredCategories.map((category) => (
                <tr
                  key={category.categoryId}
                  className="hover:bg-gray-100 cursor-pointer"
                  onClick={() =>
                    navigate(`/manager/categories/${category.categoryId}`)
                  }
                >
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {category.categoryId}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {category.name}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {category.categoryType}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {category.available ? 'Available' : 'Unavailable'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default ManagerCategoriesPage;
