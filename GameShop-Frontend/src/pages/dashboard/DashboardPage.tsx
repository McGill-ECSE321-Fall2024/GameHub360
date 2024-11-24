import React, { useEffect, useState } from 'react';
import SalesMetricsBanner from '../../components/SalesMetricsBanner';
import {
  viewStorePolicy,
  manageStorePolicy,
  createStorePolicy,
} from '../../api/storeInfoService';

const DashboardPage = () => {
  const [storePolicy, setStorePolicy] = useState<string | null>(null);
  const [loadingPolicy, setLoadingPolicy] = useState(true);
  const [editing, setEditing] = useState(false);
  const [policyInput, setPolicyInput] = useState('');
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    const fetchStorePolicy = async () => {
      try {
        const policy = await viewStorePolicy();
        setStorePolicy(policy);
      } catch (error) {
        console.error('Error fetching store policy:', (error as Error).message);
      } finally {
        setLoadingPolicy(false);
      }
    };

    fetchStorePolicy();
  }, []);

  const handleSave = async () => {
    if (!policyInput.trim()) {
      // Show an error message if the policy input is empty
      setErrorMessage('Policy cannot be empty.');
      return;
    }

    try {
      if (storePolicy) {
        // Update existing policy
        const updatedPolicy = await manageStorePolicy(policyInput);
        setStorePolicy(updatedPolicy);
      } else {
        // Create a new policy
        const newPolicy = await createStorePolicy(policyInput);
        setStorePolicy(newPolicy);
      }
      setEditing(false); // Exit editing mode
      setErrorMessage(null); // Clear error message
    } catch (error) {
      console.error('Error saving store policy:', (error as Error).message);
      setErrorMessage('An error occurred while saving the policy.');
    }
  };

  const handleCancel = () => {
    setEditing(false);
    setPolicyInput(storePolicy ?? '');
    setErrorMessage(null); // Clear error message
  };

  return (
    <>
      <h2 className="mt-5 mb-3 ml-5 text-xl font-bold tracking-tight text-gray-900">
        Sales Metrics
      </h2>
      <SalesMetricsBanner />

      <div className="mt-7 mb-3 ml-5 flex items-center justify-between pr-5">
        <h2 className="text-xl font-bold tracking-tight text-gray-900">
          Store Policies
        </h2>
        <button
          className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
          onClick={() => {
            setEditing(true);
            setPolicyInput(storePolicy ?? '');
          }}
        >
          {storePolicy ? 'Edit' : 'Create'}
        </button>
      </div>
      <div className="my-5">
        {loadingPolicy ? (
          <p className="ml-5 text-gray-700">Loading store policy...</p>
        ) : editing ? (
          <div className="ml-5 pr-5">
            <textarea
              className="w-full max-w-3xl rounded-md border border-gray-300 px-4 py-2 text-gray-900 focus:outline-none focus:ring-2 focus:ring-blue-600"
              value={policyInput}
              onChange={(e) => setPolicyInput(e.target.value)}
              rows={4}
            />
            <div className="mt-2 flex items-center gap-2">
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
          </div>
        ) : storePolicy ? (
          <p className="ml-5 text-gray-700">{storePolicy}</p>
        ) : (
          <p className="ml-5 text-gray-700">No store policy found.</p>
        )}
      </div>

      <h2 className="mt-7 mb-3 ml-5  text-xl font-bold tracking-tight text-gray-900">
        Customer Orders
      </h2>
    </>
  );
};

export default DashboardPage;
