import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllGameRequests } from '../../api/gameRequestService';
import { ManagerRouteNames } from '../../model/routeNames/ManagerRouteNames';
import { GameRequest } from '../../model/manager/GameRequest';

const ManagerGameRequestsPage = () => {
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
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-semibold text-gray-800">Game Requests</h2>
      </div>

      {errorMessage && (
        <div className="mb-4 p-4 text-red-700 bg-red-100 rounded-lg">
          {errorMessage}
        </div>
      )}

      {/* Filter Buttons */}
      <div className="mb-6 flex space-x-4">
        {['ALL', 'SUBMITTED', 'APPROVED', 'REFUSED'].map((status) => (
          <button
            key={status}
            className={`px-5 py-2 rounded-lg shadow-sm font-medium transition duration-200 ${
              filter === status
                ? 'bg-blue-500 text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
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
        <p className="text-gray-700 text-lg">Loading game requests...</p>
      ) : filteredRequests.length === 0 ? (
        <p className="text-gray-700 text-lg">
          No game requests found for the selected filter.
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
                  Status
                </th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  Requested By
                </th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  Request Date
                </th>
              </tr>
            </thead>
            <tbody>
              {filteredRequests.map((request) => (
                <tr
                  key={request.id}
                  className="border-b hover:bg-gray-50 cursor-pointer transition"
                  onClick={() =>
                    navigate(
                      ManagerRouteNames.GAME_REQUEST_DETAIL.replace(
                        ':id',
                        request.id.toString()
                      )
                    )
                  }
                >
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {request.id}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {request.name}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {request.description}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {request.requestStatus}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {request.staffId}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
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

export default ManagerGameRequestsPage;
