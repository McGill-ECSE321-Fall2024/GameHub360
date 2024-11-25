import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import { useParams } from 'react-router-dom';
import { getGameRequestById, addNoteToGameRequest, processGameRequest } from '../../api/gameRequestService';
import { GameRequest, RequestNote } from '../../api/gameRequestService';

Modal.setAppElement('#root'); // Ensure accessibility

const GameRequestDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const [gameRequest, setGameRequest] = useState<GameRequest | null>(null);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [noteInput, setNoteInput] = useState('');
  
  // Modals states
  const [isApproveModalOpen, setIsApproveModalOpen] = useState(false);
  const [isRefuseModalOpen, setIsRefuseModalOpen] = useState(false);

  const [approvalDetails, setApprovalDetails] = useState({
    price: '',
    quantityInStock: '',
  });

  const [rejectionReason, setRejectionReason] = useState('');

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

    fetchRequest();
  }, [id]);

  const handleAddNote = async () => {
    if (!noteInput.trim()) {
      setErrorMessage('Note cannot be empty.');
      return;
    }

    try {
      const note: RequestNote = await addNoteToGameRequest(parseInt(id!), {
        content: noteInput,
        staffWriterId: 5402, // Replace with user ID from context
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

  const handleProcessRequest = async (approval: boolean) => {
    try {
      const approvalDto = approval
        ? {
            price: parseFloat(approvalDetails.price),
            quantityInStock: parseInt(approvalDetails.quantityInStock),
            note: noteInput ? { content: noteInput, staffWriterId: 5402, noteDate: new Date().toISOString() } : undefined,
          }
        : {
            rejectionReason,
            note: noteInput ? { content: noteInput, staffWriterId: 5402, noteDate: new Date().toISOString() } : undefined,
          };

      const updatedRequest = await processGameRequest(parseInt(id!), 5402, approval, approvalDto);
      setGameRequest(updatedRequest); // Update the state with the processed request
      setErrorMessage(null);
    } catch (error) {
      console.error('Error processing request:', error);
      setErrorMessage('Failed to process game request.');
    } finally {
      // Close modals after processing
      setIsApproveModalOpen(false);
      setIsRefuseModalOpen(false);
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

          {gameRequest.requestStatus === 'SUBMITTED' && (
            <>
              <h3 className="mt-6 text-lg font-semibold">Process Request</h3>
              <button
                onClick={() => setIsApproveModalOpen(true)}
                className="mt-2 bg-green-500 text-white px-4 py-2 rounded"
              >
                Approve
              </button>
              <button
                onClick={() => setIsRefuseModalOpen(true)}
                className="mt-2 bg-red-500 text-white px-4 py-2 rounded"
              >
                Refuse
              </button>
            </>
          )}
        </div>
      )}

      {/* Approval Modal */}
      <Modal
        isOpen={isApproveModalOpen}
        onRequestClose={() => setIsApproveModalOpen(false)}
        className="bg-white p-6 rounded shadow-lg"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
      >
        <h2 className="text-lg font-bold">Approve Request</h2>
        <div>
          <label>Price:</label>
          <input
            type="text"
            value={approvalDetails.price}
            onChange={(e) => setApprovalDetails({ ...approvalDetails, price: e.target.value })}
            className="border rounded-md p-1"
          />
        </div>
        <div>
          <label>Quantity in Stock:</label>
          <input
            type="text"
            value={approvalDetails.quantityInStock}
            onChange={(e) => setApprovalDetails({ ...approvalDetails, quantityInStock: e.target.value })}
            className="border rounded-md p-1"
          />
        </div>
        <button onClick={() => handleProcessRequest(true)} className="mt-4 bg-green-500 text-white px-4 py-2 rounded">
          Approve
        </button>
      </Modal>

      {/* Refuse Modal */}
      <Modal
        isOpen={isRefuseModalOpen}
        onRequestClose={() => setIsRefuseModalOpen(false)}
        className="bg-white p-6 rounded shadow-lg"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
      >
        <h2 className="text-lg font-bold">Refuse Request</h2>
        <div>
          <label>Rejection Reason:</label>
          <textarea
            value={rejectionReason}
            onChange={(e) => setRejectionReason(e.target.value)}
            className="w-full mt-2 border rounded-md p-2"
          />
        </div>
        <button onClick={() => handleProcessRequest(false)} className="mt-4 bg-red-500 text-white px-4 py-2 rounded">
          Refuse
        </button>
      </Modal>
    </div>
  );
};

export default GameRequestDetailPage;
