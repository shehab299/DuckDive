import React from "react";

import styles from "./SearchSuggestion.module.css";

function SearchSuggestion({ suggestion, onSelectSuggestion }) {
  return (
    <div
      onClick={() => onSelectSuggestion(suggestion)}
      className={styles.container}
    >
      {suggestion}
    </div>
  );
}

export default SearchSuggestion;
