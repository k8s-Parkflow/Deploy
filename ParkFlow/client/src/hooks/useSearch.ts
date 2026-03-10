/*
    parked car location search
 */

import { useState, useCallback } from "react";
import type { ParkingSlotData } from "../types.ts";

const PLATE_REGEX = /^\d{2,3}[가-힣]\s?\d{4}$/;
export type SearchError = "invalid_format" | "not_found" | null;

export interface UseSearchReturn {
  searchQuery: string;
  setSearchQuery: (q: string) => void;
  isValidPlate: boolean;           // live format check!
  searchError: SearchError;
  highlightedSlotId: string;
  handleSearch: (allSlots: ParkingSlotData[], onFound: (zoneId: number) => void) => void;
}

function normalisePlate(p: string): string {
  return p.trim().replace(/\s+/g, " ");
}

export function useSearch(): UseSearchReturn {
  const [searchQuery, setSearchQueryBase] = useState("");
  const [searchError, setSearchError] = useState<SearchError>(null);
  const [highlightedSlotId, setHighlightedSlotId] = useState("");
  
  const isValidPlate = PLATE_REGEX.test(searchQuery.trim());
  
  const setSearchQuery = useCallback((q: string) => {
    setSearchQueryBase(q);
    setSearchError(null);
    setHighlightedSlotId("");
  }, []);

  const handleSearch = useCallback(
    (allSlots: ParkingSlotData[], onFound: (zoneId: number) => void) => {

      if (!isValidPlate) {
        setSearchError("invalid_format");
        return;
      }

      const normQ = normalisePlate(searchQuery);
      const found = allSlots.find(
        (s) => s.licensePlate && normalisePlate(s.licensePlate) === normQ
      );

      if (!found) {
        setSearchError("not_found");
        return;
      }

      setSearchError(null);
      onFound(found.zoneId);
      setHighlightedSlotId(found.slotCode);
    },
    [searchQuery]
  );

  // const clearSearch = useCallback(() => {
  //   setSearchQueryBase("");
  //   setSearchError(null);
  //   setHighlightedSlotId("");
  // }, []);

  return { searchQuery, setSearchQuery, isValidPlate, searchError, highlightedSlotId, handleSearch };
}