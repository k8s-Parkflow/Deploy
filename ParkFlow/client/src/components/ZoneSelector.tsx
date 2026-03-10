import "../styles/ZoneSelector.css";
import { useState, useEffect } from "react";
import { TOTAL_ZONES } from "../utils/parkingUtils";
import { ChevronLeft, ChevronRight } from "lucide-react";

interface ZoneSelectorProps {
  selectedZoneId:    number;
  onZoneChange:      (id: number) => void;
}

export function ZoneSelector({ selectedZoneId, onZoneChange }: ZoneSelectorProps) {
  const [inputValue, setInputValue] = useState(selectedZoneId.toString());
  useEffect(() => {
    setInputValue(selectedZoneId.toString());
  }, [selectedZoneId]);

  const handlePrevious = () => {
    if (selectedZoneId > 1) onZoneChange(selectedZoneId - 1);
  };

  const handleNext = () => {
    if (selectedZoneId < TOTAL_ZONES) onZoneChange(selectedZoneId + 1);
  };

  return (
    <section className="zone-selector">
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
            value={inputValue}
            onChange={(e) => {
              const val = e.target.value;
              setInputValue(val);
              const num = parseInt(val, 10);
              if (!isNaN(num) && num >= 1 && num <= TOTAL_ZONES) {
                onZoneChange(num);
              }
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