import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  getGameRequestById,
  addNoteToGameRequest,
  getGameRequestNotes,
} from '../../api/gameRequestService';
import { getAuthUser } from '../../state/authState';
import { GameRequest, RequestNote } from '../../model/manager/GameRequest';
import Modal from 'react-modal';

const GameRequestDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const [gameRequest, setGameRequest] = useState<GameRequest | null>(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [noteInput, setNoteInput] = useState('');
  const [staffId, setStaffId] = useState<number | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [notes, setNotes] = useState<RequestNote[]>([]);

  useEffect(() => {
    const fetchRequest = async () => {
      try {
        if (id) {
          const request = await getGameRequestById(parseInt(id));
          setGameRequest(request);
          const fetchedNotes = await getGameRequestNotes(parseInt(id));
          setNotes(fetchedNotes);
        }
      } catch (error) {
        console.error('Error fetching game request:', error);
        setErrorMessage('Failed to fetch game request details.');
      } finally {
        setLoading(false);
      }
    };

    const fetchAuthUser = async () => {
      try {
        const authUser = getAuthUser();
        if (!authUser || !authUser.staffId) {
          console.error('No authenticated user found or user ID is missing.');
          setErrorMessage(
            'Failed to retrieve employee ID. Please log in again.'
          );
          return;
        }
        setStaffId(authUser.staffId);
      } catch (error) {
        console.error('Error retrieving authenticated user:', error);
        setErrorMessage('Failed to retrieve employee ID. Please log in again.');
      }
    };

    fetchRequest();
    fetchAuthUser();
  }, [id]);

  const handleAddNote = async () => {
    if (!noteInput.trim()) {
      setErrorMessage('Note cannot be empty.');
      return;
    }

    if (staffId === null) {
      setErrorMessage('Failed to retrieve employee ID. Please log in again.');
      return;
    }

    try {
      const note: RequestNote = await addNoteToGameRequest(parseInt(id!), {
        content: noteInput,
        staffWriterId: staffId,
        noteDate: new Date().toISOString(),
      });
      setNotes([...notes, note]);
      setNoteInput('');
      setErrorMessage(null);
    } catch (error) {
      console.error('Error adding note:', error);
      setErrorMessage('Failed to add note.');
    }
  };

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  if (loading)
    return (
      <p className="text-center text-gray-600 font-medium">
        Loading game request details...
      </p>
    );

  if (errorMessage)
    return (
      <div className="text-red-500 text-center bg-red-100 border border-red-400 p-4 rounded">
        {errorMessage}
      </div>
    );

  return (
    <div className="max-w-4xl mx-auto p-6 bg-gray-50 rounded-lg shadow-lg">
      <div className="flex items-center mb-6">
        <button
          onClick={() => navigate('/employee/game-requests')}
          className="bg-gray-100 text-gray-700 px-4 py-2 rounded-md shadow hover:bg-gray-200 transition"
        >
          Back
        </button>
        <h2 className="ml-4 text-2xl font-bold tracking-tight text-gray-900">
          Game Request Details
        </h2>
      </div>

      {gameRequest && (
        <div>
          <div className="bg-white p-6 rounded shadow-md">
            <p className="mb-2">
              <strong className="font-medium text-gray-600">Name:</strong>{' '}
              {gameRequest.name}
            </p>
            <p className="mb-2">
              <strong className="font-medium text-gray-600">
                Description:
              </strong>{' '}
              {gameRequest.description}
            </p>
            <p className="mb-2">
              <strong className="font-medium text-gray-600">Status:</strong>{' '}
              <span
                className={`px-2 py-1 rounded text-white ${
                  gameRequest.requestStatus === 'SUBMITTED'
                    ? 'bg-blue-500'
                    : gameRequest.requestStatus === 'APPROVED'
                    ? 'bg-green-500'
                    : 'bg-red-500'
                }`}
              >
                {gameRequest.requestStatus}
              </span>
            </p>
            <p className="mb-4">
              <strong className="font-medium text-gray-600">
                Request Date:
              </strong>{' '}
              {new Date(gameRequest.requestDate).toLocaleDateString()}
            </p>

            <h3 className="text-lg font-medium text-gray-800">Add Note</h3>
            <textarea
              value={noteInput}
              onChange={(e) => setNoteInput(e.target.value)}
              className="w-full mt-2 border rounded-md p-2 focus:ring-blue-500 focus:border-blue-500"
            />
            <button
              onClick={handleAddNote}
              className="mt-4 bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition"
            >
              Add Note
            </button>
            <button
              onClick={openModal}
              className="mt-4 ml-4 bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition"
            >
              View Notes
            </button>
          </div>
        </div>
      )}

      <Modal
        isOpen={isModalOpen}
        onRequestClose={closeModal}
        contentLabel="View Notes"
        className="fixed inset-0 flex items-center justify-center p-4 bg-gray-800 bg-opacity-75"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50"
      >
        <div className="bg-white rounded-lg shadow-lg p-6 max-w-3xl w-full">
          <h2 className="text-2xl font-bold mb-4">Notes</h2>
          <button
            onClick={closeModal}
            className="absolute top-4 right-4 bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition"
          >
            Close
          </button>
          <ul className="space-y-4">
            {notes.map((note) => (
              <li
                key={note.noteId}
                className="bg-gray-100 p-4 rounded-md shadow"
              >
                <p className="text-gray-800">{note.content}</p>
                <p className="text-gray-600 text-sm">
                  {new Date(note.noteDate).toLocaleDateString()}
                </p>
                <p className="text-gray-500 text-xs">
                  Sent by:{' '}
                  {note.staffWriterId === staffId ? 'Employee' : 'Manager'} (ID:{' '}
                  {note.staffWriterId})
                </p>
              </li>
            ))}
          </ul>
        </div>
      </Modal>
    </div>
  );
};

export default GameRequestDetailPage;
