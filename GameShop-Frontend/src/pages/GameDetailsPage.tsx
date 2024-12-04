import { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import apiService from '../config/axiosConfig';
import { getAuthState } from '../state/authState';
import { AuthState } from '../model/AuthState';
import { useCart } from '../context/CartContext';
import { useWishlist } from '../context/WishlistContext';
import { useToast } from '../context/ToastContext';

interface Game {
  gameId: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  quantityInStock: number;
  console?: string;
  category?: string;
  categoryId?: number;
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
  const location = useLocation();
  const returnPath = location.state?.returnPath || '/browse';
  const initialGame = location.state?.game;
  const [game, setGame] = useState<Game | null>(initialGame);
  const [reviews, setReviews] = useState<Review[]>([]);
  const authState = getAuthState();
  const { addToCart } = useCart();
  const { addToWishlist, removeFromWishlist, isInWishlist } = useWishlist();
  const { showToast } = useToast();

  useEffect(() => {
    if (authState === AuthState.UNAUTHENTICATED) {
      navigate('/browse');
    }
  }, [authState, navigate]);

  useEffect(() => {
    const fetchGameData = async () => {
      try {
        let gameData = initialGame;
        
        // If no initial game, fetch it
        if (!initialGame && id) {
          const gameResponse = await apiService.get(`/games/${id}`);
          gameData = gameResponse.data;
        }

        // Always fetch categories for the game
        if (gameData && id) {
          try {
            const categoryResponse = await apiService.get(`/categories/game/${id}`);
            const categories = categoryResponse.data.gameCategories;
            setGame({
              ...gameData,
              category: categories.length > 0 ? categories[0].name : 'Uncategorized'
            });
          } catch (error) {
            setGame({
              ...gameData,
              category: 'Uncategorized'
            });
          }
        }
      } catch (error) {
        console.error('Error fetching game:', error);
        navigate(returnPath);
      }
    };

    if (id) {
      fetchGameData();
    }
  }, [id, initialGame, navigate, returnPath]);

  useEffect(() => {
    const fetchReviews = async () => {
      try {
        if (id) {
          const response = await apiService.get(`/games/${id}/reviews`);
          setReviews(response.data.reviews);
        }
      } catch (error) {
        console.error('Error fetching reviews:', error);
      }
    };

    fetchReviews();
  }, [id]);

  const handleWishlistToggle = async () => {
    if (!game) return;
    
    try {
      if (isInWishlist(game.gameId)) {
        removeFromWishlist(game.gameId);
        showToast('Removed from Wishlist', 'success');
      } else {
        await addToWishlist({
          gameId: game.gameId,
          name: game.name,
          price: game.price,
          imageUrl: game.imageUrl,
          console: game.console,
          category: game.category
        });
        showToast('Added to Wishlist successfully!', 'success');
      }
    } catch (error) {
      console.error('Error updating wishlist:', error);
      showToast('Failed to update wishlist', 'error');
    }
  };

  const handleAddToCart = () => {
    if (!game) return;
    addToCart({
      gameId: game.gameId,
      name: game.name,
      price: game.price,
      imageUrl: game.imageUrl,
      quantity: 1
    });
    showToast('Added to cart successfully!', 'success');
  };

  if (!game) {
    navigate(returnPath);
    return null;
  }

  console.log('Current auth state:', authState);
  console.log('Fetched game data:', game);

  return (
    <div className="container mx-auto px-4 py-8">
      <button
        onClick={() => navigate(returnPath)}
        className="mb-6 text-blue-500 hover:text-blue-600 flex items-center gap-2"
      >
        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
        </svg>
        Back to {returnPath === '/wishlist' ? 'Wishlist' : 'Browse'}
      </button>
      
      <div className="bg-white rounded-xl shadow-lg overflow-hidden">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 p-6">
          {/* Game Image */}
          <div className="relative">
            <img
              src={game.imageUrl}
              alt={game.name}
              className="w-full rounded-lg shadow-md object-cover aspect-video"
            />
            {game.quantityInStock === 0 && (
              <div className="absolute top-2 right-2 bg-red-500 text-white px-3 py-1 rounded-full text-sm">
                Out of Stock
              </div>
            )}
          </div>

          {/* Game Details */}
          <div className="space-y-6">
            <h1 className="text-3xl font-bold">{game.name}</h1>
            <p className="text-gray-600">{game.description}</p>
            
            <div className="flex flex-wrap gap-4">
              <span className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm">
                {game.category}
              </span>
            </div>

            <div className="flex items-center justify-between py-4 border-t border-gray-100">
              <div>
                <span className="text-3xl font-bold">${game.price.toFixed(2)}</span>
                <span className="ml-2 text-sm text-gray-500">
                  {game.quantityInStock > 0 && `${game.quantityInStock} in stock`}
                </span>
              </div>
            </div>

            {authState === AuthState.CUSTOMER && (
              <div className="flex gap-4">
                <button
                  onClick={handleWishlistToggle}
                  className="flex-1 bg-white border-2 border-blue-500 text-blue-500 px-4 py-2 rounded-lg hover:bg-blue-50 transition-colors"
                >
                  {isInWishlist(game.gameId) ? 'Remove from Wishlist' : 'Add to Wishlist'}
                </button>
                <button
                  disabled={game.quantityInStock === 0}
                  onClick={handleAddToCart}
                  className="flex-1 bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
                >
                  Add to Cart
                </button>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Reviews Section */}
      <div className="mt-12 bg-white rounded-xl shadow-lg p-6">
        <h2 className="text-2xl font-bold mb-6">Customer Reviews</h2>
        {reviews.length > 0 ? (
          <div className="space-y-8">
            {reviews.map((review) => (
              <div key={review.id} className="border-b border-gray-100 pb-8 last:border-0">
                <div className="flex items-center justify-between mb-3">
                  <div>
                    <span className="font-semibold text-lg">{review.customerName}</span>
                    <div className="text-yellow-400 text-lg mt-1">
                      {'★'.repeat(review.rating)}
                      <span className="text-gray-300">{'★'.repeat(5 - review.rating)}</span>
                    </div>
                  </div>
                  <span className="text-gray-500 text-sm">
                    {new Date(review.reviewDate).toLocaleDateString()}
                  </span>
                </div>
                <p className="text-gray-700 mt-2 text-lg">{review.comment}</p>

                {/* Manager Replies */}
                {review.replies && review.replies.map((reply) => (
                  <div key={reply.id} className="ml-8 mt-4 bg-blue-50 p-4 rounded-lg border-l-4 border-blue-500">
                    <div className="flex items-center justify-between mb-2">
                      <div className="flex items-center gap-2">
                        <span className="font-semibold text-blue-600">
                          {reply.managerName}
                        </span>
                        <span className="text-sm text-blue-500">Manager</span>
                      </div>
                      <span className="text-gray-500 text-sm">
                        {new Date(reply.replyDate).toLocaleDateString()}
                      </span>
                    </div>
                    <p className="text-gray-700">{reply.content}</p>
                  </div>
                ))}
              </div>
            ))}
          </div>
        ) : (
          <div className="text-center py-8">
            <p className="text-gray-500 mb-2">No reviews yet</p>
            {authState === AuthState.CUSTOMER && (
              <p className="text-sm text-gray-400">
                Be the first to review this game!
              </p>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default GameDetailsPage;
