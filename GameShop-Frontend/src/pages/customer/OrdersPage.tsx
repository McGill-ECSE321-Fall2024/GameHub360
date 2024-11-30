import React, { useEffect, useState } from "react";
import { Button } from "../../components/ui/Button";
import { getCustomerOrderHistory, getCustomerPaymentCards, OrderHistoryResponse, PaymentCardListResponse } from "../../api/customerService";
import { getGamesByOrderGameIds, GameDetails } from "../../api/gameService";

const OrdersPage: React.FC = () => {
  const [orders, setOrders] = useState<OrderHistoryResponse | null>(null);
  const [games, setGames] = useState<{ [key: number]: GameDetails }>({});
  const [cards, setCards] = useState<{ [key: number]: string }>({}); // Maps paymentId to cardNumber
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchOrdersAndGames = async () => {
      try {
        const customerData = JSON.parse(localStorage.getItem("loggedInUser") || "{}");
        const customerId = customerData.id;

        if (!customerId) {
          setError("No customer logged in.");
          return;
        }

        // Fetch the customer's order history
        const orderHistory = await getCustomerOrderHistory(customerId);

        console.log(orderHistory);

        // Extract all unique game IDs from the orders
        const gameIds = Array.from(
          new Set(orderHistory.orders.flatMap((order) => order.orderGameIds))
        );

        // Fetch game details for all game IDs
        const gameDetails = await getGamesByOrderGameIds(gameIds);

        // Fetch all payment cards associated with the customer
        const cardResponse: PaymentCardListResponse = await getCustomerPaymentCards(customerId);

        // Map paymentId to cardNumber
        const cardMap = cardResponse.paymentCards.reduce((map, card) => {
          map[card.paymentDetailsId] = card.cardNumber;
          return map;
        }, {} as { [key: number]: string });

        // Transform game details into a map for quick access
        const gameMap = gameDetails.reduce((map, game) => {
          map[game.gameId] = game;
          return map;
        }, {} as { [key: number]: GameDetails });

        setOrders(orderHistory);
        setGames(gameMap);
        setCards(cardMap);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to fetch orders, games, or cards.");
      }
    };

    fetchOrdersAndGames();
  }, []);

  if (error) {
    return <p className="text-red-500">Error: {error}</p>;
  }

  if (!orders) {
    return <p>Loading...</p>;
  }

  // Function to determine the status color
  const getStatusColor = (status: string): string => {
    switch (status) {
      case "SHIPPING":
        return "text-blue-500"; // Blue for shipping
      case "DELIVERED":
        return "text-green-500"; // Green for delivered
      case "RETURNED":
        return "text-red-500"; // Red for returned
      default:
        return "text-gray-500"; // Default color
    }
  };

  // Function to mask the card number
  const maskCardNumber = (cardNumber: string): string =>
    `**** **** **** ${cardNumber.slice(-4)}`;

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold">Order History</h1>
      <p className="text-sm text-gray-500">
        Check the status of recent orders, manage returns, and discover similar products.
      </p>

      {orders.orders.map((order) => {
        const cardNumber = cards[order.paymentId];
        return (
          <div key={order.orderId} className="border rounded-lg p-4 my-4 shadow-sm">
            {/* Order Header */}
            <div className="flex justify-between items-center mb-4">
              <div>
                <p className="text-sm font-bold">Order ID: {order.orderId}</p>
                <p className="text-sm text-gray-500">
                  Date placed: {new Date(order.orderDate).toLocaleDateString()}
                </p>
                <p className="text-sm text-gray-500">
                  Card: {cardNumber ? maskCardNumber(cardNumber) : "Loading..."}
                </p>
                <p className="text-sm font-bold">Total Price: ${order.totalPrice}</p>
              </div>
              <div className="flex gap-4 items-center">
                <p className={`font-bold ${getStatusColor(order.orderStatus)}`}>
                  Status: {order.orderStatus}
                </p>
              </div>
              <div className="flex gap-2">
                    <Button variant="outline">Return Order</Button>
              </div>
            </div>

            {/* Order Items */}
            {Object.values(games)
              .filter((game) => game.orderIds.includes(order.orderId))
              .map((game, index) => (
                <div key={index} className="flex justify-between items-center border-b py-4">
                  <div className="flex items-center gap-4">
                    {game ? (
                      <>
                        <img
                          src={game.imageUrl}
                          alt={game.name}
                          className="w-16 h-16 rounded"
                        />
                        <div>
                          <p className="font-medium">{game.name}</p>
                          <p className="text-sm text-gray-500">{game.description}</p>
                        </div>
                      </>
                    ) : (
                      <p>Loading game details...</p>
                    )}
                  </div>
                  <div>
                    <p className="font-medium">Price: ${game.price.toFixed(2)}</p>
                  </div>
                  <div className="flex gap-2">
                    <Button variant="link">View product</Button>
                    <Button variant="link">Buy again</Button>
                  </div>
                </div>
              ))}
          </div>
        );
      })}
    </div>
  );
};

export default OrdersPage;
