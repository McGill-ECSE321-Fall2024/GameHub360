export interface CustomerProfile {
    customerId: number;
    email: string;
    name?: string;
    phoneNumber?: string;
  }

export interface AccountInfoProps {
    email: string;
    name: string;
    phoneNumber: string;
    onSave: (data: { name: string; phoneNumber: string; password?: string }) => Promise<void>; // Include `id` in onSave
  }