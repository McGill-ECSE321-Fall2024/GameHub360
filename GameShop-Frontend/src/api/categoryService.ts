import apiService from '../config/axiosConfig';
import { isAxiosError } from 'axios';

export interface Category {
  categoryId: number;
  name: string;
  categoryType: string; // 'GENRE' or 'CONSOLE'
  available: boolean;
  gameIds: number[];
  promotionIds: number[];
}

export async function getAllCategories(): Promise<Category[]> {
  try {
    const response = await apiService.get('/categories');
    return response.data.gameCategories as Category[];
  } catch (error) {
    throw new Error('Failed to fetch categories.');
  }
}

export async function createCategory(category: {
  name: string;
  categoryType: string; // 'GENRE' or 'CONSOLE'
  available: boolean;
}): Promise<void> {
  try {
    await apiService.post('/categories', category);
  } catch (error) {
    throw new Error('Failed to create category.');
  }
}

export async function getCategoryById(categoryId: number): Promise<Category> {
  try {
    const response = await apiService.get(`/categories/${categoryId}`);
    const category = response.data;

    // Ensure categoryType is a string ('GENRE' or 'CONSOLE') or set a default value
    category.categoryType = category.categoryType || 'UNKNOWN';
    return category as Category;
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Category not found.');
      }
      throw new Error('Failed to fetch category.');
    }
    throw new Error('An unexpected error occurred while fetching the category.');
  }
}

export async function updateCategory(
  categoryId: number,
  categoryData: { name: string; categoryType: string; available: boolean }
): Promise<void> {
  try {
    await apiService.put(`/categories/${categoryId}`, categoryData);
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Category not found.');
      }
      if (error.response?.status === 400) {
        throw new Error('Invalid category details.');
      }
      throw new Error('Failed to update category.');
    }
    throw new Error('An unexpected error occurred while updating the category.');
  }
}

export async function deleteCategory(categoryId: number): Promise<void> {
  try {
    await apiService.delete(`/categories/${categoryId}`);
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Category not found.');
      }
      throw new Error('Failed to delete category.');
    }
    throw new Error('An unexpected error occurred while deleting the category.');
  }
}

// Assign a game to a category
export async function assignCategoryToGame(categoryId: number, gameId: number): Promise<void> {
  try {
    await apiService.put(`/categories/${categoryId}/game/${gameId}`);
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Category or game not found.');
      }
      if (error.response?.status === 400) {
        throw new Error('Game already assigned to this category.');
      }
      throw new Error('Failed to assign game to category.');
    }
    throw new Error('An unexpected error occurred while assigning the game to the category.');
  }
}

// Unassign a game from a category
export async function unassignCategoryFromGame(categoryId: number, gameId: number): Promise<void> {
  try {
    await apiService.delete(`/categories/${categoryId}/game/${gameId}`);
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Category or game not found.');
      }
      throw new Error('Failed to unassign game from category.');
    }
    throw new Error('An unexpected error occurred while unassigning the game from the category.');
  }
}

// Assign a promotion to a category
export async function assignCategoryToPromotion(categoryId: number, promotionId: number): Promise<void> {
  try {
    await apiService.put(`/categories/${categoryId}/promotion/${promotionId}`);
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Category or promotion not found.');
      }
      if (error.response?.status === 400) {
        throw new Error('Promotion is already assigned to this category.');
      }
      throw new Error('Failed to assign promotion to category.');
    }
    throw new Error('An unexpected error occurred while assigning the promotion to the category.');
  }
}

// Unassign a promotion from a category
export async function unassignCategoryFromPromotion(categoryId: number, promotionId: number): Promise<void> {
  try {
    await apiService.delete(`/categories/${categoryId}/promotion/${promotionId}`);
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Category or promotion not found.');
      }
      throw new Error('Failed to unassign promotion from category.');
    }
    throw new Error('An unexpected error occurred while unassigning the promotion from the category.');
  }
}
