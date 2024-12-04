import { Link } from 'react-router-dom';
import { useState } from 'react';
import { LoginUser } from '../../model/user/LoginUser';
import { UserType } from '../../model/user/UserType';
import { AxiosError } from 'axios';
import { login } from '../../api/authService';
import { setAuthUser } from '../../state/authState';
import { GeneralRouteNames } from '../../model/routeNames/GeneralRouteNames';
import { StoredUserData } from '../../model/UserData';

const LoginPage = () => {
  const [errorMsg, setErrorMsg] = useState<string>('');
  const [formData, setFormData] = useState<LoginUser>({
    id: null,
    email: '',
    password: '',
    userType: UserType.CUSTOMER,
  });

  // Handles input change for email and password fields
  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value.trim() });
  };

  // Validates the form fields
  const validateForm = () => {
    if (!formData.email.trim() || !formData.password.trim()) {
      setErrorMsg('Both email and password are required.');
      return false;
    }
    return true;
  };

  // Handles login - calls the API service
  const onLogin = async (
    e: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    e.preventDefault();
    setErrorMsg('');

    if (!validateForm()) {
      return;
    }

    try {
      const responseData = await login(formData);

      // Update formData with the ID from response
      const updatedUser: StoredUserData = {
        email: formData.email,
        userType: formData.userType,
        customerId:
          formData.userType === UserType.CUSTOMER
            ? responseData.customerId
            : undefined,
        staffId:
          formData.userType === UserType.EMPLOYEE ||
          formData.userType === UserType.MANAGER
            ? responseData.staffId
            : undefined,
      };

      // Set the auth user in the state
      setAuthUser(updatedUser);

      // Redirect to base page
      window.location.href = GeneralRouteNames.BASE;
    } catch (error) {
      setErrorMsg((error as AxiosError).message);
      console.error('Login failed:', error);
    }
  };

  return (
    <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-sm">
        <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
          Sign in to your account
        </h2>
      </div>

      <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
        <form className="space-y-6">
          <div>
            <label
              htmlFor="email"
              className="block text-sm font-medium leading-6 text-gray-900"
            >
              Email
            </label>
            <div className="mt-2">
              <input
                id="email"
                name="email"
                type="email"
                required
                value={formData.email}
                onChange={handleInputChange}
                className="block w-full rounded-md border-0 py-1.5 px-3 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>

          <div>
            <label
              htmlFor="password"
              className="block text-sm font-medium leading-6 text-gray-900"
            >
              Password
            </label>
            <div className="mt-2">
              <input
                id="password"
                name="password"
                type="password"
                required
                value={formData.password}
                onChange={handleInputChange}
                className="block w-full rounded-md border-0 py-1.5 px-3 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>

          <div>
            <label
              htmlFor="userType"
              className="block text-sm font-medium leading-6 text-gray-900"
            >
              User Type
            </label>
            <div className="mt-2">
              <select
                id="userType"
                name="userType"
                value={formData.userType}
                onChange={handleInputChange}
                className="block w-full rounded-md border-0 py-1.5 pl-3 pr-10 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              >
                <option value={UserType.CUSTOMER}>Customer</option>
                <option value={UserType.EMPLOYEE}>Employee</option>
                <option value={UserType.MANAGER}>Manager</option>
              </select>
            </div>
          </div>

          <div className="text-center">
            <p className="text-sm text-red-500">{errorMsg}</p>
          </div>

          <div>
            <button
              type="submit"
              onClick={onLogin}
              className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
              Sign in
            </button>
          </div>
        </form>

        <p className="mt-10 text-center text-sm text-gray-500">
          Don&apos;t have an account?{' '}
          <Link
            to={GeneralRouteNames.SIGNUP}
            className="font-semibold leading-6 text-indigo-600 hover:text-indigo-500"
          >
            Click here to register
          </Link>
        </p>
      </div>
    </div>
  );
};

export default LoginPage;
