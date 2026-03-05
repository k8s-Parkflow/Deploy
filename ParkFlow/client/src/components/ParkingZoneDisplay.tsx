import { ZoomIn, ZoomOut, Maximize2 } from "lucide-react";
import { ParkingGrid } from "./ParkingGrid";
import type { Zone, ZoneAvailabilityResponse, ParkingSlotData } from "../types.ts";
import "../styles/ParkingZoneDisplay.css";

interface ParkingZoneDisplayProps {
  zone: Zone;
  zoneStats: ZoneAvailabilityResponse;
  zoneSlots: ParkingSlotData[];
  highlightedSlotId: string;
  zoomLevel: number;
  onZoomIn: () => void;
  onZoomOut: () => void;
  onZoomReset: () => void;
}

const MIN_ZOOM = 0.5;
const MAX_ZOOM = 2.0;

export function ParkingZoneDisplay({
  zone,
  zoneStats,
  zoneSlots,
  highlightedSlotId,
  zoomLevel,
  onZoomIn,
  onZoomOut,
  onZoomReset,
}: ParkingZoneDisplayProps) {
  return (
    <section className="zone-display">
      <div className="zone-display__header">
        <div className="zone-display__group">
          <span className="zone-badge">{zone.zone_id}</span>
          <div>
            <h2 className="zone-display__title">{zone.zone_name}</h2>
            <p className="zone-display__sub">
              {zoneStats.available_slots} 여유 / {zoneStats.total_slots} 전체
              &nbsp;·&nbsp; 일반 {zoneStats.general_available}/{zoneStats.general_total}
              &nbsp;·&nbsp; EV {zoneStats.ev_available}/{zoneStats.ev_total}
              &nbsp;·&nbsp; 장애인 {zoneStats.disabled_available}/{zoneStats.disabled_total}
            </p>
          </div>
        </div>

        <div className="zone-display__controls">
          <div className="zoom-controls">
            <button className="zoom-btn" onClick={onZoomOut}  disabled={zoomLevel <= MIN_ZOOM} aria-label="축소">
              <ZoomOut size={16} />
            </button>
            <button className="zoom-btn" onClick={onZoomReset} aria-label="원래 크기">
              <Maximize2 size={16} />
            </button>
            <button className="zoom-btn" onClick={onZoomIn}   disabled={zoomLevel >= MAX_ZOOM} aria-label="확대">
              <ZoomIn size={16} />
            </button>
            <span className="zoom-label">{Math.round(zoomLevel * 100)}%</span>
          </div>
        </div>
      </div>

      <div
        className="zone-display__grid"
        style={{ height: `calc(var(--grid-natural-height) * ${zoomLevel})` }}
      >
        <ParkingGrid
          slots={zoneSlots}
          highlightedSlotId={highlightedSlotId}
          zoomLevel={zoomLevel}
        />
      </div>
    </section>
  );
}