import apiService from '../config/axiosConfig';

export interface GameCategory {
  categoryId: number;
  name: string;
  available: boolean;
  categoryType: string; // 'GENRE' or 'CONSOLE'
}

export async function getAllCategories(): Promise<GameCategory[]> {
  try {
    const response = await apiService.get('/categories');
    return response.data.gameCategories as GameCategory[];
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
  
