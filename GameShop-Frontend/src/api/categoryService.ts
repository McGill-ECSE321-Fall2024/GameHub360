import apiService from '../config/axiosConfig';
import { isAxiosError } from 'axios';
import { Category } from '../model/manager/Category';

/**
 * Retrieves all categories.
 *
 * @returns A promise that resolves to an array of all categories.
 * @throws An error if the request fails.
 */
export async function getAllCategories(): Promise<Category[]> {
  try {
    const response = await apiService.get('http://localhost:8080/manager/categories');
    return response.data.gameCategories as Category[];
  } catch (error) {
    throw new Error('Failed to fetch categories.');
  }
}

/**
 * Creates a new category.
 *
 * @param category The category data to create.
 * @throws An error if the request fails.
 */
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

/**
 * Retrieves a category by ID.
 *
 * @param categoryId The ID of the category to retrieve.
 * @returns A promise that resolves to the category.
 * @throws An error if the request fails.
 */
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
    throw new Error(
      'An unexpected error occurred while fetching the category.'
    );
  }
}

/**
 * Updates a category by ID.
 *
 * @param categoryId The ID of the category to update.
 * @param category The updated category data.
 * @throws An error if the request fails.
 */
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
    throw new Error(
      'An unexpected error occurred while updating the category.'
    );
  }
}

/**
 * Deletes a category by ID.
 *
 * @param categoryId The ID of the category to delete.
 * @throws An error if the request fails.
 */
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
    throw new Error(
      'An unexpected error occurred while deleting the category.'
    );
  }
}

/**
 * Assigns a game to a category.
 * @param categoryId
 * @param gameId
 * @throws An error if the request fails.
 */
export async function assignCategoryToGame(
  categoryId: number,
  gameId: number
): Promise<void> {
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
    throw new Error(
      'An unexpected error occurred while assigning the game to the category.'
    );
  }
}

/**
 * Unassigns a game from a category.
 * @param categoryId
 * @param gameId
 * @throws An error if the request fails.
 */
export async function unassignCategoryFromGame(
  categoryId: number,
  gameId: number
): Promise<void> {
  try {
    await apiService.delete(`/categories/${categoryId}/game/${gameId}`);
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Category or game not found.');
      }
      throw new Error('Failed to unassign game from category.');
    }
    throw new Error(
      'An unexpected error occurred while unassigning the game from the category.'
    );
  }
}

/**
 * Assigns a promotion to a category.
 * @param categoryId
 * @param promotionId
 * @throws An error if the request fails.
 */
export async function assignCategoryToPromotion(
  categoryId: number,
  promotionId: number
): Promise<void> {
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
    throw new Error(
      'An unexpected error occurred while assigning the promotion to the category.'
    );
  }
}

/**
 * Unassigns a promotion from a category.
 * @param categoryId
 * @param promotionId
 * @throws An error if the request fails.
 */
export async function unassignCategoryFromPromotion(
  categoryId: number,
  promotionId: number
): Promise<void> {
  try {
    await apiService.delete(
      `/categories/${categoryId}/promotion/${promotionId}`
    );
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Category or promotion not found.');
      }
      throw new Error('Failed to unassign promotion from category.');
    }
    throw new Error(
      'An unexpected error occurred while unassigning the promotion from the category.'
    );
  }
}
