/*
    manages parking state and simulates real-time refresh loop
    must replace generateMockData() / the interval with real calls for integration w/ backend later
 */

import { useState, useEffect, useMemo } from "react";
import {
  generateMockData,
  generateKPlate,
  mapToParkingSlotData,
  calcZoneAvailability,
  calcGlobalStats,
  type GlobalStats,
} from "../utils/parkingUtils";

import type { Zone } from "../types.ts";
import type { ParkingSlotData, ZoneAvailabilityResponse } from "../types.ts";

const REFRESH_INTERVAL_MS = 5_000;
const EVENTS_PER_TICK = () => Math.floor(Math.random() * 4) + 2;

interface GetParkingDataReturn {
  zones: Zone[];
  allSlots: ParkingSlotData[];
  zoneSlots: ParkingSlotData[];
  zoneStats: ZoneAvailabilityResponse;
  globalStats: GlobalStats;
  lastUpdated: Date;
  autoRefresh: boolean;
  toggleAutoRefresh: () => void;
  selectedZoneId: number;
  setSelectedZoneId: (id: number) => void;
}

export function getParkingData(): GetParkingDataReturn {
  const [db, setDb] = useState(generateMockData);
  const [selectedZoneId, setSelectedZoneId] = useState(1);
  const [lastUpdated, setLastUpdated] = useState(new Date);
  const [autoRefresh, setAutoRefresh] = useState(true);

  // real-time updates
  // 할일: replace with WebSocket / SSE subscription 
  useEffect(() => {
    if (!autoRefresh) return;

    const interval = setInterval(() => {
      setDb((prev) => {
        const newOccupancies = [...prev.occupancies];
        const count = EVENTS_PER_TICK();

        for (let i = 0; i < count; i++) {
          const idx = Math.floor(Math.random() * newOccupancies.length);
          const cur = newOccupancies[idx];
          newOccupancies[idx] = cur.occupied
            ? { ...cur, occupied: false, vehicle_plate: undefined }
            : { ...cur, occupied: true,  vehicle_plate: generateKPlate() };
        }

        return { ...prev, occupancies: newOccupancies };
      });
      setLastUpdated(new Date());
    }, REFRESH_INTERVAL_MS);

    return () => clearInterval(interval);
  }, [autoRefresh]);

  const allSlots = useMemo(
    () => mapToParkingSlotData(db.baseSlots, db.occupancies),
    [db.baseSlots, db.occupancies]
  );

  const zoneSlots = useMemo(
    () => allSlots.filter((s) => s.zoneId === selectedZoneId),
    [allSlots, selectedZoneId]
  );

  const zoneStats  = useMemo(() => calcZoneAvailability(zoneSlots), [zoneSlots]);
  const globalStats = useMemo(() => calcGlobalStats(allSlots), [allSlots]);

  return {
    zones: db.zones,
    allSlots,
    zoneSlots,
    zoneStats,
    globalStats,
    lastUpdated,
    autoRefresh,
    toggleAutoRefresh: () => setAutoRefresh((v) => !v),
    selectedZoneId,
    setSelectedZoneId,
  };
}