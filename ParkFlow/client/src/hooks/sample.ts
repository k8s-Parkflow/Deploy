/*
  manages parking state with real backend calls
  - polls GET /zones/availability every 5s for global stats
  - polls GET /zones/{id}/availability for the selected zone
  - TODO: replace setInterval with SSE/WebSocket once backend supports it
*/

import { useState, useEffect, useMemo, useCallback } from "react";
import { calcGlobalStats, type GlobalStats } from "../utils/parkingUtils";
import type { Zone } from "../types.ts";
import type { ParkingSlotData, ZoneAvailabilityResponse } from "../types.ts";

// ── adjust if backend runs elsewhere ─────────────────────────────────────────
const API_BASE = import.meta.env.VITE_API_URL ?? "http://localhost:8080";
// ─────────────────────────────────────────────────────────────────────────────

const REFRESH_INTERVAL_MS = 5_000;

// matches backend ZoneAvailabilityResponse
interface BackendZoneAvailability {
  zoneId:         number;
  zoneName:       string;
  totalSlots:     number;
  occupiedSlots:  number;
  availableSlots: number;
  general:        number;   // available general count
  ev:             number;   // available EV count
  disabled:       number;   // available disabled count
}

// matches backend TypeAvailabilityResponse
interface BackendTypeAvailability {
  zoneId:   number;
  category: "GENERAL" | "EV" | "DISABLED";
  total:    number;
  occupied: number;
  available: number;
}

interface GetParkingDataReturn {
  zones:            Zone[];
  allSlots:         ParkingSlotData[];      // kept for grid/map components that need slot-level data
  zoneSlots:        ParkingSlotData[];
  zoneStats:        ZoneAvailabilityResponse;
  globalStats:      GlobalStats;
  lastUpdated:      Date;
  autoRefresh:      boolean;
  toggleAutoRefresh: () => void;
  selectedZoneId:   number;
  setSelectedZoneId: (id: number) => void;
  loading:          boolean;
  error:            string | null;
}

// unwrap the { data: T } envelope the backend always returns
async function apiFetch<T>(path: string): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`);
  if (!res.ok) throw new Error(`${res.status} ${res.statusText} — ${path}`);
  const json = await res.json();
  return json.data as T;
}

export function getParkingData(): GetParkingDataReturn {
  const [allZoneStats,   setAllZoneStats]   = useState<BackendZoneAvailability[]>([]);
  const [selectedZoneStat, setSelectedZoneStat] = useState<BackendZoneAvailability | null>(null);

  const [selectedZoneId, setSelectedZoneId] = useState(1);
  const [lastUpdated,    setLastUpdated]    = useState(new Date());
  const [autoRefresh,    setAutoRefresh]    = useState(true);
  const [loading,        setLoading]        = useState(true);
  const [error,          setError]          = useState<string | null>(null);

  // ── fetch all zones (for global stats + zone list) ────────────────────────
  const fetchAllZones = useCallback(async () => {
    const data = await apiFetch<BackendZoneAvailability[]>("/zones/availability");
    setAllZoneStats(data);
  }, []);

  // ── fetch selected zone detail ────────────────────────────────────────────
  const fetchSelectedZone = useCallback(async () => {
    const data = await apiFetch<BackendZoneAvailability>(`/zones/${selectedZoneId}/availability`);
    setSelectedZoneStat(data);
  }, [selectedZoneId]);

  // ── combined refresh ──────────────────────────────────────────────────────
  const fetchAll = useCallback(async () => {
    try {
      await Promise.all([fetchAllZones(), fetchSelectedZone()]);
      setLastUpdated(new Date());
      setError(null);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Fetch error");
    } finally {
      setLoading(false);
    }
  }, [fetchAllZones, fetchSelectedZone]);

  // initial load + re-fetch when selected zone changes
  useEffect(() => { fetchAll(); }, [fetchAll]);

  // TODO: replace this block with WebSocket/SSE once backend exposes it:
  //   const ws = new WebSocket(`${API_BASE.replace(/^http/, "ws")}/ws/parking`);
  //   ws.onmessage = (e) => { const d = JSON.parse(e.data); setAllZoneStats(d); };
  //   return () => ws.close();
  useEffect(() => {
    if (!autoRefresh) return;
    const id = setInterval(fetchAll, REFRESH_INTERVAL_MS);
    return () => clearInterval(id);
  }, [autoRefresh, fetchAll]);

  // ── adapt backend shape → frontend types ─────────────────────────────────

  // Zone[] for the sidebar/tab list
  const zones = useMemo<Zone[]>(
    () => allZoneStats.map((z) => ({ id: z.zoneId, name: z.zoneName })),
    [allZoneStats]
  );

  // ZoneAvailabilityResponse for the selected zone panel
  const zoneStats = useMemo<ZoneAvailabilityResponse>(() => {
    const z = selectedZoneStat;
    if (!z) return { zoneId: selectedZoneId, zoneName: "", totalSlots: 0, occupiedSlots: 0, availableSlots: 0, general: 0, ev: 0, disabled: 0 };
    return {
      zoneId:         z.zoneId,
      zoneName:       z.zoneName,
      totalSlots:     z.totalSlots,
      occupiedSlots:  z.occupiedSlots,
      availableSlots: z.availableSlots,
      general:        z.general,
      ev:             z.ev,
      disabled:       z.disabled,
    };
  }, [selectedZoneStat, selectedZoneId]);

  // GlobalStats derived from all zones
  const globalStats = useMemo<GlobalStats>(() => {
    const total    = allZoneStats.reduce((s, z) => s + z.totalSlots, 0);
    const occupied = allZoneStats.reduce((s, z) => s + z.occupiedSlots, 0);
    return {
      totalSlots:     total,
      occupiedSlots:  occupied,
      availableSlots: total - occupied,
      occupancyRate:  total > 0 ? Math.round((occupied / total) * 100) : 0,
    };
  }, [allZoneStats]);

  // allSlots / zoneSlots: only needed if a slot-grid component renders individual bays.
  // The backend doesn't expose per-slot data yet — keep as empty until that endpoint exists.
  // Once GET /zones/{id}/slots exists, fetch it here and replace these.
  const allSlots:  ParkingSlotData[] = [];
  const zoneSlots: ParkingSlotData[] = [];

  return {
    zones,
    allSlots,
    zoneSlots,
    zoneStats,
    globalStats,
    lastUpdated,
    autoRefresh,
    toggleAutoRefresh: () => setAutoRefresh((v) => !v),
    selectedZoneId,
    setSelectedZoneId,
    loading,
    error,
  };
}