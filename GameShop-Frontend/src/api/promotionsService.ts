import apiService from '../config/axiosConfig';
import { isAxiosError } from 'axios';

// Define the Promotion interface
export interface Promotion {
  promotionId: number;
  promotionType: 'GAME' | 'CATEGORY';
  discountPercentageValue: number;
  promotedGameIds: number[];
  promotedCategoryIds: number[];
}

// Function to fetch promoted games by promotion ID
export async function getPromotedGames(promotionId: string) {
  try {
    const response = await apiService.get(`/promotions/${promotionId}/games`);
    return response.data.games;
  } catch (error) {
    if (isAxiosError(error)) {
      throw new Error('Failed to fetch promoted games.');
    }
    throw new Error('An unexpected error occurred while fetching promoted games.');
  }}

export async function getAllPromotions() {
    try {
      const response = await apiService.get('/promotions');
      return response.data.promotions;
    } catch (error) {
      if (isAxiosError(error)) {
        throw new Error('Failed to fetch promotions.');
      }
      throw new Error('An unexpected error occurred while fetching promotions.');
    }}

export const createPromotion = async (promotion: Promotion) => {
  try {
    const response = await apiService.post("/manager/promotions", promotion, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response.data;
  } catch (error: any) {
    if (error.response && error.response.data) {
      throw new Error(error.response.data.message || "Failed to create promotion.");
    }
    throw new Error("Failed to create promotion.");
  }
};


