import React, { useEffect, useState } from 'react';
import AccountInfo from '../../components/AccountInfo';
import { getCustomerProfile, updateCustomerProfile } from '../../api/customerService';
import { getAuthUser } from '../../state/authState';

const CustomerProfilePage: React.FC = () => {
  const [customerProfile, setCustomerProfile] = useState<{
    customerId: number; 
    email: string;
    name: string;
    phoneNumber: string;
  } | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        // Retrieve `authUser` from localStorage
        const authUser = getAuthUser();
        if (!authUser || !authUser.id) {
          console.error('No authenticated user found or user ID is missing.');
          return;
        }

        // Fetch the customer profile using the ID
        const profile = await getCustomerProfile(authUser.id);
        console.log(profile);
        setCustomerProfile({
          customerId: profile.customerId,
          email: profile.email,
          name: profile.name || '',
          phoneNumber: profile.phoneNumber || '',
        });
      } catch (error) {
        console.error('Error fetching customer profile:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
    console.log("Customer Profile Data after fetch: ", customerProfile);
  }, []);

  const handleSave = async (data: { name: string; phoneNumber: string; password?: string }) => {
    if (!customerProfile) {
      console.error('Customer profile is not loaded');
      return;
    }

    try {
      const updatedProfile = await updateCustomerProfile(customerProfile.customerId, {
        email: customerProfile.email, // Email is required by the backend
        name: data.name,
        phoneNumber: data.phoneNumber,
        password: data.password,
      });

      setCustomerProfile({
        customerId: updatedProfile.customerId, // Retain the same ID
        email: updatedProfile.email,
        name: updatedProfile.name || '',
        phoneNumber: updatedProfile.phoneNumber || '',
      });
    } catch (error) {
      console.error('Error updating customer profile:', error);
      throw error;
    }
  };

  if (loading) {
    return <p>Loading customer profile...</p>;
  }

  return (
    <div>
      {customerProfile && (
        <AccountInfo
          email={customerProfile.email}
          name={customerProfile.name}
          phoneNumber={customerProfile.phoneNumber}
          onSave={handleSave}
        />
      )}
    </div>
  );
};

export default CustomerProfilePage;
