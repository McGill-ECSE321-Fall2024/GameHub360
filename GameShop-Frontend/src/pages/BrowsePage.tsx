import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import apiService from '../config/axiosConfig';
import GameCard from '../components/GameCard';

interface FilterState {
  category?: string;
  console?: string;
  priceRange?: [number, number];
  name?: string;
}

interface Game {
  gameId: number;
  name: string;
  price: number;
  imageUrl: string;
  console?: string;
  category?: string;
}

const BrowsePage = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [filters, setFilters] = useState<FilterState>({
    category: searchParams.get('category') || undefined,
    console: searchParams.get('console') || undefined,
    priceRange: searchParams.get('price_range')?.split('-').map(Number) as [number, number] || undefined,
    name: searchParams.get('name') || undefined
  });
  const [games, setGames] = useState<Game[]>([]);
  const [categories, setCategories] = useState<string[]>([]);
  const [consoles, setConsoles] = useState<string[]>([]);

  useEffect(() => {
    const fetchFilters = async () => {
      try {
        const [categoriesRes, consolesRes] = await Promise.all([
          apiService.get('/games/categories'),
          apiService.get('/games/consoles')
        ]);
        setCategories(categoriesRes.data.categories);
        setConsoles(consolesRes.data.consoles);
      } catch (error) {
        console.error('Error fetching filters:', error);
      }
    };
    fetchFilters();
  }, []);

  useEffect(() => {
    // Update URL when filters change
    const params = new URLSearchParams();
    if (filters.category) params.set('category', filters.category);
    if (filters.console) params.set('console', filters.console);
    if (filters.priceRange) params.set('price_range', filters.priceRange.join('-'));
    if (filters.name) params.set('name', filters.name);
    setSearchParams(params);

    // Fetch filtered games
    const fetchGames = async () => {
      try {
        const params = {
          category: filters.category || undefined,
          console: filters.console || undefined,
          minPrice: filters.priceRange?.[0],
          maxPrice: filters.priceRange?.[1],
        };

        let response;
        if (filters.name) {
          response = await apiService.get('/games/search', { 
            params: { ...params, query: filters.name }
          });
        } else {
          response = await apiService.get('/games', { params });
        }
        
        setGames(response.data.games);
      } catch (error) {
        console.error('Error fetching games:', error);
      }
    };
    fetchGames();
  }, [filters, setSearchParams]);

  return (
    <div className="container mx-auto px-4 py-8">
      {/* Filter Section */}
      <div className="mb-8 p-4 bg-gray-100 rounded-lg">
        <h2 className="text-xl font-bold mb-4">Filters</h2>
        {/* Category Filter */}
        <select
          value={filters.category || ''}
          onChange={(e) => setFilters({ ...filters, category: e.target.value || undefined })}
          className="mb-4 p-2 rounded border"
        >
          <option value="">All Categories</option>
          {categories.map(category => (
            <option key={category} value={category}>{category}</option>
          ))}
        </select>

        {/* Console Filter */}
        <select
          value={filters.console || ''}
          onChange={(e) => setFilters({ ...filters, console: e.target.value || undefined })}
          className="mb-4 p-2 rounded border"
        >
          <option value="">All Consoles</option>
          {consoles.map(console => (
            <option key={console} value={console}>{console}</option>
          ))}
        </select>

        {/* Price Range Slider */}
        <div className="mb-4">
          <label className="block mb-2">Price Range: ${filters.priceRange?.[0] || 0} - ${filters.priceRange?.[1] || 200}</label>
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
            className="w-full"
          />
        </div>

        {/* Search by Name */}
        <input
          type="text"
          placeholder="Search games..."
          value={filters.name || ''}
          onChange={(e) => setFilters({ ...filters, name: e.target.value || undefined })}
          className="p-2 rounded border w-full"
        />
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
