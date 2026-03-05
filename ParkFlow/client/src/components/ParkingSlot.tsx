import { Car, Zap, Accessibility } from "lucide-react";
import type { SlotType } from "../types.ts";
import "../styles/ParkingSlot.css";

interface ParkingSlotProps {
  slotNumber: string;
  isActive: boolean;
  zone: string;
  slotType?: SlotType;
  licensePlate?: string;
  isHighlighted?: boolean;
  orientation?: "left" | "right";
}

function getSlotClassNames( isActive: boolean, slotType: SlotType, isHighlighted: boolean ): string {
  const classes = ["parking-slot"];
  if (slotType === "handicapped") classes.push("parking-slot--handicapped");
  if (slotType === "EV") classes.push("parking-slot--ev");
  if (isHighlighted) classes.push("parking-slot--highlighted");
  else if (isActive) classes.push("parking-slot--occupied");
  else classes.push("parking-slot--available");
  return classes.join(" ");
}

function SlotBadge({ slotType }: { slotType: SlotType }) {
  if (slotType === "standard") return null;

  return (
    <div className="parking-slot__badge">
      {slotType === "EV" && <Zap className="parking-slot__badge-icon parking-slot__badge-icon--ev" size={12} />}
      {slotType === "handicapped" && (
        <Accessibility className="parking-slot__badge-icon parking-slot__badge-icon--handicapped" size={12} />
      )}
    </div>
  );
}

function SlotIcon({isActive, orientation, isHighlighted}: {
  isActive: boolean;
  orientation: "left" | "right";
  isHighlighted: boolean;
}) {
  if (!isActive) return null;

  const iconClass = [
    "parking-slot__car-icon",
    orientation === "right" ? "parking-slot__car-icon--flipped" : "",
    isHighlighted ? "parking-slot__car-icon--highlighted" : "",
  ].filter(Boolean).join(" ");

  return <Car className={iconClass} size={36} />;
}

export function ParkingSlot({
  slotNumber,
  isActive,
  zone,
  slotType = "standard",
  licensePlate,
  isHighlighted = false,
  orientation = "left",
}: ParkingSlotProps) {

  const containerClass = getSlotClassNames(isActive, slotType, isHighlighted);
  const statusLabel = isActive ? "occupied" : "available";
  return (
    <div className={containerClass} aria-label={`Slot ${slotNumber}, Zone ${zone}, ${statusLabel}`}>
      <SlotBadge slotType={slotType} />
      <SlotIcon
        isActive={isActive}
        orientation={orientation}
        isHighlighted={isHighlighted}
      />
      <span className="parking-slot__number">{slotNumber}</span>
      {licensePlate && isActive && (
        <span className="parking-slot__plate">
          {licensePlate}
        </span>
      )}
    </div>
  );
}
