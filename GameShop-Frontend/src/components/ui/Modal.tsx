import React from "react";

interface ModalProps {
  isOpen: boolean; // Whether the modal is visible
  onClose: () => void; // Function to close the modal
  children: React.ReactNode; // Content to display inside the modal
}

const Modal: React.FC<ModalProps> = ({ isOpen, onClose, children }) => {
  if (!isOpen) return null; // Do not render the modal if not open

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
      <div className="bg-white p-6 rounded shadow-lg max-w-md w-full">
        {children}
        <button
          className="mt-4 w-full bg-blue-500 text-white p-2 rounded"
          onClick={onClose}
        >
          Close
        </button>
      </div>
    </div>
  );
};

export default Modal;
