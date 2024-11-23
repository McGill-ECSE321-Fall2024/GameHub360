import React, { useEffect } from 'react';

interface ToastProps {
  message: string;
  type: 'success' | 'error';
  onClose: () => void;
}

const Toast: React.FC<ToastProps> = ({ message, type, onClose }) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, 3000);

    return () => clearTimeout(timer);
  }, [onClose]);

  const bgColor = type === 'success' ? 'bg-green-500' : 'bg-red-500';

  return (
    <div className="fixed bottom-4 left-0 right-0 flex justify-center pointer-events-none z-50">
      <div className={`${bgColor} text-white px-6 py-3 rounded-lg shadow-lg animate-fade-in-up`}>
        {message}
      </div>
    </div>
  );
};

export default Toast;