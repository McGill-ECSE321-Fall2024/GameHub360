import { useEffect, useState } from 'react';
import {
  getManagerProfile,
  updateManagerProfile,
} from '../../api/managerService';

const ManagerProfilePage = () => {
  const [managerProfile, setManagerProfile] = useState<{
    email: string;
    name: string;
    phoneNumber: string;
  } | null>(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [profileInput, setProfileInput] = useState({
    name: '',
    phoneNumber: '',
    password: '',
  });

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const profile = await getManagerProfile();
        setManagerProfile({
          email: profile.email,
          name: profile.name || '',
          phoneNumber: profile.phoneNumber || '',
        });
        setProfileInput({
          name: profile.name || '',
          phoneNumber: profile.phoneNumber || '',
          password: '',
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

  const handleSave = async () => {
    if (!profileInput.name.trim() || !profileInput.phoneNumber.trim()) {
      setErrorMessage('Name and phone number cannot be empty.');
      return;
    }

    try {
      const updatedProfile = await updateManagerProfile({
        email: managerProfile?.email || '',
        name: profileInput.name,
        phoneNumber: profileInput.phoneNumber,
        password: profileInput.password || undefined,
      });

      setManagerProfile({
        email: updatedProfile.email,
        name: updatedProfile.name || '',
        phoneNumber: updatedProfile.phoneNumber || '',
      });

      setEditing(false);
      setErrorMessage(null);
    } catch (error) {
      console.error('Error updating profile:', error);
      setErrorMessage('Failed to update profile. Please check your input.');
    }
  };

  const handleCancel = () => {
    setEditing(false);
    if (managerProfile) {
      setProfileInput({
        name: managerProfile.name,
        phoneNumber: managerProfile.phoneNumber,
        password: '',
      });
    }
    setErrorMessage(null);
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="mb-6">
        <h2 className="text-3xl font-semibold text-gray-800">
          Manager Profile
        </h2>
        <p className="mt-1 text-gray-600">
          View and update your profile information.
        </p>
      </div>

      {loading ? (
        <p className="text-gray-700">Loading profile...</p>
      ) : editing ? (
        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700">
              Name
            </label>
            <input
              type="text"
              className="w-full mt-1 rounded-md border border-gray-300 px-4 py-2 focus:ring-blue-500 focus:border-blue-500"
              value={profileInput.name}
              onChange={(e) =>
                setProfileInput({ ...profileInput, name: e.target.value })
              }
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700">
              Phone Number
            </label>
            <input
              type="text"
              className="w-full mt-1 rounded-md border border-gray-300 px-4 py-2 focus:ring-blue-500 focus:border-blue-500"
              value={profileInput.phoneNumber}
              onChange={(e) =>
                setProfileInput({
                  ...profileInput,
                  phoneNumber: e.target.value,
                })
              }
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700">
              Password (optional)
            </label>
            <input
              type="password"
              className="w-full mt-1 rounded-md border border-gray-300 px-4 py-2 focus:ring-blue-500 focus:border-blue-500"
              value={profileInput.password}
              onChange={(e) =>
                setProfileInput({ ...profileInput, password: e.target.value })
              }
            />
          </div>
          <div className="flex items-center gap-4">
            <button
              className="bg-green-500 text-white px-4 py-2 rounded-lg shadow hover:bg-green-600"
              onClick={handleSave}
            >
              Save
            </button>
            <button
              className="bg-gray-500 text-white px-4 py-2 rounded-lg shadow hover:bg-gray-600"
              onClick={handleCancel}
            >
              Cancel
            </button>
            {errorMessage && (
              <p className="text-sm text-red-500">{errorMessage}</p>
            )}
          </div>
        </div>
      ) : managerProfile ? (
        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="mb-4">
            <p className="text-sm font-medium text-gray-700">Email</p>
            <p className="text-gray-800">{managerProfile.email}</p>
          </div>
          <div className="mb-4">
            <p className="text-sm font-medium text-gray-700">Name</p>
            <p className="text-gray-800">{managerProfile.name}</p>
          </div>
          <div className="mb-4">
            <p className="text-sm font-medium text-gray-700">Phone Number</p>
            <p className="text-gray-800">{managerProfile.phoneNumber}</p>
          </div>
          <button
            className="bg-blue-500 text-white px-4 py-2 rounded-lg shadow hover:bg-blue-600"
            onClick={() => setEditing(true)}
          >
            Edit Profile
          </button>
        </div>
      ) : (
        <p className="text-gray-700">No profile information found.</p>
      )}
    </div>
  );
};

export default ManagerProfilePage;
