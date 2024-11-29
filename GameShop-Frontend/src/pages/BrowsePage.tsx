import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import apiService from '../config/axiosConfig';
import GameCard from '../components/GameCard';

interface FilterState {
  category?: string;
  priceRange?: [number, number];
  name?: string;
  categoryType?: 'GENRE' | 'CONSOLE';
}

interface Game {
  gameId: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  quantityInStock: number;
  categoryIds: number[];
}

interface Category {
  categoryId: number;
  name: string;
  categoryType: 'GENRE' | 'CONSOLE';
  available: boolean;
}

const BrowsePage = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [filters, setFilters] = useState<FilterState>({
    category: searchParams.get('category') || undefined,
    priceRange: searchParams.get('price_range')?.split('-').map(Number) as [number, number] || undefined,
    name: searchParams.get('name') || undefined,
    categoryType: (searchParams.get('categoryType') as 'GENRE' | 'CONSOLE') || undefined
  });
  const [games, setGames] = useState<Game[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const { data } = await apiService.get('/categories');
        setCategories(data.gameCategories);
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
    if (filters.categoryType) params.set('categoryType', filters.categoryType);
    if (filters.priceRange) params.set('price_range', filters.priceRange.join('-'));
    if (filters.name) params.set('name', filters.name);
    setSearchParams(params);

    const fetchGames = async () => {
      try {
        const queryParams = {
          category: filters.category || undefined,
          categoryType: filters.categoryType || undefined,
          minPrice: filters.priceRange?.[0],
          maxPrice: filters.priceRange?.[1],
        };

        const endpoint = filters.name ? '/games/search' : '/games';
        const { data } = await apiService.get(endpoint, { 
          params: filters.name ? { ...queryParams, query: filters.name } : queryParams 
        });
        
        console.log('Fetched games:', data.games);
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

          {/* Category Type Filter */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Category Type
            </label>
            <select
              value={filters.categoryType || ''}
              onChange={(e) => setFilters({ 
                ...filters, 
                categoryType: (e.target.value as 'GENRE' | 'CONSOLE' | '') || undefined,
                category: undefined // Reset category when changing type
              })}
              className="w-full p-3 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all"
            >
              <option value="">All Types</option>
              <option value="GENRE">Genre</option>
              <option value="CONSOLE">Console</option>
            </select>
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
              {categories
                .filter(category => !filters.categoryType || category.categoryType === filters.categoryType)
                .map(category => (
                  <option key={category.categoryId} value={category.name}>
                    {category.name}
                  </option>
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
      {games.length === 0 ? (
        <div className="text-center py-8">
          <h3 className="text-xl text-gray-600">No games found matching your filters</h3>
          <button
            onClick={() => setFilters({})}
            className="mt-4 px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
          >
            Clear Filters
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {games
            .filter(game => {
              if (!filters.categoryType) return true;
              return game.categoryIds?.some(categoryId => {
                const category = categories.find(c => c.categoryId === categoryId);
                return category?.categoryType === filters.categoryType;
              });
            })
            .map((game) => (
              <GameCard key={game.gameId} game={game} />
            ))}
        </div>
      )}
    </div>
  );
};

export default BrowsePage;
