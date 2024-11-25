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
    <div className="p-6">
      <div className="px-4 sm:px-0">
        <h3 className="text-base font-semibold text-gray-900">
          Manager Profile
        </h3>
        <p className="mt-1 max-w-2xl text-sm text-gray-500">
          View and update your profile information.
        </p>
      </div>

      {loading ? (
        <p className="text-gray-700">Loading profile...</p>
      ) : editing ? (
        <div className="mt-6 border-t border-gray-100">
          <dl className="divide-y divide-gray-100">
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-900">Name</dt>
              <input
                type="text"
                className="mt-1 text-sm text-gray-700 sm:col-span-2 sm:mt-0 rounded-md border border-gray-300 px-4 py-2 focus:ring-blue-600 focus:outline-none"
                value={profileInput.name}
                onChange={(e) =>
                  setProfileInput({ ...profileInput, name: e.target.value })
                }
              />
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-900">
                Phone Number
              </dt>
              <input
                type="text"
                className="mt-1 text-sm text-gray-700 sm:col-span-2 sm:mt-0 rounded-md border border-gray-300 px-4 py-2 focus:ring-blue-600 focus:outline-none"
                value={profileInput.phoneNumber}
                onChange={(e) =>
                  setProfileInput({
                    ...profileInput,
                    phoneNumber: e.target.value,
                  })
                }
              />
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-900">
                Password (optional)
              </dt>
              <input
                type="password"
                className="mt-1 text-sm text-gray-700 sm:col-span-2 sm:mt-0 rounded-md border border-gray-300 px-4 py-2 focus:ring-blue-600 focus:outline-none"
                value={profileInput.password}
                onChange={(e) =>
                  setProfileInput({ ...profileInput, password: e.target.value })
                }
              />
            </div>
            <div className="mt-6 flex items-center gap-4 px-4">
              <button
                className="rounded-md bg-green-600 px-4 py-2 text-sm font-medium text-white hover:bg-green-700"
                onClick={handleSave}
              >
                Save
              </button>
              <button
                className="rounded-md bg-gray-600 px-4 py-2 text-sm font-medium text-white hover:bg-gray-700"
                onClick={handleCancel}
              >
                Cancel
              </button>
              {errorMessage && (
                <p className="text-sm text-red-600">{errorMessage}</p>
              )}
            </div>
          </dl>
        </div>
      ) : managerProfile ? (
        <div className="mt-6 border-t border-gray-100">
          <dl className="divide-y divide-gray-100">
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-900">Email</dt>
              <dd className="mt-1 text-sm text-gray-700 sm:col-span-2 sm:mt-0">
                {managerProfile.email}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-900">Name</dt>
              <dd className="mt-1 text-sm text-gray-700 sm:col-span-2 sm:mt-0">
                {managerProfile.name}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-900">
                Phone Number
              </dt>
              <dd className="mt-1 text-sm text-gray-700 sm:col-span-2 sm:mt-0">
                {managerProfile.phoneNumber}
              </dd>
            </div>
          </dl>
          <div className="mt-6">
            <button
              className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
              onClick={() => setEditing(true)}
            >
              Edit Profile
            </button>
          </div>
        </div>
      ) : (
        <p className="text-gray-700">No profile information found.</p>
      )}
    </div>
  );
};

export default ManagerProfilePage;
