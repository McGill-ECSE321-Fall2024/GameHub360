import { isAxiosError } from 'axios';
import apiService from '../config/axiosConfig';
import { GameRequest, RequestNote } from '../model/manager/GameRequest';

/**
 * Retrieves all game requests.
 * @returns A promise that resolves to an array of all game requests.
 * @throws An error if the request fails.
 */
export async function getAllGameRequests(): Promise<GameRequest[]> {
  try {
    const response = await apiService.get('/games/request');
    return response.data.gameRequests as GameRequest[]; // Ensure we map the `gameRequests` array
  } catch (error) {
    if (isAxiosError(error)) {
      throw new Error('Failed to fetch game requests.');
    }
    throw new Error(
      'An unexpected error occurred while fetching game requests.'
    );
  }
}

/**
 * Retrieves a game request by ID.
 * @param requestId The ID of the game request to retrieve.
 * @returns A promise that resolves to the game request.
 * @throws An error if the request fails.
 */
export async function getGameRequestById(
  requestId: number
): Promise<GameRequest> {
  try {
    const response = await apiService.get(`/games/request/${requestId}`);
    return response.data as GameRequest;
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Game request not found.');
      }
    }
    throw new Error(
      'An unexpected error occurred while fetching the game request.'
    );
  }
}

/**
 * Adds a note to a game request.
 * @param requestId The ID of the game request to add the note to.
 * @param note The note data to add.
 * @returns A promise that resolves to the added note.
 * @throws An error if the request fails.
 */
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
        throw new Error(
          'Game request or staff writer not found.' + error.message
        );
      }
    }
    throw new Error('An unexpected error occurred while adding the note.');
  }
}

/**
 * Processes a game request.
 * @param requestId The ID of the game request to process.
 * @param managerId The ID of the manager processing the request.
 * @param approval Whether the request is approved or not.
 * @param approvalDetails The approval details (price, quantityInStock, note).
 * @returns A promise that resolves to the updated game request.
 * @throws An error if the request fails.
 */
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
    throw new Error(
      'An unexpected error occurred while processing the game request.'
    );
  }
}

export async function createGameRequest(
  request: {
    name: string;
    description: string;
    imageUrl: string;
    requestDate: string; // ISO string
    staffId: number;
    categoryIds: number[];
  }
): Promise<GameRequest> {
  try {
    const response = await apiService.post('/games/request', request);
    return response.data as GameRequest;
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Staff member not found.');
      }
      if (error.response?.status === 400) {
        throw new Error('Invalid game request details.');
      }
    }
    throw new Error('An unexpected error occurred while creating the game request.');
  }
}
