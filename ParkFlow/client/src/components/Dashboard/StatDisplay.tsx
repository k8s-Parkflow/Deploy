import { CheckCircle, Zap, Accessibility } from "lucide-react";
import { StatPanel }   from "./StatPanel";
import { SearchPanel } from "./SearchPanel";
import type { GlobalStats } from "../../utils/parkingUtils";
import type { SearchError } from "../../hooks/useSearch";
import "../../styles/StatDisplay.css";

interface StatDisplayProps {
  globalStats:   GlobalStats;
  searchQuery:   string;
  isValidPlate: boolean,
  searchError: SearchError;
  onSearchChange:(q: string) => void;
  onSearch:      () => void;
}

export function StatDisplay({ globalStats, searchQuery, isValidPlate, searchError, onSearchChange, onSearch }: StatDisplayProps) {
  const { generalAvailable, generalTotal, disabledAvailable, disabledTotal, evAvailable, evTotal } = globalStats;

  return (
    <section className="stats-row">
      <StatPanel
        category="hero"
        label="일반 주차석"
        value={generalAvailable}
        total={generalTotal}
        fillPct={(generalAvailable / generalTotal) * 100}
        sub={`전체 일반 주차석의 ${((generalAvailable / (generalTotal || 1)) * 100).toFixed(0)}% 여유`}
        icon={CheckCircle}
        iconClass="stat-panel__icon--hero"
      />

      <StatPanel
        category="handicapped"
        label="장애인 주차석"
        value={disabledAvailable}
        total={disabledTotal}
        sub="Available"
        icon={Accessibility}
        iconClass="stat-panel__icon--blue"
      />

      <StatPanel
        category="EV"
        label="EV 충전석"
        value={evAvailable}
        total={evTotal}
        sub="Available"
        icon={Zap}
        iconClass="stat-panel__icon--purple"
      />

      <SearchPanel
        query={searchQuery}
        isValidPlate={isValidPlate}
        searchError={searchError}
        onChange={onSearchChange}
        onSearch={onSearch}
      />
    </section>
  );
}