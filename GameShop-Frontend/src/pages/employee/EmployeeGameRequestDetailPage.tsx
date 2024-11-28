import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getGameRequestById, addNoteToGameRequest } from '../../api/gameRequestService';
import { GameRequest, RequestNote } from '../../api/gameRequestService';
import { getAuthUser } from '../../state/authState';

const GameRequestDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const [gameRequest, setGameRequest] = useState<GameRequest | null>(null);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [noteInput, setNoteInput] = useState('');
  const [staffId, setStaffId] = useState<number | null>(null);

  useEffect(() => {
    const fetchRequest = async () => {
      try {
        if (id) {
          const request = await getGameRequestById(parseInt(id));
          setGameRequest(request);
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
        if (!authUser || !authUser.id) {
          console.error('No authenticated user found or user ID is missing.');
          setErrorMessage('Failed to retrieve employee ID. Please log in again.');
          return;
        }
        setStaffId(authUser.id);
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
      setGameRequest({
        ...gameRequest!,
        noteIds: [...gameRequest!.noteIds, note.noteId],
      });
      setNoteInput('');
      setErrorMessage(null);
    } catch (error) {
      console.error('Error adding note:', error);
      setErrorMessage('Failed to add note.');
    }
  };
  
  if (loading) return <p>Loading game request details...</p>;

  if (errorMessage) return <p className="text-red-500">{errorMessage}</p>;

  return (
    <div className="p-6">
      <h2 className="mb-4 text-xl font-bold">Game Request Details</h2>

      {gameRequest && (
        <div>
          <p><strong>Name:</strong> {gameRequest.name}</p>
          <p><strong>Description:</strong> {gameRequest.description}</p>
          <p><strong>Status:</strong> {gameRequest.requestStatus}</p>
          <p><strong>Request Date:</strong> {new Date(gameRequest.requestDate).toLocaleDateString()}</p>

          <h3 className="mt-4 text-lg font-semibold">Add Note</h3>
          <textarea
            value={noteInput}
            onChange={(e) => setNoteInput(e.target.value)}
            className="w-full mt-2 border rounded-md p-2"
          />
          <button onClick={handleAddNote} className="mt-2 bg-blue-500 text-white px-4 py-2 rounded">
            Add Note
          </button>
        </div>
      )}
    </div>
  );
};

export default GameRequestDetailPage;