import React, { useEffect, useState } from 'react';
import { Button } from '../../components/ui/Button';
import Modal from '../../components/ui/Modal'; // Import your Modal component
import {
  getCustomerOrderHistory,
  getCustomerPaymentCards,
  returnOrder,
} from '../../api/customerService';
import {
  CustomerOrderResponse,
  OrderHistoryResponse,
} from '../../model/customer/customerOrderInterfaces';
import { PaymentCardListResponse } from '../../model/customer/paymentCardInterfaces';
import {
  getGameByOrderGameId,
  getGamesByOrderGameIds,
  GameDetails,
} from '../../api/gameService';
import {
  submitReview,
  getGameReviews,
  deleteReview,
} from '../../api/reviewService';

const OrdersPage: React.FC = () => {
  const [orders, setOrders] = useState<OrderHistoryResponse | null>(null);
  const [games, setGames] = useState<{ [key: number]: GameDetails[] }>({});
  const [reviews, setReviews] = useState<{
    [key: number]: {
      showComment: boolean;
      review: { rating: string; comment?: string } | null;
    };
  }>({});
  const [cards, setCards] = useState<{ [key: number]: string }>({});
  const [error, setError] = useState<string | null>(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedOrderId, setSelectedOrderId] = useState<number | null>(null);
  const [modalMessage, setModalMessage] = useState<string>('');
  const [reviewModalOpen, setReviewModalOpen] = useState(false);
  const [selectedOrderGameId, setSelectedOrderGameId] = useState<number | null>(
    null
  ); // Replace selectedGameId
  const [rating, setRating] = useState<
    'ONE_STAR' | 'TWO_STARS' | 'THREE_STARS' | 'FOUR_STARS' | 'FIVE_STARS' | ''
  >('');
  const [comment, setComment] = useState<string>('');

  useEffect(() => {
    const fetchOrdersAndGames = async () => {
      try {
        const customerData = JSON.parse(
          localStorage.getItem('loggedInUser') || '{}'
        );
        const customerId = customerData.customerId;

        if (!customerId) {
          setError('No customer logged in.');
          return;
        }

        // Fetch the customer's order history
        const orderHistory = await getCustomerOrderHistory(customerId);

        // Extract all unique game IDs from the orders
        const orderGameIds = Array.from(
          new Set(orderHistory.orders.flatMap((order) => order.orderedGamesIds))
        );

        // Build a mapping between OrderGameId and GameDetails
        const customerOrderToGamesMap = {} as {
          [customerOrderId: number]: GameDetails[];
        };

        for (const order of orderHistory.orders) {
          customerOrderToGamesMap[order.orderId] = [];

          for (const orderGameId of order.orderedGamesIds) {
            try {
              const gameDetails = await getGameByOrderGameId(orderGameId);

              console.log(gameDetails);

              // Add game and its associated orderGameId to the map
              customerOrderToGamesMap[order.orderId].push({
                ...gameDetails,
                orderGameId,
                quantity: gameDetails.quantity || 1, // Ensure quantity is set
              });
            } catch (error) {
              console.error(
                `Error fetching game for OrderGame ID ${orderGameId}:`,
                error
              );
            }
          }
        }

        // Fetch all payment cards associated with the customer
        const cardResponse: PaymentCardListResponse =
          await getCustomerPaymentCards(customerId);

        // Map paymentId to cardNumber
        const cardMap = cardResponse.paymentCards.reduce(
          (map, card) => {
            map[card.paymentDetailsId] = card.cardNumber;
            return map;
          },
          {} as { [key: number]: string }
        );

        // Map reviews to OrderGameIds based on the new `customerOrderToGamesMap` structure
        const reviewsMap: {
          [key: number]: {
            showComment: boolean;
            review: { rating: string; comment?: string } | null;
          };
        } = {};

        for (const [customerOrderId, games] of Object.entries(
          customerOrderToGamesMap
        )) {
          for (const game of games) {
            try {
              console.log('order game id: ', game.orderGameId);
              // Fetch the reviews associated with the current OrderGameId
              const gameReviews = await getGameReviews(game.gameId);

              // Map the review to the corresponding OrderGameId
              reviewsMap[game.orderGameId] = {
                showComment: false, // Default to hidden
                review:
                  gameReviews.length > 0
                    ? {
                        rating: gameReviews[0].rating, // Map the first review's rating
                        comment: gameReviews[0].comment, // Map the first review's comment
                      }
                    : null, // No review available
              };
            } catch (error) {
              console.error(
                `Failed to fetch reviews for OrderGame ID ${game.orderGameId}:`,
                error
              );
            }
          }
        }

        console.log('Reviews: ', reviewsMap);

        setOrders(orderHistory);
        setGames(customerOrderToGamesMap);
        console.log('games: ', customerOrderToGamesMap);
        setCards(cardMap);
        setReviews(reviewsMap);
      } catch (err) {
        setError(
          err instanceof Error
            ? err.message
            : 'Failed to fetch orders, games, or cards.'
        );
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
      case 'SHIPPING':
        return 'text-blue-500'; // Blue for shipping
      case 'DELIVERED':
        return 'text-green-500'; // Green for delivered
      case 'RETURNED':
        return 'text-red-500'; // Red for returned
      default:
        return 'text-gray-500'; // Default color
    }
  };

  // Function to mask the card number
  const maskCardNumber = (cardNumber: string): string =>
    `**** **** **** ${cardNumber.slice(-4)}`;

  const handleReturnClick = (orderId: number) => {
    setSelectedOrderId(orderId);
    setModalMessage('Are you sure you want to return this order?');
    setModalOpen(true);
  };

  const handleConfirmReturn = async () => {
    if (!selectedOrderId) return;

    try {
      const response = await returnOrder(selectedOrderId);
      setOrders((prevOrders) => {
        if (!prevOrders) return prevOrders;

        return {
          ...prevOrders,
          orders: prevOrders.orders.map((order) =>
            order.orderId === selectedOrderId
              ? { ...order, orderStatus: 'RETURNED' }
              : order
          ),
        };
      });
      setModalMessage('Order returned successfully!');
    } catch (error) {
      setModalMessage(
        error instanceof Error ? error.message : 'An unexpected error occurred.'
      );
    }
  };

  const closeModal = () => {
    setModalOpen(false);
    setSelectedOrderId(null);
  };

  const openReviewModal = (orderGameId: number) => {
    setSelectedOrderGameId(orderGameId); // Set the selected OrderGame ID
    setReviewModalOpen(true);
  };

  const closeReviewModal = () => {
    setReviewModalOpen(false);
    setSelectedOrderGameId(null);
    setRating('');
    setComment('');
  };

  const handleSubmitReview = async () => {
    if (!selectedOrderGameId || rating === '') {
      alert('Please select a rating before submitting.');
      return;
    }

    // Retrieve customerId from local storage
    const customerData = JSON.parse(
      localStorage.getItem('loggedInUser') || '{}'
    );
    const customerId: number = customerData.customerId;

    if (!customerId) {
      alert('Please log in before submitting a review.');
      return;
    }

    const reviewPayload = {
      rating, // Use the enum value directly
      customerId,
      comment,
    };

    try {
      const review = await submitReview(selectedOrderGameId, reviewPayload);

      setReviews((prevReviews) => ({
        ...prevReviews,
        [selectedOrderGameId]: {
          showComment: true,
          review: { rating, comment },
        },
      }));

      closeReviewModal();
    } catch (error) {
      console.error('Failed to submit review:', error);
      alert('Failed to submit the review. Please try again.');
    }
  };

  const toggleReviewVisibility = (gameId: number) => {
    setReviews((prevReviews) => ({
      ...prevReviews,
      [gameId]: {
        ...prevReviews[gameId],
        showComment: !prevReviews[gameId]?.showComment,
      },
    }));
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold">Order History</h1>
      <p className="text-sm text-gray-500">
        Check the status of recent orders, manage returns, and discover similar
        products.
      </p>

      {orders.orders.map((order) => {
        const cardNumber = cards[order.paymentInformationId];
        const orderDate = new Date(order.orderDate);
        const currentDate = new Date();

        // Check if the order date is within 7 days
        const isReturnable =
          (currentDate.getTime() - orderDate.getTime()) /
            (1000 * 60 * 60 * 24) <=
          7;

        return (
          <div
            key={order.orderId}
            className="border rounded-lg p-4 my-4 shadow-sm"
          >
            {/* Order Header */}
            <div className="flex justify-between items-center mb-4">
              <div>
                <p className="text-sm font-bold">Order ID: {order.orderId}</p>
                <p className="text-sm text-gray-500">
                  Date placed: {orderDate.toLocaleDateString()}
                </p>
                <p className="text-sm text-gray-500">
                  Card: {cardNumber ? maskCardNumber(cardNumber) : 'Loading...'}
                </p>
              </div>
              <div className="flex gap-4 items-center">
                <p className={`font-bold ${getStatusColor(order.orderStatus)}`}>
                  Status: {order.orderStatus}
                </p>
              </div>
              <div className="flex gap-2">
                {/* Tooltip and Disabled Button */}
                <div
                  className="group relative"
                  style={{ position: 'relative', display: 'inline-block' }}
                >
                  <Button
                    variant="outline"
                    disabled={!isReturnable || order.orderStatus === 'RETURNED'}
                    onClick={() => handleReturnClick(order.orderId)}
                  >
                    Return Order
                  </Button>
                  {!isReturnable && (
                    <span
                      className="absolute bg-gray-700 text-white text-xs rounded py-1 px-2 opacity-0 group-hover:opacity-100"
                      style={{
                        position: 'absolute',
                        top: '100%',
                        left: '45%',
                        transform: 'translateX(-50%)',
                        zIndex: 1,
                        whiteSpace: 'nowrap',
                      }}
                    >
                      Returns are allowed within 7 days only.
                    </span>
                  )}
                </div>
              </div>
            </div>

            {/* Order Items */}
            {games[order.orderId].map((gameEntry, index) => {
              const {
                name,
                description,
                imageUrl,
                price,
                available,
                quantity,
                orderGameId,
              } = gameEntry;

              // Access review for the current OrderGameId
              const reviewEntry = reviews[orderGameId] || {
                showComment: false,
                review: null,
              };

              return (
                <div
                  key={`${order.orderId}-${orderGameId}-${index}`}
                  className="border-b py-4"
                >
                  {/* Game Info, Price, and Buttons */}
                  <div className="flex items-center">
                    {/* Game Info Section */}
                    <div className="flex items-center gap-4 flex-grow">
                      <img
                        src={imageUrl}
                        alt={name}
                        className="w-16 h-16 rounded"
                      />
                      <div>
                        <p className="font-medium">{name}</p>
                        <p className="text-sm text-gray-500">{description}</p>
                        {!available && (
                          <span className="text-xs text-red-500 font-bold">
                            Archived Game
                          </span>
                        )}
                      </div>
                    </div>

                    {/* Price Section */}
                    <div className="flex justify-center flex-shrink-0 w-32">
                      <p className="font-medium">
                        Unit Price: ${price.toFixed(2)}
                      </p>
                    </div>

                    {/* Action Buttons Section */}
                    <div className="flex gap-2 ml-auto flex-shrink-0">
                      <Button
                        variant="link"
                        onClick={() => openReviewModal(orderGameId)}
                      >
                        Submit Review
                      </Button>
                      <Button variant="link" disabled={!available}>
                        Buy again
                      </Button>
                    </div>
                  </div>

                  {/* Toggle Customer Review */}
                  <div className="mt-2">
                    <button
                      className="text-blue-500 text-sm underline"
                      onClick={() => toggleReviewVisibility(orderGameId)}
                    >
                      {reviewEntry.showComment
                        ? 'Hide Your Review'
                        : 'Show Your Review'}
                    </button>
                    {reviewEntry.showComment && reviewEntry.review?.comment && (
                      <div className="mt-1 p-2 bg-gray-100 rounded">
                        <p className="text-sm">
                          <strong>Your Rating:</strong>{' '}
                          {reviewEntry.review.rating.replace('_', ' ')}
                        </p>
                        <p className="text-sm">
                          <strong>Your Comment:</strong>{' '}
                          {reviewEntry.review.comment}
                        </p>
                      </div>
                    )}
                  </div>
                </div>
              );
            })}
          </div>
        );
      })}

      {/* Modal for order return */}
      <Modal isOpen={modalOpen} onClose={closeModal}>
        <p>{modalMessage}</p>
        {modalMessage === 'Are you sure you want to return this order?' && (
          <div className="flex gap-2">
            <Button variant="default" onClick={handleConfirmReturn}>
              Confirm
            </Button>
            <Button variant="secondary" onClick={closeModal}>
              Cancel
            </Button>
          </div>
        )}
      </Modal>

      {/* Modal for review submission */}
      <Modal isOpen={reviewModalOpen} onClose={closeReviewModal}>
        <h2 className="text-xl font-bold">Submit Review</h2>
        <form className="mt-4">
          <label className="block text-sm font-bold">Rating:</label>
          <select
            className="block w-full p-2 mt-1 border rounded"
            value={rating}
            onChange={(e) =>
              setRating(
                e.target.value as
                  | 'ONE_STAR'
                  | 'TWO_STARS'
                  | 'THREE_STARS'
                  | 'FOUR_STARS'
                  | 'FIVE_STARS'
              )
            }
          >
            <option value="">Select a rating</option>
            <option value="ONE_STAR">1 Star</option>
            <option value="TWO_STARS">2 Stars</option>
            <option value="THREE_STARS">3 Stars</option>
            <option value="FOUR_STARS">4 Stars</option>
            <option value="FIVE_STARS">5 Stars</option>
          </select>

          <label className="block mt-4 text-sm font-bold">
            Comment (optional):
          </label>
          <textarea
            className="block w-full p-2 mt-1 border rounded"
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            placeholder="Write a comment..."
          />

          <div className="mt-4 flex gap-2">
            <Button variant="default" onClick={handleSubmitReview}>
              Submit
            </Button>
            <Button variant="secondary" onClick={closeReviewModal}>
              Cancel
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default OrdersPage;
