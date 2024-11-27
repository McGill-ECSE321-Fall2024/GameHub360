"use client";

import React, { useEffect, useState } from "react";
import AccountInfo from "../../components/AccountInfo";
import PaymentCards from "../../components/PaymentCards";
import {
  getCustomerProfile,
  updateCustomerProfile,
  getCustomerPaymentCards,
  PaymentCard,
} from "../../api/customerService";
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
        <PaymentCards paymentCards={paymentCards} />
      )}
    </div>
  );
};

export default CustomerProfilePage;
