import apiService from '../config/axiosConfig';
import { isAxiosError } from 'axios';

// Define the Promotion interface
export interface Promotion {
  promotionId: number;
  promotionType: 'GAME' | 'CATEGORY'; // Promotion type (validated by the backend)
  discountPercentageValue: number;
  promotedGameIds: number[];
  promotedCategoryIds: number[];
}

/**
 * Retrieves all promotions.
 * @returns A promise that resolves to an array of all promotions.
 * @throws An error if the request fails.
 */
export async function getAllPromotions(): Promise<Promotion[]> {
  try {
    const response = await apiService.get('/promotions');
    return response.data.promotions as Promotion[];
  } catch (error) {
    if (isAxiosError(error)) {
      throw new Error('Failed to fetch promotions.');
    }
    throw new Error('An unexpected error occurred while fetching promotions.');
  }
}
