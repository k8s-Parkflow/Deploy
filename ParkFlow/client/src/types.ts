export interface Zone {
  zone_id: number;
  zone_name: string;
}

//Parking slot default
export interface BaseSlot {
  slot_id: number;
  zone_id: number;
  slot_type_id: number;
  slot_code: string;
  is_active: boolean;
}

export interface SlotOccupancy {
  slot_id: number;
  occupied: boolean;
  vehicle_plate?: string;
}

// Updated Slot interface used throughout the front
export type SlotType = "standard" | "EV" | "handicapped";

export interface ParkingSlotData {
  slotId: number;
  slotCode: string;
  zoneId: number;
  slotType: SlotType;
  isActive: boolean;
  licensePlate?: string;
}

export interface ZoneAvailabilityResponse {
  zone_id: number;
  total_slots: number;
  occupied_slots: number;
  available_slots: number;
  general_total: number;
  general_occupied: number;
  general_available: number;
  ev_total: number;
  ev_occupied: number;
  ev_available: number;
  disabled_total: number;
  disabled_occupied: number;
  disabled_available: number;
}

export interface TypeAvailabilityResponse {
  zone_id: number;
  type: string;
  total: number;
  occupied: number;
  available: number;
}

