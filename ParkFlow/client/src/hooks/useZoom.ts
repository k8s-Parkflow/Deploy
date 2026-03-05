// zoom in / out of zones 

import { useState } from "react";

const MIN_ZOOM = 0.5;
const MAX_ZOOM = 2.0;
const STEP     = 0.2;

interface UseZoomReturn {
  zoomLevel: number;
  zoomIn: () => void;
  zoomOut: () => void;
  zoomReset: () => void;
}

export function useZoom(initial = 1): UseZoomReturn {
  const [zoomLevel, setZoomLevel] = useState(initial);

  return {
    zoomLevel,
    zoomIn: () => setZoomLevel((z) => Math.min(+(z + STEP).toFixed(1), MAX_ZOOM)),
    zoomOut: () => setZoomLevel((z) => Math.max(+(z - STEP).toFixed(1), MIN_ZOOM)),
    zoomReset: () => setZoomLevel(1),
  };
}