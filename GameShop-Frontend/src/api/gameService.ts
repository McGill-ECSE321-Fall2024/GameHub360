import { isAxiosError } from 'axios';
import apiService from '../config/axiosConfig';
import { Game } from '../model/manager/Game';

/**
 * Retrieves all games.
 * @returns A promise that resolves to an array of all games.
 * @throws An error if the request fails.
 */
export async function getAllGames(): Promise<Game[]> {
  try {
    const response = await apiService.get('/games/all');
    return response.data.games as Game[];
  } catch (error) {
    if (isAxiosError(error)) {
      throw new Error('Failed to fetch games.');
    }
    throw new Error('An unexpected error occurred while fetching games.');
  }
}

/**
 * Retrieves all archived games.
 * @returns A promise that resolves to an array of all archived games.
 * @throws An error if the request fails.
 */
export async function getArchivedGames(): Promise<Game[]> {
  try {
    const response = await apiService.get('/games/archive');
    return response.data.games as Game[];
  } catch (error) {
    if (isAxiosError(error)) {
      throw new Error('Failed to fetch archived games.');
    }
    throw new Error(
      'An unexpected error occurred while fetching archived games.'
    );
  }
}

/**
 * Creates a new game.
 * @param game The game data to create.
 * @throws An error if the request fails.
 */
export async function createGame(game: {
  name: string;
  description: string;
  imageURL: string;
  quantityInStock: number;
  isAvailable: boolean;
  price: number;
  categoryIds: number[];
}): Promise<void> {
  try {
    await apiService.post('/games', game);
  } catch (error) {
    if (isAxiosError(error)) {
      const statusCode = error.response?.status;
      if (statusCode === 400) {
        throw new Error('A game with this name already exists.');
      }
      if (statusCode === 404) {
        throw new Error('One or more categories Ids do not exist.');
      }
    }
    throw new Error('An unexpected error occurred while creating the game.');
  }
}

/**
 * Retrieves a game by ID.
 * @param gameId The ID of the game to retrieve.
 * @returns A promise that resolves to the game.
 * @throws An error if the request fails.
 */
export async function getGameById(gameId: number): Promise<Game> {
  try {
    const response = await apiService.get(`/games/${gameId}`);
    return response.data as Game;
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Game not found.');
      }
      throw new Error('Failed to fetch game.');
    }
    throw new Error('An unexpected error occurred while fetching the game.');
  }
}

/**
 * Updates a game.
 * @param gameId The ID of the game to update.
 * @param gameData The updated game data.
 * @throws An error if the request fails.
 */
export async function updateGame(gameId: number, gameData: any): Promise<void> {
  try {
    await apiService.put(`/games/${gameId}`, gameData);
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Game not found.');
      }
      if (error.response?.status === 400) {
        throw new Error('Invalid game details.');
      }
      throw new Error('Failed to update game.');
    }
    throw new Error('An unexpected error occurred while updating the game.');
  }
}

/**
 * Deletes a game.
 * @param gameId The ID of the game to delete.
 * @throws An error if the request fails.
 */
export async function archiveGame(gameId: number): Promise<void> {
  try {
    await apiService.put(`/games/archive/${gameId}`);
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Game not found.');
      }
      throw new Error('Failed to archive game.');
    }
    throw new Error('An unexpected error occurred while archiving the game.');
  }
}

export async function reactivateGame(gameId: number): Promise<void> {
  try {
    await apiService.put(`/games/archive/${gameId}/reactivate`);
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Game not found.');
      }
      if (error.response?.status === 400) {
        throw new Error('Game is already active.');
      }
      throw new Error('Failed to reactivate game.');
    }
    throw new Error(
      'An unexpected error occurred while reactivating the game.'
    );
  }
}

export async function assignGameToCategory(
  gameId: number,
  categoryId: number
): Promise<void> {
  try {
    await apiService.put(`/games/${gameId}/categories/${categoryId}`);
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Game or category not found.');
      }
      if (error.response?.status === 400) {
        throw new Error('Game is already assigned to this category.');
      }
      throw new Error('Failed to assign game to category.');
    }
    throw new Error(
      'An unexpected error occurred while assigning the game to a category.'
    );
  }
}

export async function assignPromotionToGame(
  gameId: number,
  promotionId: number
): Promise<void> {
  try {
    await apiService.put(`/games/${gameId}/promotions/${promotionId}`);
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Promotion not found.');
      }
      if (error.response?.status === 400) {
        throw new Error('Game is already assigned to this promotion.');
      }
      throw new Error('Failed to assign promotion to game.');
    }
    throw new Error(
      'An unexpected error occurred while assigning the promotion to the game.'
    );
  }
}
