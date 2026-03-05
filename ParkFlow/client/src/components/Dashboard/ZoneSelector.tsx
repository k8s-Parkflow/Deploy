import "../../styles/ZoneSelector.css";
import { TOTAL_ZONES } from "../../utils/parkingUtils";
import { ChevronLeft, ChevronRight } from "lucide-react";

interface ZoneSelectorProps {
  selectedZoneId:    number;
  onZoneChange:      (id: number) => void;
}

export function ZoneSelector({ selectedZoneId, onZoneChange }: ZoneSelectorProps) {
  const handlePrevious = () => {
    if (selectedZoneId > 1) onZoneChange(selectedZoneId - 1);
  };

  const handleNext = () => {
    if (selectedZoneId < TOTAL_ZONES) onZoneChange(selectedZoneId + 1);
  };

  return (
    <section className="zone-selector">
      <div className="zone-selector__header">
        <h3 className="zone-selector__title">구역 선택</h3>
        <span className="zone-selector__or">(1–{TOTAL_ZONES}):</span>
      </div>
      <div className="zone-selector__nav">
        <button 
          className="zone-selector__navbtn"
          onClick={handlePrevious}
          disabled={selectedZoneId <= 1}
          aria-label="Previous Zone"
        >
          <ChevronLeft size={20} strokeWidth={2.5} />
        </button>
        <div className="zone-selector__manual">
          <input
            className="zone-input"
            type="number"
            min={1}
            max={TOTAL_ZONES}
            value={selectedZoneId}
            onChange={(e) => {
              const v = Number(e.target.value);
              if (v >= 1 && v <= TOTAL_ZONES) onZoneChange(v);
            }}
          />
        </div>
        <button 
          className="zone-selector__navbtn"
          onClick={handleNext}
          disabled={selectedZoneId >= TOTAL_ZONES}
          aria-label="Next Zone"
        >
          <ChevronRight size={20} strokeWidth={2.5} />
        </button>
      </div>
    </section>
  );
}