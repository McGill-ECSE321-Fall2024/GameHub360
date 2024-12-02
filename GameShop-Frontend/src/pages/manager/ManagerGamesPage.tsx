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
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-semibold text-gray-800">Games</h2>
        <button
          onClick={() => navigate(ManagerRouteNames.CREATE_GAME)}
          className="bg-blue-500 hover:bg-blue-600 text-white font-medium px-5 py-2 rounded-lg shadow-md transition duration-200"
        >
          Create Game
        </button>
      </div>

      {errorMessage && (
        <div className="mb-4 p-4 text-red-700 bg-red-100 rounded-lg">
          {errorMessage}
        </div>
      )}

      {/* Filter Buttons */}
      <div className="mb-6 flex space-x-4">
        {['ALL', 'ARCHIVED', 'ACTIVE'].map((status) => (
          <button
            key={status}
            className={`px-5 py-2 rounded-lg shadow-sm font-medium transition duration-200 ${
              filter === status
                ? 'bg-blue-500 text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            }`}
            onClick={() => setFilter(status as 'ALL' | 'ARCHIVED' | 'ACTIVE')}
          >
            {status}
          </button>
        ))}
      </div>

      {loading ? (
        <p className="text-gray-700 text-lg">Loading games...</p>
      ) : games.length === 0 ? (
        <p className="text-gray-700 text-lg">
          No games found for the selected filter.
        </p>
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
                  Description
                </th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  Price
                </th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  Quantity
                </th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  Status
                </th>
              </tr>
            </thead>
            <tbody>
              {games.map((game) => (
                <tr
                  key={game.gameId}
                  className="border-b hover:bg-gray-50 cursor-pointer transition"
                  onClick={() =>
                    navigate(
                      ManagerRouteNames.GAME_DETAIL.replace(
                        ':id',
                        game.gameId.toString()
                      )
                    )
                  }
                >
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {game.gameId}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {game.name}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {game.description}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
                    ${game.price.toFixed(2)}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {game.quantityInStock}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
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
