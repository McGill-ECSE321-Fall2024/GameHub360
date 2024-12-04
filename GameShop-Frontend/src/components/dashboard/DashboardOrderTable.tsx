import React, { useEffect, useState } from 'react';
import { getOrders } from '../../api/orderService';
import { CustomerOrder } from '../../model/CustomerOrder';

type TransformedOrder = {
  orderId: number;
  orderDate: string;
  orderedGameIds: string;
  customerId: string;
  orderStatus: string;
};

const DashboardOrderTable = () => {
  const [orders, setOrders] = useState<TransformedOrder[]>([]);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const apiOrders = await getOrders();
        const transformedOrders = apiOrders.map((order: CustomerOrder) => ({
          orderId: order.orderId,
          orderDate: new Date(order.orderDate).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
          }),
          orderedGameIds: order.orderedGamesIds.join(', '),
          customerId: order.orderedById.toString(),
          orderStatus: order.orderStatus,
        }));
        setOrders(transformedOrders);
      } catch (error) {
        console.error('Error fetching orders:', (error as Error).message);
      }
    };

    fetchOrders();
  }, []);

  return (
    <div className="px-4 sm:px-6 lg:px-8">
      <div className="mt-4 flow-root">
        <div className="-mx-4 -my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
          <div className="inline-block min-w-full py-2 align-middle sm:px-6 lg:px-8">
            <div className="overflow-hidden shadow ring-1 ring-black/5 sm:rounded-lg">
              <table className="min-w-full divide-y divide-gray-300">
                <thead className="bg-gray-50">
                  <tr>
                    <th
                      scope="col"
                      className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-6"
                    >
                      Order Date
                    </th>
                    <th
                      scope="col"
                      className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900"
                    >
                      Ordered Game IDs
                    </th>
                    <th
                      scope="col"
                      className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900"
                    >
                      Customer ID
                    </th>
                    <th
                      scope="col"
                      className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900"
                    >
                      Order Status
                    </th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200 bg-white">
                  {orders.length > 0 ? (
                    orders.map((order) => (
                      <tr key={order.orderId}>
                        <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-6">
                          {order.orderDate}
                        </td>
                        <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                          {order.orderedGameIds}
                        </td>
                        <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                          {order.customerId}
                        </td>
                        <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                          {order.orderStatus}
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td
                        colSpan={4}
                        className="whitespace-nowrap py-4 text-center text-sm text-gray-500"
                      >
                        No orders found.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardOrderTable;
