import React, { useState } from 'react';

interface AccountInfoProps {
  email: string;
  name: string;
  phoneNumber: string;
  onSave: (data: {
    name: string;
    phoneNumber: string;
    password?: string;
  }) => Promise<void>;
}

const AccountInfo: React.FC<AccountInfoProps> = ({
  email,
  name,
  phoneNumber,
  onSave,
}) => {
  const [editing, setEditing] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [profileInput, setProfileInput] = useState({
    name: name || '',
    phoneNumber: phoneNumber || '',
    password: '',
  });

  const handleSave = async () => {
    if (!profileInput.name.trim() || !profileInput.phoneNumber.trim()) {
      setErrorMessage('Name and phone number cannot be empty.');
      return;
    }

    try {
      await onSave({
        name: profileInput.name,
        phoneNumber: profileInput.phoneNumber,
        password: profileInput.password || undefined,
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
    setProfileInput({
      name,
      phoneNumber,
      password: '',
    });
    setErrorMessage(null);
  };

  return (
    <div className="p-6 bg-white rounded-lg shadow-md max-w-2xl mx-auto">
      <div className="border-b pb-4">
        <h3 className="text-xl font-semibold text-gray-800">
          Account Information
        </h3>
        <p className="mt-1 text-sm text-gray-500">
          View and update your profile details. Changes will be saved
          immediately after submission.
        </p>
      </div>

      {editing ? (
        <div className="mt-6">
          <div className="space-y-6">
            {/* Name Input */}
            <div>
              <label
                htmlFor="name"
                className="block text-sm font-medium text-gray-700"
              >
                Full Name <span className="text-red-500">*</span>
              </label>
              <input
                id="name"
                type="text"
                placeholder="Enter your full name"
                className="mt-1 block w-full rounded-md border border-gray-300 shadow-sm px-4 py-2 focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                value={profileInput.name}
                onChange={(e) =>
                  setProfileInput({ ...profileInput, name: e.target.value })
                }
              />
              <p className="mt-1 text-sm text-gray-400">
                Your full name as displayed in records.
              </p>
            </div>

            {/* Phone Number Input */}
            <div>
              <label
                htmlFor="phoneNumber"
                className="block text-sm font-medium text-gray-700"
              >
                Phone Number <span className="text-red-500">*</span>
              </label>
              <input
                id="phoneNumber"
                type="text"
                placeholder="Enter your phone number"
                className="mt-1 block w-full rounded-md border border-gray-300 shadow-sm px-4 py-2 focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                value={profileInput.phoneNumber}
                onChange={(e) =>
                  setProfileInput({
                    ...profileInput,
                    phoneNumber: e.target.value,
                  })
                }
              />
              <p className="mt-1 text-sm text-gray-400">
                Include your country code (e.g., +1).
              </p>
            </div>

            {/* Password Input */}
            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-gray-700"
              >
                New Password <span className="text-gray-400">(optional)</span>
              </label>
              <input
                id="password"
                type="password"
                placeholder="Enter a new password"
                className="mt-1 block w-full rounded-md border border-gray-300 shadow-sm px-4 py-2 focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                value={profileInput.password}
                onChange={(e) =>
                  setProfileInput({ ...profileInput, password: e.target.value })
                }
              />
              <p className="mt-1 text-sm text-gray-400">
                Leave blank if you do not wish to update your password.
              </p>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="mt-6 flex items-center gap-4">
            <button
              className="rounded-md bg-green-600 px-4 py-2 text-sm font-medium text-white shadow hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
              onClick={handleSave}
            >
              Save Changes
            </button>
            <button
              className="rounded-md bg-gray-600 px-4 py-2 text-sm font-medium text-white shadow hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
              onClick={handleCancel}
            >
              Cancel
            </button>
            {errorMessage && (
              <p className="text-sm text-red-500">{errorMessage}</p>
            )}
          </div>
        </div>
      ) : (
        <div className="mt-6">
          <dl className="divide-y divide-gray-200">
            {/* Email */}
            <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-800">
                Email Address
              </dt>
              <dd className="mt-1 text-sm text-gray-600 sm:col-span-2 sm:mt-0">
                {email}
              </dd>
            </div>

            {/* Name */}
            <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-800">Full Name</dt>
              <dd className="mt-1 text-sm text-gray-600 sm:col-span-2 sm:mt-0">
                {name}
              </dd>
            </div>

            {/* Phone Number */}
            <div className="py-4 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium text-gray-800">
                Phone Number
              </dt>
              <dd className="mt-1 text-sm text-gray-600 sm:col-span-2 sm:mt-0">
                {phoneNumber}
              </dd>
            </div>
          </dl>

          {/* Edit Button */}
          <div className="mt-6">
            <button
              className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white shadow hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
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
