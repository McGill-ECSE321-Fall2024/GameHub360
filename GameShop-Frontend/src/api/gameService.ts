import apiService from "../config/axiosConfig";

export interface GameDetails {
  available: boolean;
  gameId: number;
  name: string;
  description: string;
  imageUrl: string;
  price: number;
  orderIds: number[];
  quantity: number;
  orderGameId: number
}

export async function getGameByOrderGameId(orderGameId: number): Promise<GameDetails> {
  try {
    const response = await apiService.get(`/games/game/${orderGameId}`);
    return response.data as GameDetails;
  } catch (error) {
    console.error(`Error fetching game for OrderGame ID ${orderGameId}:`, error);
    throw new Error("Unable to fetch game details.");
  }
}

export async function getGamesByOrderGameIds(orderGameIds: number[]): Promise<GameDetails[]> {
  try {
    const gameDetails: GameDetails[] = [];
    for (const orderGameId of orderGameIds) {
      const gameDetail = await getGameByOrderGameId(orderGameId);
      gameDetails.push(gameDetail);
    }
    return gameDetails;
  } catch (error) {
    console.error("Error fetching game details:", error);
    throw new Error("Unable to fetch game details.");
  }
}



