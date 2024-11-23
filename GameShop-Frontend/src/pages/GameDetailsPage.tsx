import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import apiService from '../config/axiosConfig';
import { getAuthState } from '../state/authState';
import { AuthState } from '../model/AuthState';
import { useCart } from '../Context/CartContext';
import { useWishlist } from '../Context/WishlistContext';

interface Game {
  gameId: number;
  title: string;
  price: number;
  imageUrl: string;
  console?: string;
  category?: string;
  description: string;
  quantityInStock: number;
}

interface Review {
  id: number;
  rating: number;
  comment: string;
  reviewDate: string;
  customerName: string;
  replies: Reply[];
}

interface Reply {
  id: number;
  content: string;
  replyDate: string;
  managerName: string;
}

const GameDetailsPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [game, setGame] = useState<Game | null>(null);
  const [reviews, setReviews] = useState<Review[]>([]);
  const authState = getAuthState();
  const { addToCart } = useCart();
  const { addToWishlist, removeFromWishlist, isInWishlist } = useWishlist();

  useEffect(() => {
    const fetchGameDetails = async () => {
      try {
        const response = await apiService.get(`/games/${id}`);
        setGame(response.data);
      } catch (error) {
        console.error('Error fetching game details:', error);
        navigate('/browse');
      }
    };

    const fetchReviews = async () => {
      try {
        const response = await apiService.get(`/games/${id}/reviews`);
        setReviews(response.data.reviews);
      } catch (error) {
        console.error('Error fetching reviews:', error);
      }
    };

    fetchGameDetails();
    fetchReviews();
  }, [id, navigate]);

  const handleWishlistToggle = async () => {
    if (!game) return;
    
    try {
      if (isInWishlist(game.gameId)) {
        removeFromWishlist(game.gameId);
      } else {
        addToWishlist({
          gameId: game.gameId,
          name: game.title,
          price: game.price,
          imageUrl: game.imageUrl,
          console: game.console,
          category: game.category
        });
      }
    } catch (error) {
      console.error('Error updating wishlist:', error);
    }
  };

  const handleAddToCart = () => {
    if (!game) return;
    addToCart({
      gameId: game.gameId,
      title: game.title,
      price: game.price,
      imageUrl: game.imageUrl,
      quantity: 1
    });
  };

  if (!game) {
    return <div className="text-center p-8">Loading...</div>;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        {/* Game Image */}
        <div>
          <img
            src={game.imageUrl}
            alt={game.title}
            className="w-full rounded-lg shadow-lg"
          />
        </div>

        {/* Game Details */}
        <div>
          <h1 className="text-3xl font-bold mb-4">{game.title}</h1>
          <p className="text-gray-600 mb-4">{game.description}</p>
          
          <div className="flex items-center justify-between mb-4">
            <span className="text-2xl font-bold">${game.price.toFixed(2)}</span>
            <span className="text-gray-600">
              {game.quantityInStock > 0 
                ? `${game.quantityInStock} in stock` 
                : 'Out of stock'}
            </span>
          </div>

          {game.console && (
            <div className="mb-4">
              <span className="font-semibold">Console:</span> {game.console}
            </div>
          )}

          {game.category && (
            <div className="mb-4">
              <span className="font-semibold">Category:</span> {game.category}
            </div>
          )}

          {authState === AuthState.CUSTOMER && (
            <div className="flex gap-4">
              <button
                onClick={handleWishlistToggle}
                className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
              >
                {isInWishlist(game.gameId) ? 'Remove from Wishlist' : 'Add to Wishlist'}
              </button>
              <button
                disabled={game.quantityInStock === 0}
                className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 disabled:bg-gray-400"
                onClick={handleAddToCart}
              >
                Add to Cart
              </button>
            </div>
          )}
        </div>
      </div>

      {/* Reviews Section */}
      <div className="mt-12">
        <h2 className="text-2xl font-bold mb-6">Reviews</h2>
        {reviews.length > 0 ? (
          <div className="space-y-6">
            {reviews.map((review) => (
              <div key={review.id} className="border rounded-lg p-4">
                <div className="flex justify-between mb-2">
                  <span className="font-semibold">{review.customerName}</span>
                  <span className="text-gray-600">{review.reviewDate}</span>
                </div>
                <div className="mb-2">
                  {'★'.repeat(review.rating)}{'☆'.repeat(5 - review.rating)}
                </div>
                <p className="text-gray-700">{review.comment}</p>

                {/* Manager Replies */}
                {review.replies.map((reply) => (
                  <div key={reply.id} className="ml-8 mt-4 bg-gray-50 p-3 rounded">
                    <div className="flex justify-between mb-1">
                      <span className="font-semibold text-sm">{reply.managerName}</span>
                      <span className="text-gray-600 text-sm">{reply.replyDate}</span>
                    </div>
                    <p className="text-gray-700">{reply.content}</p>
                  </div>
                ))}
              </div>
            ))}
          </div>
        ) : (
          <p className="text-gray-600">No reviews yet.</p>
        )}
      </div>
    </div>
  );
};

export default GameDetailsPage;
