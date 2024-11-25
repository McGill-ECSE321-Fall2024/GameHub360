import React, { useState, useEffect } from 'react';

interface AccountInfoProps {
  email: string;
  name: string;
  phoneNumber: string;
  onSave: (data: { name: string; phoneNumber: string; password?: string }) => Promise<void>; // Include `id` in onSave
}

const AccountInfo: React.FC<AccountInfoProps> = ({ email, name, phoneNumber, onSave }) => {
  const [editing, setEditing] = useState(false); // Tracks whether the component is in edit mode
  const [errorMessage, setErrorMessage] = useState<string | null>(null); // Error message state
  const [profileInput, setProfileInput] = useState({
    name: name || '',
    phoneNumber: phoneNumber || '',
    password: '',
  });

  // Handles save action
  const handleSave = async () => {
    if (!profileInput.name.trim() || !profileInput.phoneNumber.trim()) {
      setErrorMessage('Name and phone number cannot be empty.');
      return;
    }

    try {
      // Call onSave with updated profile data
      await onSave({
        name: profileInput.name,
        phoneNumber: profileInput.phoneNumber,
        password: profileInput.password || undefined, // Password is optional
      });
      setEditing(false); // Exit edit mode
      setErrorMessage(null); // Clear error messages
    } catch (error) {
      console.error('Error updating profile:', error);
      setErrorMessage('Failed to update profile. Please check your input.');
    }
  };

  // Handles cancel action
  const handleCancel = () => {
    setEditing(false); // Exit edit mode
    // Reset profileInput to the original props
    setProfileInput({
      name,
      phoneNumber,
      password: '',
    });
    setErrorMessage(null); // Clear error messages
  };

  return (
    <div className="p-6">
      <div className="px-4 sm:px-0">
        <h3 className="text-base font-semibold text-gray-900">Account Information</h3>
        <p className="mt-1 max-w-2xl text-sm text-gray-500">
          View and update your profile information.
        </p>
      </div>

      {editing ? (
        <div className="mt-6 border-t border-gray-100">
          <dl className="divide-y divide-gray-100">
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-900">Name</dt>
              <input
                type="text"
                className="mt-1 text-sm text-gray-700 sm:col-span-2 sm:mt-0 rounded-md border border-gray-300 px-4 py-2 focus:ring-blue-600 focus:outline-none"
                value={profileInput.name}
                onChange={(e) => setProfileInput({ ...profileInput, name: e.target.value })}
              />
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-900">Phone Number</dt>
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
              <dt className="text-sm font-medium text-gray-900">Password (optional)</dt>
              <input
                type="password"
                className="mt-1 text-sm text-gray-700 sm:col-span-2 sm:mt-0 rounded-md border border-gray-300 px-4 py-2 focus:ring-blue-600 focus:outline-none"
                value={profileInput.password}
                onChange={(e) => setProfileInput({ ...profileInput, password: e.target.value })}
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
              {errorMessage && <p className="text-sm text-red-600">{errorMessage}</p>}
            </div>
          </dl>
        </div>
      ) : (
        <div className="mt-6 border-t border-gray-100">
          <dl className="divide-y divide-gray-100">
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-900">Email</dt>
              <dd className="mt-1 text-sm text-gray-700 sm:col-span-2 sm:mt-0">{email}</dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-900">Name</dt>
              <dd className="mt-1 text-sm text-gray-700 sm:col-span-2 sm:mt-0">{name}</dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-900">Phone Number</dt>
              <dd className="mt-1 text-sm text-gray-700 sm:col-span-2 sm:mt-0">{phoneNumber}</dd>
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
      )}
    </div>
  );
};

export default AccountInfo;
