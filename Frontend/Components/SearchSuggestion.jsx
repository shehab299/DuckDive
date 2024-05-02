import React from "react";

import styles from "./SearchSuggestion.module.css";

function SearchSuggestion({ suggestion, onSelectSuggestion }) {
  return (
    <div
      onClick={() => onSelectSuggestion(suggestion.query)}
      className={styles.container}
    >
      {suggestion.query}
    </div>
  );
}

export default SearchSuggestion;
