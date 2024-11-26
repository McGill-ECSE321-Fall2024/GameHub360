import { isAxiosError } from 'axios';
import apiService from '../config/axiosConfig';

// Interfaces
export interface GameRequest {
    id: number;
    name: string;
    description: string;
    imageUrl: string;
    requestDate: string; // ISO string
    requestStatus: 'SUBMITTED' | 'APPROVED' | 'REFUSED'; // Enum values
    staffId: number;
    noteIds: number[];
    categoryIds: number[];
    price?: number; // Nullable, only present if APPROVED
    quantityInStock?: number; // Nullable, only present if APPROVED
    rejectionReason?: string; // Nullable, only present if REFUSED
  }  

export interface RequestNote {
  noteId: number;
  content: string;
  noteDate: string; // ISO string
  staffWriterId: number;
  gameRequestId: number;
}

// Fetch all game requests
export async function getAllGameRequests(): Promise<GameRequest[]> {
  try {
    const response = await apiService.get('/games/request');
    return response.data.gameRequests as GameRequest[]; // Ensure we map the `gameRequests` array
  } catch (error) {
    if (isAxiosError(error)) {
      throw new Error('Failed to fetch game requests.');
    }
    throw new Error('An unexpected error occurred while fetching game requests.');
  }
}

// Fetch a specific game request by ID
export async function getGameRequestById(requestId: number): Promise<GameRequest> {
  try {
    const response = await apiService.get(`/games/request/${requestId}`);
    return response.data as GameRequest;
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Game request not found.');
      }
    }
    throw new Error('An unexpected error occurred while fetching the game request.');
  }
}

// Add a note to a game request
export async function addNoteToGameRequest(
  requestId: number,
  note: {
    content: string;
    staffWriterId: number;
    noteDate: string; // ISO string
  }
): Promise<RequestNote> {
  try {
    const response = await apiService.post(`/games/request/${requestId}/note`, {
      content: note.content,
      staffWriterId: note.staffWriterId,
      noteDate: note.noteDate,
      gameRequestId: requestId, // Ensure the gameRequestId is included as required by the DTO
    });
    return response.data as RequestNote;
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Game request or staff writer not found.' + error.message);
      }
    }
    throw new Error('An unexpected error occurred while adding the note.');
  }
}

// Approve or refuse a game request
export async function processGameRequest(
    requestId: number,
    managerId: number,
    approval: boolean,
    approvalDetails?: {
      price?: number;
      quantityInStock?: number;
      note?: {
        content: string;
        staffWriterId: number;
        noteDate: string; // ISO string
      };
    }
  ): Promise<GameRequest> {
    try {
      const response = await apiService.put(
        `/games/request/${requestId}/approval`,
        approvalDetails || {}, // Pass the approval details in the body (if any)
        {
          params: { managerId, approval }, // Pass managerId and approval as query parameters
        }
      );
      return response.data as GameRequest;
    } catch (error) {
      if (isAxiosError(error)) {
        if (error.response?.status === 404) {
          throw new Error('Game request not found.');
        }
        if (error.response?.status === 400) {
          throw new Error('Invalid approval details.');
        }
      }
      throw new Error('An unexpected error occurred while processing the game request.');
    }
  }
  
