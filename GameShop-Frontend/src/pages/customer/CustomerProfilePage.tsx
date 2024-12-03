"use client";

import React, { useEffect, useState } from "react";
import AccountInfo from "../../components/AccountInfo";
import PaymentCards from "../../components/PaymentCards";

import {
  getCustomerProfile,
  updateCustomerProfile,
  getCustomerPaymentCards,
  updatePaymentCard,
  createPaymentCard, // Import createPaymentCard
} from "../../api/customerService";
import { PaymentCard, PaymentDetailsRequest } from "../../model/customer/paymentCardInterfaces";
import { getAuthUser } from "../../state/authState";

const CustomerProfilePage: React.FC = () => {
  const [customerProfile, setCustomerProfile] = useState<{
    customerId: number;
    email: string;
    name: string;
    phoneNumber: string;
  } | null>(null);

  const [paymentCards, setPaymentCards] = useState<PaymentCard[]>([]);
  const [profileLoading, setProfileLoading] = useState(true);
  const [cardLoading, setCardLoading] = useState(true);

  // Fetch customer profile and payment cards
  useEffect(() => {
    const fetchData = async () => {
      try {
        const authUser = getAuthUser();
        if (!authUser || !authUser.id) {
          console.error("No authenticated user found or user ID is missing.");
          return;
        }

        // Fetch customer profile
        const profile = await getCustomerProfile(authUser.id);
        setCustomerProfile({
          customerId: profile.customerId,
          email: profile.email,
          name: profile.name || "",
          phoneNumber: profile.phoneNumber || "",
        });
        setProfileLoading(false);

        // Fetch payment cards
        const paymentCardData = await getCustomerPaymentCards(authUser.id);
        setPaymentCards(paymentCardData.paymentCards);
        console.log(paymentCardData);
        setCardLoading(false);
      } catch (error) {
        console.error("Error fetching data:", error);
        setProfileLoading(false);
        setCardLoading(false);
      }
    };

    fetchData();
  }, []);

  // Handle saving updated profile data
  const handleSave = async (data: {
    name: string;
    phoneNumber: string;
    password?: string;
  }) => {
    if (!customerProfile) {
      console.error("Customer profile is not loaded.");
      return;
    }

    try {
      const updatedProfile = await updateCustomerProfile(
        customerProfile.customerId,
        {
          email: customerProfile.email, // Email is required by the backend
          name: data.name,
          phoneNumber: data.phoneNumber,
          password: data.password,
        }
      );

      setCustomerProfile({
        customerId: updatedProfile.customerId,
        email: updatedProfile.email,
        name: updatedProfile.name || "",
        phoneNumber: updatedProfile.phoneNumber || "",
      });
    } catch (error) {
      console.error("Error updating customer profile:", error);
      throw error;
    }
  };

  // Handle card details update
  const handleUpdateCard = async (cardId: number, updatedCard: PaymentCard) => {
    if (!customerProfile) {
      console.error("Customer profile is not loaded.");
      return;
    }

    try {
      const updated = await updatePaymentCard(customerProfile.customerId, cardId, {
        cardName: updatedCard.cardName,
        cardNumber: updatedCard.cardNumber,
        postalCode: updatedCard.postalCode,
        expMonth: updatedCard.expMonth,
        expYear: updatedCard.expYear,
      });
      setPaymentCards((prevCards) =>
        prevCards.map((card) =>
          card.paymentDetailsId === cardId ? { ...card, ...updated } : card
        )
      );
    } catch (error) {
      console.error("Error updating payment card:", error);
    }
  };

  // Handle adding a new card
  const handleAddCard = async (newCard: PaymentDetailsRequest) => {
    if (!customerProfile) {
      console.error("Customer profile is not loaded.");
      return;
    }

    try {
      const addedCard = await createPaymentCard(customerProfile.customerId, newCard);
      setPaymentCards((prevCards) => [...prevCards, addedCard]); // Add the new card to the list
    } catch (error) {
      console.error("Error adding new payment card:", error);
    }
  };

  if (profileLoading) {
    return <p>Loading customer profile...</p>;
  }

  return (
    <div className="p-6 space-y-6">
      {customerProfile && (
        <AccountInfo
          email={customerProfile.email}
          name={customerProfile.name}
          phoneNumber={customerProfile.phoneNumber}
          onSave={handleSave}
        />
      )}

      {cardLoading ? (
        <p>Loading payment cards...</p>
      ) : (
        <PaymentCards
          paymentCards={paymentCards}
          onUpdate={handleUpdateCard}
          onAdd={handleAddCard} // Pass the add card handler
        />
      )}
    </div>
  );
};

export default CustomerProfilePage;