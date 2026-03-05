import { Search, X } from "lucide-react";
import { SearchError } from "../../hooks/useSearch";
import "../../styles/SearchPanel.css";

interface SearchPanelProps {
  query: string;
  isValidPlate: boolean;
  searchError: SearchError;
  onChange: (q: string) => void;
  onSearch: () => void;
}

const ERROR_MESSAGES: Record<NonNullable<SearchError>, string> = {
  invalid_format: "예) 12가 3456 또는 123가 4567",
  not_found: "그런 차 없삼",
}

export function SearchPanel({ query, isValidPlate, searchError, onChange, onSearch }: SearchPanelProps) {
  const showError = !!searchError;

  return (
    <div className="stat-panel stat-panel--search">
      <p className="stat-panel__label" style={{ marginBottom: "0.5rem" }}>번호판 검색</p>
      <div className="search-hint">
        {showError && (
          <p className = "search-hint--error">{ERROR_MESSAGES[searchError!]}</p>
        )}
      </div>
      <div className="search-row">
        <div className="search-input-wrap">
          <Search size={14} className="search-icon" />
          <input
            className="search-input"
            type="text"
            placeholder="예: 12가 3456"
            value={query}
            maxLength={9}
            onChange={(e) => onChange(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && onSearch()}
          />
        </div>
        <button 
          className="btn-search" 
          onClick={onSearch} 
        >
          검색
        </button>
      </div>
    </div>
  );
}