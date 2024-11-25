import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import apiService from '../config/axiosConfig';
import GameCard from '../components/GameCard';

interface FilterState {
  category?: string;
  priceRange?: [number, number];
  name?: string;
}

interface Game {
  gameId: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  quantityInStock: number;
}

const BrowsePage = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [filters, setFilters] = useState<FilterState>({
    category: searchParams.get('category') || undefined,
    priceRange: searchParams.get('price_range')?.split('-').map(Number) as [number, number] || undefined,
    name: searchParams.get('name') || undefined
  });
  const [games, setGames] = useState<Game[]>([]);
  const [categories, setCategories] = useState<string[]>([]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const { data } = await apiService.get('/categories');
        const categoryNames = data.gameCategories.map((category: { name: string }) => category.name);
        setCategories(categoryNames);
      } catch (error) {
        console.error('Error fetching categories:', error);
        setCategories([]);
      }
    };
    fetchCategories();
  }, []);

  useEffect(() => {
    const params = new URLSearchParams();
    if (filters.category) params.set('category', filters.category);
    if (filters.priceRange) params.set('price_range', filters.priceRange.join('-'));
    if (filters.name) params.set('name', filters.name);
    setSearchParams(params);

    const fetchGames = async () => {
      try {
        const queryParams = {
          category: filters.category || undefined,
          minPrice: filters.priceRange?.[0],
          maxPrice: filters.priceRange?.[1],
        };

        const endpoint = filters.name ? '/games/search' : '/games';
        const { data } = await apiService.get(endpoint, { 
          params: filters.name ? { ...queryParams, query: filters.name } : queryParams 
        });
        
        setGames(data.games);
      } catch (error) {
        console.error('Error fetching games:', error);
      }
    };
    fetchGames();
  }, [filters, setSearchParams]);

  return (
    <div className="container mx-auto px-4 py-8">
      {/* Filter Section */}
      <div className="mb-8 bg-white rounded-xl shadow-lg p-6">
        <h2 className="text-2xl font-bold mb-6">Find Your Perfect Game</h2>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Search by Name */}
          <div className="col-span-full">
            <div className="relative">
              <input
                type="text"
                placeholder="Search games..."
                value={filters.name || ''}
                onChange={(e) => setFilters({ ...filters, name: e.target.value || undefined })}
                className="w-full p-3 pl-10 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all"
              />
              <svg 
                className="absolute left-3 top-3.5 h-5 w-5 text-gray-400"
                fill="none" 
                stroke="currentColor" 
                viewBox="0 0 24 24"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
          </div>

          {/* Category Filter */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Category
            </label>
            <select
              value={filters.category || ''}
              onChange={(e) => setFilters({ ...filters, category: e.target.value || undefined })}
              className="w-full p-3 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all"
            >
              <option value="">All Categories</option>
              {categories.map(category => (
                <option key={category} value={category}>{category}</option>
              ))}
            </select>
          </div>

          {/* Price Range */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Price Range: ${filters.priceRange?.[0] || 0} - ${filters.priceRange?.[1] || 200}
            </label>
            <input
              type="range"
              min="0"
              max="200"
              step="10"
              value={filters.priceRange?.[1] || 200}
              onChange={(e) => setFilters({ 
                ...filters, 
                priceRange: [0, parseInt(e.target.value)] 
              })}
              className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer accent-blue-500"
            />
            <div className="flex justify-between text-xs text-gray-500 mt-1">
              <span>$0</span>
              <span>$200</span>
            </div>
          </div>
        </div>

        {/* Reset Filters Button */}
        <div className="mt-6 flex justify-end">
          <button
            onClick={() => setFilters({})}
            className="px-4 py-2 text-sm text-gray-600 hover:text-gray-800 transition-colors"
          >
            Reset Filters
          </button>
        </div>
      </div>

      {/* Games Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {games.map((game) => (
          <GameCard key={game.gameId} game={game} />
        ))}
      </div>
    </div>
  );
};

export default BrowsePage;
