import { ParkingSlot } from "./ParkingSlot";
import { ParkingSlotData } from "../types.ts";
import "../styles/ParkingGrid.css";

const AISLES_COUNT = 5;
const SLOTS_PER_AISLE = 20;
const SLOTS_PER_SIDE = SLOTS_PER_AISLE / 2;

interface ParkingGridProps {
  slots: ParkingSlotData[]; 
  highlightedSlotId?: string;
  zoomLevel: number;
}

interface AisleProps {
  aisleSlots: ParkingSlotData[];
  highlightedSlotId?: string;
}

//aisle: 1-2 columns of parking spaces
//lane: 주행도로 / 빈 공간

function groupIntoAisles(slots: ParkingSlotData[]): ParkingSlotData[][] {
  return Array.from({ length: AISLES_COUNT }, (_, i) => 
  slots.slice(i * SLOTS_PER_AISLE, (i+1) * SLOTS_PER_AISLE));
}

function SlotColumn({ slots, orientation, highlightedSlotId,
}: {
  slots: ParkingSlotData[];
  orientation: "left" | "right";
  highlightedSlotId?: string;
}) {
  return (
    <div className="parking-grid__column">
      {slots.map((slot) => (
        <ParkingSlot 
          key={slot.slotId}
          slotNumber={slot.slotCode}
          isActive={slot.isActive}
          zone={String(slot.zoneId)}
          slotType={slot.slotType}
          licensePlate={slot.licensePlate}
          orientation={orientation}
          isHighlighted={slot.slotCode === highlightedSlotId}/>
      ))}
    </div>
  )
}

function Aisle({ aisleSlots, highlightedSlotId }: AisleProps) {
  const topRow = aisleSlots.slice(0, SLOTS_PER_SIDE);
  const bottomRow = aisleSlots.slice(SLOTS_PER_SIDE);

  return (
    <div className="parking-grid__aisle">
      <SlotColumn slots={topRow} orientation="left" highlightedSlotId={highlightedSlotId} />
      <div className="parking-grid__lane" aria-hidden="true">
        <div className="parking-grid__lane--right" aria-hidden="true" />
      </div>
      <SlotColumn slots={bottomRow} orientation="right" highlightedSlotId={highlightedSlotId} />
    </div>
  );
}

export function ParkingGrid({ slots, highlightedSlotId, zoomLevel }: ParkingGridProps) {
  const aisles = groupIntoAisles(slots);

  return (
    <div className="parking-grid__scaler">
      <div
        style={{
          width:  `calc(var(--grid-w)  * ${zoomLevel})`,
          height: `calc(var(--grid-h) * ${zoomLevel})`,
          position: "relative",
          transition: "width 0.2s ease, height 0.2s ease",
        }}
      >
        <div
          className="parking-grid"
          style={{ transform: `scale(${zoomLevel})`, transformOrigin: "top left"}}
        >
          {aisles.map((aisleSlots, i) => (
            <Aisle key={i} aisleSlots={aisleSlots} highlightedSlotId={highlightedSlotId} />
          ))}
        </div>
      </div>
    </div>
  );
}