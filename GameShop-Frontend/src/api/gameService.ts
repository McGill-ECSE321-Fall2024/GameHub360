import { isAxiosError } from 'axios';
import apiService from '../config/axiosConfig';

export interface Game {
  gameId: number;
  name: string;
  description: string;
  imageUrl: string;
  quantityInStock: number;
  available: boolean;
  price: number;
  categoryIds: number[];
}

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

export async function getArchivedGames(): Promise<Game[]> {
  try {
    const response = await apiService.get('/games/archive');
    return response.data.games as Game[];
  } catch (error) {
    if (isAxiosError(error)) {
      throw new Error('Failed to fetch archived games.');
    }
    throw new Error('An unexpected error occurred while fetching archived games.');
  }
}

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
        throw new Error('Failed to create game.');
      }
      throw new Error('An unexpected error occurred while creating the game.');
    }
  }  
