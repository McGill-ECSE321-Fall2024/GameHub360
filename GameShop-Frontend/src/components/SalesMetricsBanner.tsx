import React, { useEffect, useState } from 'react';
import { getSalesMetrics } from '../api/storeInfoService';

const SalesMetricsBanner = () => {
  const [stats, setStats] = useState([
    { name: 'Orders', value: 'Loading...' },
    { name: 'Customers', value: 'Loading...' },
    { name: 'Games Sold', value: 'Loading...' },
    { name: 'Sales', value: 'Loading...' },
  ]);

  useEffect(() => {
    const fetchSalesMetrics = async () => {
      try {
        const salesMetrics = await getSalesMetrics();
        setStats([
          {
            name: 'Orders',
            value: salesMetrics.totalOrders.toLocaleString(),
          },
          {
            name: 'Customers',
            value: salesMetrics.totalCustomers.toLocaleString(),
          },
          {
            name: 'Games Sold',
            value: salesMetrics.totalGamesSold.toLocaleString(),
          },
          {
            name: 'Sales',
            value: `$${salesMetrics.totalSales.toFixed(2)}`,
          },
        ]);
      } catch (error) {
        if (error instanceof Error) {
          console.error('Error fetching sales metrics:', error.message);
        } else {
          console.error('Error fetching sales metrics:', error);
        }
        setStats([
          { name: 'Orders', value: 'Error' },
          { name: 'Customers', value: 'Error' },
          { name: 'Games Sold', value: 'Error' },
          { name: 'Total Sales', value: 'Error' },
        ]);
      }
    };

    fetchSalesMetrics();
  }, []);

  return (
    <dl className="mx-auto grid grid-cols-4 gap-2 sm:gap-4 ">
      {stats.map((stat) => (
        <div
          key={stat.name}
          className="flex flex-col items-start justify-center bg-white px-4 sm:px-6 xl:px-8"
        >
          <dt className="text-sm font-medium text-gray-500">{stat.name}</dt>
          <dd className="text-2xl font-semibold tracking-tight text-gray-900">
            {stat.value}
          </dd>
        </div>
      ))}
    </dl>
  );
};

export default SalesMetricsBanner;
