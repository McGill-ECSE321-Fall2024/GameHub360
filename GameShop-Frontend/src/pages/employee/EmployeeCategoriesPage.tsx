import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllCategories } from '../../api/categoryService';
import { Category } from '../../model/manager/Category';

const EmployeeCategoriesPage = () => {
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
      setFilteredCategories(categories.filter((cat) => cat.categoryType === filter));
    }
  }, [filter, categories]);

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      {errorMessage && (
        <div className="mb-4 p-4 text-red-700 bg-red-100 rounded-lg">
          {errorMessage}
        </div>
      )}

      {/* Filter Buttons */}
      <div className="mb-6 flex space-x-4">
        {['ALL', 'GENRE', 'CONSOLE'].map((type) => (
          <button
            key={type}
            className={`px-5 py-2 rounded-lg shadow-sm font-medium transition duration-200 ${
              filter === type
                ? 'bg-blue-500 text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            }`}
            onClick={() => setFilter(type as 'ALL' | 'GENRE' | 'CONSOLE')}
          >
            {type}
          </button>
        ))}
      </div>

      {loading ? (
        <p className="text-gray-700 text-lg">Loading categories...</p>
      ) : filteredCategories.length === 0 ? (
        <p className="text-gray-700 text-lg">No categories found for the selected filter.</p>
      ) : (
        <div className="overflow-x-auto bg-white rounded-lg shadow-md">
          <table className="min-w-full border-collapse">
            <thead className="bg-gray-100 border-b">
              <tr>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  ID
                </th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  Name
                </th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  Type
                </th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  Status
                </th>
              </tr>
            </thead>
            <tbody>
              {filteredCategories.map((category) => (
                <tr
                  key={category.categoryId}
                  className="border-b hover:bg-gray-50 cursor-pointer transition"
                  onClick={() => navigate(`/employee/categories/${category.categoryId}`)}
                >
                  <td className="px-6 py-4 text-sm text-gray-800">{category.categoryId}</td>
                  <td className="px-6 py-4 text-sm text-gray-800">{category.name}</td>
                  <td className="px-6 py-4 text-sm text-gray-800">{category.categoryType}</td>
                  <td className="px-6 py-4 text-sm text-gray-800">
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

export default EmployeeCategoriesPage;