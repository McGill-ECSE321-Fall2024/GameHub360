import apiService from "../config/axiosConfig";

export interface GameDetails {
  gameId: number;
  name: string;
  description: string;
  imageUrl: string;
  price: number;
  orderIds: number[];
}

export async function getGamesByOrderGameIds(orderGameIds: number[]): Promise<GameDetails[]> {
  try {
    const response = await apiService.post("/games/details", orderGameIds);
    return response.data.games as GameDetails[];
  } catch (error) {
    console.error("Error fetching game details:", error);
    throw new Error("Unable to fetch game details.");
  }
}
