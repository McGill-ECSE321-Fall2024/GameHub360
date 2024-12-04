export interface Category {
  categoryId: number;
  name: string;
  categoryType: string; // 'GENRE' or 'CONSOLE'
  available: boolean;
  gameIds: number[];
  promotionIds: number[];
}
