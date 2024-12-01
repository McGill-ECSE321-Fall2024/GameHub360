import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllGames, getArchivedGames } from '../../api/gameService';
import { ManagerRouteNames } from '../../model/routeNames/ManagerRouteNames';
import { Game } from '../../model/manager/Game';

const ManagerGamesPage = () => {
  const [games, setGames] = useState<Game[]>([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [filter, setFilter] = useState<'ALL' | 'ARCHIVED' | 'ACTIVE'>('ALL');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchGames = async () => {
      setLoading(true);
      setErrorMessage(null);

      try {
        if (filter === 'ALL') {
          const allGames = await getAllGames();
          setGames(allGames);
        } else if (filter === 'ARCHIVED') {
          const archivedGames = await getArchivedGames();
          setGames(archivedGames);
        } else if (filter === 'ACTIVE') {
          const allGames = await getAllGames();
          const activeGames = allGames.filter(
            (game) => game.available === true
          );
          setGames(activeGames);
        }
      } catch (error) {
        console.error('Error fetching games:', error);
        setErrorMessage('Failed to load games.');
      } finally {
        setLoading(false);
      }
    };

    fetchGames();
  }, [filter]);

  return (
    <div className="p-6">
      <h2 className="mb-6 text-2xl font-bold tracking-tight text-gray-900">
        Games
      </h2>

      {errorMessage && <p className="text-red-600">{errorMessage}</p>}

      {/* Filter Buttons */}
      <div className="mb-4 flex justify-between">
        <div className="flex gap-4">
          <button
            className={`px-4 py-2 rounded-md ${
              filter === 'ALL'
                ? 'bg-blue-600 text-white'
                : 'bg-gray-200 text-gray-700'
            }`}
            onClick={() => setFilter('ALL')}
          >
            All Games
          </button>
          <button
            className={`px-4 py-2 rounded-md ${
              filter === 'ARCHIVED'
                ? 'bg-blue-600 text-white'
                : 'bg-gray-200 text-gray-700'
            }`}
            onClick={() => setFilter('ARCHIVED')}
          >
            Archived Games
          </button>
          <button
            className={`px-4 py-2 rounded-md ${
              filter === 'ACTIVE'
                ? 'bg-blue-600 text-white'
                : 'bg-gray-200 text-gray-700'
            }`}
            onClick={() => setFilter('ACTIVE')}
          >
            Active Games
          </button>
        </div>
        <button
          onClick={() => navigate(ManagerRouteNames.CREATE_GAME)}
          className="bg-blue-500 text-white px-4 py-2 rounded-md"
        >
          Create Game
        </button>
      </div>

      {loading ? (
        <p className="text-gray-700">Loading games...</p>
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
                  Description
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Price
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Quantity
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Status
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {games.map((game) => (
                <tr
                  key={game.gameId}
                  className="hover:bg-gray-100 cursor-pointer"
                  onClick={() =>
                    navigate(
                      ManagerRouteNames.GAME_DETAIL.replace(
                        ':id',
                        game.gameId.toString()
                      )
                    )
                  }
                >
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {game.gameId}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {game.name}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {game.description}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    ${game.price.toFixed(2)}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {game.quantityInStock}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {game.available ? 'Active' : 'Archived'}
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

export default ManagerGamesPage;
