export interface GameRequest {
  id: number;
  name: string;
  description: string;
  imageUrl: string;
  requestDate: string; // ISO string
  requestStatus: 'SUBMITTED' | 'APPROVED' | 'REFUSED'; // Enum values
  staffId: number;
  noteIds: number[];
  categoryIds: number[];
  price?: number; // Nullable, only present if APPROVED
  quantityInStock?: number; // Nullable, only present if APPROVED
  rejectionReason?: string; // Nullable, only present if REFUSED
}

// Interface for a note on a game request
export interface RequestNote {
  noteId: number;
  content: string;
  noteDate: string; // ISO string
  staffWriterId: number;
  gameRequestId: number;
}
