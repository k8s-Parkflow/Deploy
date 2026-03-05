//generate mock data and calculate stats

import { 
  BaseSlot, SlotOccupancy, SlotType, ParkingSlotData, 
  Zone, ZoneAvailabilityResponse } from "../types.ts";

export const TOTAL_ZONES = 100;
export const SLOTS_PER_ZONE = 100;

export const SLOT_TYPE_MAP: Record<number, SlotType> = {
  0: "standard",
  1: "EV",
  2: "handicapped",
};

// Random car plate generator
const KR_MID_CHARS = [
  "가", "나", "다", "라", "마",
  "거", "너", "더", "러", "머", "버", "서", "어", "저",
  "고", "노", "도", "로", "모", "보", "소", "오", "조",
  "구", "누", "두", "루", "무", "부", "수", "우", "주",
];

export function generateKPlate(): string {
  const num1 = String(Math.floor(Math.random() * 800)).padStart(2, '0');
  const mid  = KR_MID_CHARS[Math.floor(Math.random() * KR_MID_CHARS.length)];
  const num2 = String(Math.floor(Math.random() * 9000) + 1000)
  return `${num1}${mid} ${num2}`;
}

// random 입출차 generator until backend integration

export function generateMockData(): {
  zones: Zone[];
  baseSlots: BaseSlot[];
  occupancies: SlotOccupancy[];
} {
  const zones: Zone[] = Array.from({ length: TOTAL_ZONES }, (_, i) => ({
    zone_id: i + 1,
    zone_name: `Zone ${i + 1}`,
  }));

  const baseSlots: BaseSlot[] = [];
  const occupancies: SlotOccupancy[] = [];

  zones.forEach(({ zone_id }) => {
    for (let i = 1; i <= SLOTS_PER_ZONE; i++) {
      const slot_id = (zone_id - 1) * SLOTS_PER_ZONE + i;

      // first 5 slots → EV, every 20 → EV
      const slot_type_id = i <= 5 ? 1 : i % 20 === 0 ? 2 : 0;
      const slot_code = `Z${zone_id}-${String(i).padStart(3, "0")}`;

      baseSlots.push({ slot_id, zone_id, slot_type_id, slot_code, is_active: true });

      const occupied = Math.random() < 0.45;
      occupancies.push({
        slot_id,
        occupied,
        vehicle_plate: occupied ? generateKPlate() : undefined,
      });
    }
  });

  return { zones, baseSlots, occupancies };
}

// Mapping from DB

export function mapToParkingSlotData(
  BaseSlots: BaseSlot[],
  occupancies: SlotOccupancy[]
): ParkingSlotData[] {
  const occMap = new Map(occupancies.map((o) => [o.slot_id, o]));

  return BaseSlots.map((slot) => {
    const occ = occMap.get(slot.slot_id);
    return {
      slotId: slot.slot_id,
      slotCode: slot.slot_code,
      zoneId: slot.zone_id,
      slotType: SLOT_TYPE_MAP[slot.slot_type_id] ?? "standard",
      isActive: occ?.occupied ?? false,
      licensePlate: occ?.vehicle_plate,
    };
  });
}

// Zone별 availability 

export function calcZoneAvailability(slots: ParkingSlotData[]): ZoneAvailabilityResponse {
  const general  = slots.filter((s) => s.slotType === "standard");
  const ev       = slots.filter((s) => s.slotType === "EV");
  const disabled = slots.filter((s) => s.slotType === "handicapped");

  return {
    zone_id:           slots[0]?.zoneId ?? 0,
    total_slots:       slots.length,
    occupied_slots:    slots.filter((s) =>  s.isActive).length,
    available_slots:   slots.filter((s) => !s.isActive).length,
    general_total:      general.length,
    general_occupied:   general.filter((s) =>  s.isActive).length,
    general_available:  general.filter((s) => !s.isActive).length,
    ev_total:          ev.length,
    ev_occupied:       ev.filter((s) =>  s.isActive).length,
    ev_available:      ev.filter((s) => !s.isActive).length,
    disabled_total:    disabled.length,
    disabled_occupied: disabled.filter((s) =>  s.isActive).length,
    disabled_available:disabled.filter((s) => !s.isActive).length,
  };
}

// 합산 stat

export interface GlobalStats {
  totalSlots: number;
  totalAvailable: number;
  generalTotal: number;
  generalAvailable: number;
  evTotal: number;
  evAvailable: number;
  disabledTotal: number;
  disabledAvailable: number;
}

export function calcGlobalStats(allSlots: ParkingSlotData[]): GlobalStats {
  const general  = allSlots.filter((s) => s.slotType === "standard");
  const ev       = allSlots.filter((s) => s.slotType === "EV");
  const disabled = allSlots.filter((s) => s.slotType === "handicapped");

  return {
    totalSlots:        allSlots.length,
    totalAvailable:    allSlots.filter((s) => !s.isActive).length,
    generalTotal:      general.length,
    generalAvailable:  general.filter((s) => !s.isActive).length,
    evTotal:           ev.length,
    evAvailable:       ev.filter((s) => !s.isActive).length,
    disabledTotal:     disabled.length,
    disabledAvailable: disabled.filter((s) => !s.isActive).length,
  };
}