import { useEffect, useState } from 'react';
import {
  getManagerProfile,
  updateManagerProfile,
} from '../../api/managerService';
import AccountInfo from '../../components/AccountInfo';

const ManagerProfilePage = () => {
  const [managerProfile, setManagerProfile] = useState<{
    email: string;
    name: string;
    phoneNumber: string;
  } | null>(null);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const profile = await getManagerProfile();
        setManagerProfile({
          email: profile.email,
          name: profile.name || '',
          phoneNumber: profile.phoneNumber || '',
        });
      } catch (error) {
        console.error('Error fetching manager profile:', error);
        setErrorMessage('Failed to load profile information.');
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, []);

  const handleSave = async (data: {
    name: string;
    phoneNumber: string;
    password?: string;
  }) => {
    if (!managerProfile) {
      console.error('Manager profile is not loaded');
      return;
    }

    try {
      const updatedProfile = await updateManagerProfile({
        email: managerProfile.email, // Email is required by the backend
        name: data.name,
        phoneNumber: data.phoneNumber,
        password: data.password, // Password is optional
      });

      setManagerProfile({
        email: updatedProfile.email,
        name: updatedProfile.name || '',
        phoneNumber: updatedProfile.phoneNumber || '',
      });

      setErrorMessage(null); // Clear error message upon successful save
    } catch (error) {
      console.error('Error updating manager profile:', error);
      setErrorMessage('Failed to update profile. Please check your input.');
    }
  };

  if (loading) {
    return (
      <p className="text-center text-gray-700">Loading manager profile...</p>
    );
  }

  if (errorMessage) {
    return (
      <div className="p-6 bg-red-100 text-red-700 rounded-lg shadow-md max-w-md mx-auto">
        <p>{errorMessage}</p>
      </div>
    );
  }

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      {managerProfile ? (
        <AccountInfo
          email={managerProfile.email}
          name={managerProfile.name}
          phoneNumber={managerProfile.phoneNumber}
          onSave={handleSave}
        />
      ) : (
        <p className="text-center text-gray-700">
          No profile information found.
        </p>
      )}
    </div>
  );
};

export default ManagerProfilePage;
