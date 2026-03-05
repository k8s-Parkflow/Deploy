import { LucideIcon } from 'lucide-react';
import "../../styles/StatPanel.css";

interface StatPanelProps {
  category?: string;
  label: string;
  value: number;
  total?: number;
  sub?: string;
  icon: LucideIcon;
  iconClass?: string;
  fillPct?: number;
}

export function StatPanel({ category="", label, value, total, sub, icon: Icon, iconClass="", fillPct=0 }: StatPanelProps) {
  return (
    <div className={`stat-panel stat-panel--${category}`}>
      {category==="hero" && <WaveFill pct={fillPct}/>}
      <div className="stat-panel__body">
        <p className="stat-panel__label">{label}</p>
        <p className="stat-panel__value">
          {value}
          {total !== undefined && (
            <span className="stat-panel__denom">/{total}</span>
          )}
        </p>
        {sub && <p className="stat-panel__sub">{sub}</p>}
      </div>
      <div className={`stat-panel__icon ${iconClass}`}>
        <Icon size='auto' />
      </div>
    </div>
  );
}

function WaveFill({ pct }: { pct: number }) {
  const fill = Math.min(100, Math.max(0, pct));
  // 100% available 일때 풀, 만석일때 0
  const translateY = 100 - fill;

  return (
    <div
      className="wave-fill"
      style={{ transform: `translateY(${translateY}%)` }}
      aria-hidden="true"
    >
      <svg
        className="wave-fill__crest"
        viewBox="0 0 200 18"
        preserveAspectRatio="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path d="M0,9 C30,18 70,0 100,9 C130,18 170,0 200,9 L200,18 L0,18 Z" />
      </svg>
      <div className="wave-fill__body" />
    </div>
  );
}
