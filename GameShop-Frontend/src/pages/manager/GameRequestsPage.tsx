import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getAllGameRequests, GameRequest } from '../../api/gameRequestService';
import { ManagerRouteNames } from '../../model/routeNames/ManagerRouteNames';

const GameRequestsPage = () => {
  const [gameRequests, setGameRequests] = useState<GameRequest[]>([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [filter, setFilter] = useState<'ALL' | 'SUBMITTED' | 'APPROVED' | 'REFUSED'>('ALL');

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
      <h2 className="mb-6 text-2xl font-bold tracking-tight text-gray-900">Game Requests</h2>

      {errorMessage && <p className="text-red-600">{errorMessage}</p>}

      {/* Filter Buttons */}
      <div className="mb-4 flex gap-4">
        <button
          className={`px-4 py-2 rounded-md ${
            filter === 'ALL' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'
          }`}
          onClick={() => setFilter('ALL')}
        >
          All
        </button>
        <button
          className={`px-4 py-2 rounded-md ${
            filter === 'SUBMITTED' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'
          }`}
          onClick={() => setFilter('SUBMITTED')}
        >
          Submitted
        </button>
        <button
          className={`px-4 py-2 rounded-md ${
            filter === 'APPROVED' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'
          }`}
          onClick={() => setFilter('APPROVED')}
        >
          Approved
        </button>
        <button
          className={`px-4 py-2 rounded-md ${
            filter === 'REFUSED' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'
          }`}
          onClick={() => setFilter('REFUSED')}
        >
          Refused
        </button>
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
                <tr key={request.id} className="hover:bg-gray-100">
                  <td className="px-6 py-4 text-sm text-gray-900">{request.id}</td>
                  <td className="px-6 py-4 text-sm text-blue-600 hover:underline">
                    <Link
                      to={`${ManagerRouteNames.GAME_REQUEST_DETAIL.replace(':id', request.id.toString())}`}
                    >
                      {request.name}
                    </Link>
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">{request.description}</td>
                  <td className="px-6 py-4 text-sm text-gray-900">{request.requestStatus}</td>
                  <td className="px-6 py-4 text-sm text-gray-900">{request.staffId}</td>
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
