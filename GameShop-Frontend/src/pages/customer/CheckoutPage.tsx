import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import PaymentCards from "../../components/PaymentCards";
import { Button } from "../../components/ui/Button";
import Modal from "../../components/ui/Modal";
import { createPaymentCard, getCustomerPaymentCards, createCustomerOrder } from "../../api/customerService";
import { PaymentCard, PaymentDetailsRequest, CustomerOrderRequest } from "../../api/customerService";

const CheckoutPage: React.FC = () => {
  const [savedCards, setSavedCards] = useState<PaymentCard[]>([]);
  const [selectedCardId, setSelectedCardId] = useState<number | null>(null);
  const [addingNewCard, setAddingNewCard] = useState(false);
  const [newCard, setNewCard] = useState<PaymentDetailsRequest | null>(null);
  const [showModal, setShowModal] = useState(false);

  const navigate = useNavigate();

  // Fetch customer and cart data from local storage
  const customerData = JSON.parse(localStorage.getItem("loggedInUser") || "{}");
  const cartData = JSON.parse(localStorage.getItem("cart") || "{}");

  useEffect(() => {
    // Fetch saved cards from backend
    const fetchCards = async () => {
      const cards = await getCustomerPaymentCards(customerData.id);
      setSavedCards(cards.paymentCards);
    };
    fetchCards();
  }, [customerData.id]);

  const handlePlaceOrder = async () => {
    if (!selectedCardId && !newCard) {
      alert("Please select or add a payment method.");
      return;
    }
  
    // Ensure cart and customer data exist and are valid
    if (!customerData.id || !cartData.items || cartData.items.length === 0) {
      alert("No valid customer or cart data found. Please check and try again.");
      return;
    }
  
    const orderRequest: CustomerOrderRequest = {
      orderedGameIds: cartData.items.map((item: { gameId: number }) => item.gameId), // Extract game IDs from cart
      paymentInformationId: selectedCardId!, // Selected card ID
      customerId: customerData.id, // Logged-in customer ID
    };
  
    try {
      await createCustomerOrder(orderRequest);
      setShowModal(true); // Show confirmation popup
    } catch (error) {
      console.error("Error placing order:", error);
      alert("Failed to place the order. Please try again.");
    }
  };
  
  const handleConfirm = () => {
    setShowModal(false);
    localStorage.removeItem("cart"); // Clear the cart
    navigate("/orders"); // Redirect to orders page
  };

  const handleAddNewCard = async (card: PaymentDetailsRequest) => {
    try {
      // Save card to backend
      const savedCard = await createPaymentCard(customerData.id, card); // API call to save card
  
      // Re-fetch updated list of cards
      const updatedCards = await getCustomerPaymentCards(customerData.id);
      setSavedCards(updatedCards.paymentCards);
  
      setAddingNewCard(false); // Close the "Add New Card" form
    } catch (error) {
      console.error("Error adding new card:", error);
      alert("Failed to save the card. Please try again.");
    }
  };  

  return (
    <div className="p-6">
      <h2 className="text-lg font-bold mb-4">Checkout</h2>

      <div className="mb-6">
        <h3 className="font-medium">Your Cart</h3>
        {cartData.items?.length > 0 ? (
          <ul className="mb-4">
            {cartData.items.map((item: any) => (
              <li key={item.gameId} className="flex justify-between border-b py-2">
                <div>
                  <p className="font-medium">{item.name}</p>
                  <p className="text-sm text-gray-500">Quantity: {item.quantity}</p>
                </div>
                <p className="text-gray-700">${item.price.toFixed(2)}</p>
              </li>
            ))}
          </ul>
        ) : (
          <p>Your cart is empty.</p>
        )}
        <p className="font-bold text-right">Total: ${cartData.total.toFixed(2)}</p>
      </div>

      <div className="mb-6">
        <h3 className="font-medium">Payment Information</h3>
        {savedCards.length > 0 ? (
          <div>
            <label htmlFor="saved-cards" className="block mb-2 text-sm">
              Select a saved card:
            </label>
            <select
              id="saved-cards"
              value={selectedCardId || ""}
              onChange={(e) => setSelectedCardId(Number(e.target.value))}
              className="border border-gray-300 p-2 rounded w-full"
            >
              <option value="">-- Select a Card --</option>
              {savedCards.map((card) => (
                <option key={card.paymentDetailsId} value={card.paymentDetailsId}>
                  **** **** **** {card.cardNumber.slice(-4)}
                </option>
              ))}
            </select>
          </div>
        ) : (
          <p>No saved cards found. Please add a new card.</p>
        )}
      </div>

      <div className="mb-6">
        <Button
          variant="outline"
          onClick={() => setAddingNewCard((prev) => !prev)}
          className="mb-4"
        >
          {addingNewCard ? "Cancel Adding New Card" : "Add a New Card"}
        </Button>
        {addingNewCard && (
          <PaymentCards
            onAdd={handleAddNewCard}
            paymentCards={[]}
            onUpdate={() => {}}
          />
        )}
      </div>

      <div className="flex justify-between gap-4 mt-6">
        <Button
          variant="outline"
          onClick={() => navigate("/cart")} // Navigate back to cart
          className="mt-4"
        >
          Go Back to Cart
        </Button>

        <Button
          variant="default"
          onClick={handlePlaceOrder}
          disabled={!selectedCardId && !newCard}
          className="mt-4"
        >
          Place Order
        </Button>
      </div>

      {/* Confirmation Modal */}
      <Modal isOpen={showModal} onClose={handleConfirm}>
        <h2 className="text-lg font-bold">Order Confirmed!</h2>
        <p>Your order has been successfully placed.</p>
        <Button onClick={handleConfirm}>Go to Orders</Button>
      </Modal>
    </div>
  );
};

export default CheckoutPage;
