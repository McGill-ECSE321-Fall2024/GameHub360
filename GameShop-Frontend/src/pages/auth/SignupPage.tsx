import { Link } from 'react-router-dom';
import { useState } from 'react';
import { SignupUser } from '../../model/user/SignupUser';
import { GeneralRouteNames } from '../../model/routeNames/GeneralRouteNames';
import { registerCustomer } from '../../api/authService';
import { setAuthUser } from '../../state/authState';
import { UserType } from '../../model/user/UserType';

const SignUpPage = () => {
  const [errorMsg, setErrorMsg] = useState<string>('');
  const [formData, setFormData] = useState<SignupUser>({
    email: '',
    password: '',
    name: '',
    phoneNumber: '',
  });

  // Function to handle input change
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value.trim() });
  };

  // Function to validate the form fields
  const validateForm = () => {
    if (!formData.email.trim() || !formData.password.trim()) {
      setErrorMsg('Email and password are required.');
      return false;
    }
    return true;
  };

  const onRegister = async (
    e: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    e.preventDefault();
    setErrorMsg('');

    if (!validateForm()) {
      return;
    }

    try {
      // Call the register function
      await registerCustomer(formData);
      console.log('Registration successful');

      // Build user state for psuedo-login
      const loginData = {
        email: formData.email,
        password: formData.password,
        userType: UserType.CUSTOMER,
      };

      // Set the auth user in the state
      setAuthUser(loginData);

      // Refresh to update the router
      window.location.reload();
    } catch (error) {
      setErrorMsg((error as Error).message);
      console.error('Registration failed:', error);
    }
  };

  return (
    <div className="flex min-h-full flex-1 items-center justify-center px-4 py-6 sm:px-6 lg:px-8">
      <div className="w-full max-w-sm space-y-10">
        <div>
          <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
            Create a customer account
          </h2>
        </div>
        <form className="space-y-6">
          <div>
            <label htmlFor="name" className="sr-only">
              Name
            </label>
            <input
              id="name"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              className="relative block w-full px-3 border-0 py-2 text-gray-900 ring-1 ring-inset ring-gray-100 placeholder:text-gray-400 focus:z-10 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              placeholder="Name (optional)"
            />
          </div>

          <div>
            <label htmlFor="phoneNumber" className="sr-only">
              Phone Number
            </label>
            <input
              id="phoneNumber"
              name="phoneNumber"
              value={formData.phoneNumber}
              onChange={handleInputChange}
              className="relative block w-full px-3 border-0 py-2 text-gray-900 ring-1 ring-inset ring-gray-100 placeholder:text-gray-400 focus:z-10 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              placeholder="Phone Number (optional)"
            />
          </div>

          <div>
            <label htmlFor="email" className="sr-only">
              Email address
            </label>
            <input
              id="email"
              name="email"
              type="email"
              autoComplete="email"
              value={formData.email}
              required
              onChange={handleInputChange}
              className="relative block w-full px-3 border-0 py-2 text-gray-900 ring-1 ring-inset ring-gray-100 placeholder:text-gray-400 focus:z-10 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              placeholder="Email address*"
            />
          </div>

          <div>
            <label htmlFor="password" className="sr-only">
              Password
            </label>
            <input
              id="password"
              name="password"
              type="password"
              value={formData.password}
              required
              onChange={handleInputChange}
              className="relative block w-full px-3 border-0 py-2 text-gray-900 ring-1 ring-inset ring-gray-100 placeholder:text-gray-400 focus:z-10 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              placeholder="Password*"
            />
          </div>

          <div className="text-center">
            <p className="text-sm text-red-500">{errorMsg}</p>
          </div>

          <div>
            <button
              type="submit"
              onClick={onRegister}
              className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold leading-6 text-white hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
              Register
            </button>
          </div>
        </form>

        <p className="mt-10 text-center text-sm text-gray-500">
          Already have an account?{' '}
          <Link
            to={GeneralRouteNames.LOGIN}
            className="font-semibold leading-6 text-indigo-600 hover:text-indigo-500"
          >
            Click here to log in
          </Link>
        </p>
      </div>
    </div>
  );
};

export default SignUpPage;
