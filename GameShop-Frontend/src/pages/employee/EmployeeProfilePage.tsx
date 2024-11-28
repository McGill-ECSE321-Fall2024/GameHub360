import React, { useEffect, useState } from 'react';
import AccountInfo from '../../components/AccountInfo';
import { getEmployeeProfile, updateEmployeeProfile } from '../../api/employeeService';
import { getAuthUser } from '../../state/authState';
const EmployeeProfilePage: React.FC = () => {
  const [employeeProfile, setEmployeeProfile] = useState<{
    staffId: number; 
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
        // Fetch the employee profile using the ID
        const profile = await getEmployeeProfile(authUser.id);
        console.log(profile);
        setEmployeeProfile({
          staffId: profile.staffId,
          email: profile.email,
          name: profile.name || '',
          phoneNumber: profile.phoneNumber || '',
        });
      } catch (error) {
        console.error('Error fetching employee profile:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchProfile();
    console.log("employee Profile Data after fetch: ", employeeProfile);
  }, []);
  const handleSave = async (data: { name: string; phoneNumber: string; password?: string }) => {
    if (!employeeProfile) {
      console.error('employee profile is not loaded');
      return;
    }
    try {
      const updatedProfile = await updateEmployeeProfile(employeeProfile.staffId, {
        email: employeeProfile.email, // Email is required by the backend
        name: data.name,
        phoneNumber: data.phoneNumber,
        password: data.password,
      });
      setEmployeeProfile({
        staffId: updatedProfile.staffId, // Retain the same ID
        email: updatedProfile.email,
        name: updatedProfile.name || '',
        phoneNumber: updatedProfile.phoneNumber || '',
      });
    } catch (error) {
      console.error('Error updating employee profile:', error);
      throw error;
    }
  };
  if (loading) {
    return <p>Loading employee profile...</p>;
  }
  return (
    <div>
      {employeeProfile && (
        <AccountInfo
          email={employeeProfile.email}
          name={employeeProfile.name}
          phoneNumber={employeeProfile.phoneNumber}
          onSave={handleSave}
        />
      )}
    </div>
  );
};
export default EmployeeProfilePage;