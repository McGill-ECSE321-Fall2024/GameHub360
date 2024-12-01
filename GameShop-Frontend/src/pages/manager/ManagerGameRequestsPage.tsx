import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllGameRequests } from '../../api/gameRequestService';
import { ManagerRouteNames } from '../../model/routeNames/ManagerRouteNames';
import { GameRequest } from '../../model/manager/GameRequest';

const GameRequestsPage = () => {
  const [gameRequests, setGameRequests] = useState<GameRequest[]>([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [filter, setFilter] = useState<
    'ALL' | 'SUBMITTED' | 'APPROVED' | 'REFUSED'
  >('ALL');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRequests = async () => {
      try {
        const requests = await getAllGameRequests();
        setGameRequests(requests);
      } catch (error) {
        console.error('Error fetching game requests:', error);
        setErrorMessage('Failed to load game requests.');
      } finally {
        setLoading(false);
      }
    };

    fetchRequests();
  }, []);

  const filteredRequests = gameRequests.filter((request) => {
    if (filter === 'ALL') return true;
    return request.requestStatus === filter;
  });

  return (
    <div className="p-6">
      <h2 className="mb-6 text-2xl font-bold tracking-tight text-gray-900">
        Game Requests
      </h2>

      {errorMessage && <p className="text-red-600">{errorMessage}</p>}

      {/* Filter Buttons */}
      <div className="mb-4 flex gap-4">
        {['ALL', 'SUBMITTED', 'APPROVED', 'REFUSED'].map((status) => (
          <button
            key={status}
            className={`px-4 py-2 rounded-md ${
              filter === status
                ? 'bg-blue-600 text-white'
                : 'bg-gray-200 text-gray-700'
            }`}
            onClick={() =>
              setFilter(status as 'ALL' | 'SUBMITTED' | 'APPROVED' | 'REFUSED')
            }
          >
            {status}
          </button>
        ))}
      </div>

      {loading ? (
        <p className="text-gray-700">Loading game requests...</p>
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
                  Status
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Requested By
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                  Request Date
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {filteredRequests.map((request) => (
                <tr
                  key={request.id}
                  className="hover:bg-gray-100 cursor-pointer"
                  onClick={() =>
                    navigate(
                      ManagerRouteNames.GAME_REQUEST_DETAIL.replace(
                        ':id',
                        request.id.toString()
                      )
                    )
                  }
                >
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {request.id}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {request.name}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {request.description}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {request.requestStatus}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {request.staffId}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {new Date(request.requestDate).toLocaleDateString()}
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

export default GameRequestsPage;
