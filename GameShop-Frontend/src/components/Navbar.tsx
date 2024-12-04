import { Fragment } from 'react';
import { Disclosure, Menu, Transition } from '@headlessui/react';
import { UserCircleIcon } from '@heroicons/react/24/outline';
import { Link } from 'react-router-dom';
import { GeneralRouteNames } from '../model/routeNames/GeneralRouteNames';
import { UserType } from '../model/user/UserType';
import { EmployeeRouteNames } from '../model/routeNames/EmployeeRouteNames';
import { ManagerRouteNames } from '../model/routeNames/ManagerRouteNames';
import { CustomerRouteNames } from '../model/routeNames/CustomerRouteNames';
import { clearAuthUser } from '../state/authState';

function classNames(...classes: string[]) {
  return classes.filter(Boolean).join(' ');
}

interface NavbarProps {
  userType: UserType | null;
}

type LinkPair = {
  name: string;
  to: string;
};

const Navbar = ({ userType }: NavbarProps) => {
  // Define different links based on the user type
  const links: LinkPair[] = (() => {
    switch (userType) {
      case UserType.CUSTOMER:
        return [
          { name: 'Browse', to: GeneralRouteNames.BROWSE },
          { name: 'Orders', to: CustomerRouteNames.ORDERS },
          { name: 'Wishlist', to: CustomerRouteNames.WISHLIST },
          { name: 'Cart', to: CustomerRouteNames.CART },
          // Add more links as needed (these are just examples)
        ];
      case UserType.EMPLOYEE:
        return [
          { name: 'Browse', to: GeneralRouteNames.BROWSE },
          { name: 'Game Requests', to: EmployeeRouteNames.GAME_REQUESTS },
          { name: 'Categories', to: EmployeeRouteNames.CATEGORIES },
          // Add more links as needed (these are just examples)
        ];
      case UserType.MANAGER:
        return [
          { name: 'Browse', to: GeneralRouteNames.BROWSE },
          { name: 'Dashboard', to: ManagerRouteNames.DASHBOARD },
          { name: 'Game Requests', to: ManagerRouteNames.GAME_REQUESTS },
          { name: 'Games', to: ManagerRouteNames.GAMES },
          { name: 'Categories', to: ManagerRouteNames.CATEGORIES },
          // Add more links as needed (these are just examples)
        ];
      default:
        return [
          { name: 'Browse', to: GeneralRouteNames.BROWSE },
          { name: 'Login', to: GeneralRouteNames.LOGIN },
          { name: 'Signup', to: GeneralRouteNames.SIGNUP },
        ];
    }
  })();

  return (
    <Disclosure as="nav" className="bg-gray-800">
      {() => (
        <>
          <div className="mx-auto max-w-7xl px-2 sm:px-4 lg:px-8">
            <div className="relative flex h-16 items-center justify-between">
              <div className="flex items-center px-2 lg:px-0">
                <Link to={GeneralRouteNames.BASE} className="flex-shrink-0">
                  <h1 className="text-white text-2xl font-bold italic">GS</h1>
                </Link>
                <div className="ml-10 lg:ml-10 lg:block">
                  <div className="flex space-x-2">
                    {' '}
                    {/* Reduced spacing */}
                    {links.map((link) => (
                      <Link
                        key={link.name}
                        to={link.to}
                        className="text-white underline hover:bg-gray-700 px-3 py-2 rounded-md text-sm font-medium"
                      >
                        {link.name}
                      </Link>
                    ))}
                  </div>
                </div>
              </div>

              {userType && (
                <div className="lg:ml-4 lg:block">
                  <div className="flex items-center">
                    <Menu as="div" className="relative ml-4 flex-shrink-0">
                      <div className="flex items-center">
                        <span className="mr-2 text-gray-300 text-xs">
                          {userType}
                        </span>
                        {/* Will need to add a cart icon here IF IS CUSTOMER */}
                        <Menu.Button className="relative flex rounded-full bg-gray-800 text-sm text-white focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800">
                          <span className="absolute -inset-1.5" />
                          <span className="sr-only">Open user menu</span>
                          <UserCircleIcon
                            className="h-8 w-8 rounded-full"
                            aria-hidden="true"
                          />
                        </Menu.Button>
                      </div>
                      <Transition
                        as={Fragment}
                        enter="transition ease-out duration-100"
                        enterFrom="transform opacity-0 scale-95"
                        enterTo="transform opacity-100 scale-100"
                        leave="transition ease-in duration-75"
                        leaveFrom="transform opacity-100 scale-100"
                        leaveTo="transform opacity-0 scale-95"
                      >
                        <Menu.Items className="absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
                          <Menu.Item>
                            {({ active }) => (
                              <Link
                                to={GeneralRouteNames.PROFILE}
                                className={classNames(
                                  active ? 'bg-gray-100' : '',
                                  'block px-4 py-2 text-sm text-gray-700'
                                )}
                              >
                                Your Profile
                              </Link>
                            )}
                          </Menu.Item>
                          <Menu.Item>
                            {({ active }) => (
                              <button
                                onClick={() => {
                                  clearAuthUser(); // Call the sign-out function
                                  window.location.href = GeneralRouteNames.BASE; // Redirect to the base route
                                  console.log('User signed out');
                                }}
                                className={classNames(
                                  active ? 'bg-gray-100' : '',
                                  'block w-full text-left px-4 py-2 text-sm text-gray-700'
                                )}
                              >
                                Sign out
                              </button>
                            )}
                          </Menu.Item>
                        </Menu.Items>
                      </Transition>
                    </Menu>
                  </div>
                </div>
              )}
            </div>
          </div>
        </>
      )}
    </Disclosure>
  );
};

export default Navbar;
