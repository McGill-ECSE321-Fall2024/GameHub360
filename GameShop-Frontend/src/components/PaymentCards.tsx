import React, { useState } from "react";
import {
  Accordion,
  AccordionItem,
  AccordionTrigger,
  AccordionContent,
} from "./ui/Accordion";
import { Button } from "./ui/Button";
import CodeInputForm from "./ui/CodeInputForm";

interface PaymentCard {
  paymentDetailsId: number;
  cardName: string;
  cardNumber: string;
  postalCode: string;
  expMonth: number;
  expYear: number;
  customerId: number;
  paidOrdersIds: number[];
}

interface PaymentDetailsRequest {
  cardName: string;
  cardNumber: string;
  postalCode: string;
  expMonth: number;
  expYear: number;
}

interface PaymentCardsProps {
  paymentCards: PaymentCard[];
  onUpdate: (cardId: number, updatedCard: PaymentCard) => void;
  onAdd: (newCard: PaymentDetailsRequest) => void;
}

const PaymentCards: React.FC<PaymentCardsProps> = ({
  paymentCards,
  onUpdate,
  onAdd,
}) => {
  const [editingCard, setEditingCard] = useState<PaymentCard | null>(null);
  const [addingCard, setAddingCard] = useState(false);
  const [newCard, setNewCard] = useState<PaymentDetailsRequest>({
    cardName: "",
    cardNumber: "",
    postalCode: "",
    expMonth: 1,
    expYear: new Date().getFullYear(),
  });

  const handleAdd = () => {
    onAdd(newCard);
    setAddingCard(false); // Close the form after adding
    setNewCard({
      cardName: "",
      cardNumber: "",
      postalCode: "",
      expMonth: 1,
      expYear: new Date().getFullYear(),
    });
  };

  return (
    <div className="p-6">
      <div className="px-4 sm:px-0">
        <h3 className="text-base font-semibold text-gray-900">Payment Cards</h3>
        <p className="mt-1 max-w-2xl text-sm text-gray-500">
          Manage payment cards linked to your profile.
        </p>
      </div>

      {/* Add Card Button */}
      {addingCard ? (
        <div className="flex flex-col gap-2 mt-4 mb-4 border p-4 rounded shadow">
          <input
            type="text"
            value={newCard.cardName}
            onChange={(e) => setNewCard({ ...newCard, cardName: e.target.value })}
            placeholder="Card Name"
            className="border border-gray-300 p-2 rounded"
          />
          <CodeInputForm
            length={16} // 16-digit card number
            onComplete={(code) => setNewCard({ ...newCard, cardNumber: code })}
          />
          <input
            type="text"
            value={newCard.postalCode}
            onChange={(e) => setNewCard({ ...newCard, postalCode: e.target.value })}
            placeholder="Postal Code"
            className="border border-gray-300 p-2 rounded"
          />
          <div className="flex gap-2">
            <input
              type="number"
              value={newCard.expMonth}
              onChange={(e) =>
                setNewCard({ ...newCard, expMonth: parseInt(e.target.value, 10) })
              }
              placeholder="Exp Month"
              className="border border-gray-300 p-2 rounded"
              min="1"
              max="12"
            />
            <input
              type="number"
              value={newCard.expYear}
              onChange={(e) =>
                setNewCard({ ...newCard, expYear: parseInt(e.target.value, 10) })
              }
              placeholder="Exp Year"
              className="border border-gray-300 p-2 rounded"
              min={new Date().getFullYear()}
            />
          </div>
          <div className="flex justify-end gap-2 mt-4">
            <Button
              variant="default"
              className="hover:bg-blue-600 hover:text-white"
              onClick={handleAdd}
            >
              Save
            </Button>
            <Button
              variant="destructive"
              className="hover:bg-gray-600 hover:text-white"
              onClick={() => setAddingCard(false)}
            >
              Cancel
            </Button>
          </div>
        </div>
      ) : (
        <Button
          variant="outline"
          className="p-4 mt-4 mb-4 hover:bg-blue-600 hover:text-white"
          onClick={() => setAddingCard(true)}
        >
          Add Card
        </Button>
      )}

      {/* Accordion for existing cards */}
      <Accordion type="single" collapsible>
        {paymentCards.map((card) => (
          <AccordionItem key={card.paymentDetailsId} value={`card-${card.paymentDetailsId}`}>
            <AccordionTrigger>
              **** **** **** {card.cardNumber.slice(-4)}
            </AccordionTrigger>
            <AccordionContent>
              <div className="flex justify-between items-center">
                {editingCard?.paymentDetailsId === card.paymentDetailsId ? (
                  // Editing form
                  <div className="flex flex-col gap-2">
                    <input
                      type="text"
                      value={editingCard.cardName}
                      onChange={(e) =>
                        setEditingCard({ ...editingCard, cardName: e.target.value })
                      }
                      placeholder="Card Name"
                      className="border border-gray-300 p-2 rounded"
                    />
                    <input
                      type="text"
                      value={editingCard.cardNumber}
                      readOnly
                      placeholder="Card Number (Read Only)"
                      className="border border-gray-300 p-2 rounded bg-gray-100 text-gray-500"
                    />
                    <input
                      type="text"
                      value={editingCard.postalCode}
                      onChange={(e) =>
                        setEditingCard({ ...editingCard, postalCode: e.target.value })
                      }
                      placeholder="Postal Code"
                      className="border border-gray-300 p-2 rounded"
                    />
                    <div className="flex gap-2">
                      <input
                        type="number"
                        value={editingCard.expMonth}
                        onChange={(e) =>
                          setEditingCard({
                            ...editingCard,
                            expMonth: parseInt(e.target.value, 10),
                          })
                        }
                        placeholder="Exp Month"
                        className="border border-gray-300 p-2 rounded"
                        min="1"
                        max="12"
                      />
                      <input
                        type="number"
                        value={editingCard.expYear}
                        onChange={(e) =>
                          setEditingCard({
                            ...editingCard,
                            expYear: parseInt(e.target.value, 10),
                          })
                        }
                        placeholder="Exp Year"
                        className="border border-gray-300 p-2 rounded"
                        min={new Date().getFullYear()}
                      />
                    </div>
                    <div className="flex justify-end gap-2 mt-4">
                      <Button
                        variant="default"
                        className="hover:bg-blue-600 hover:text-white"
                        onClick={() => {
                          if (editingCard) {
                            onUpdate(editingCard.paymentDetailsId, editingCard); // Call onUpdate with updated data
                            setEditingCard(null); // Close the editing form
                          }
                        }}
                      >
                        Save
                      </Button>
                      <Button
                        variant="destructive"
                        className="hover:bg-gray-600 hover:text-white"
                        onClick={() => setEditingCard(null)}
                      >
                        Cancel
                      </Button>
                    </div>
                  </div>
                ) : (
                  // Displaying card info
                  <div className="flex justify-between items-center w-full">
                    <div className="flex flex-col gap-2">
                      <p>
                        <strong>Card Name:</strong> {card.cardName}
                      </p>
                      <p>
                        <strong>Expiration:</strong> {card.expMonth}/{card.expYear}
                      </p>
                      <p>
                        <strong>Postal Code:</strong> {card.postalCode}
                      </p>
                    </div>
                    <div className="flex gap-4">
                      <Button
                        variant="default"
                        className="hover:bg-blue-600 hover:text-white"
                        onClick={() => setEditingCard(card)}
                      >
                        Update
                      </Button>
                      <Button
                        variant="destructive"
                        className="hover:bg-red-600 hover:text-white"
                      >
                        Remove
                      </Button>
                    </div>
                  </div>
                )}
              </div>
            </AccordionContent>
          </AccordionItem>
        ))}
      </Accordion>
    </div>
  );
};

export default PaymentCards;
