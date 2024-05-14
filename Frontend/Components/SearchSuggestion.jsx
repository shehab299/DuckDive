import React from "react";

import styles from "./SearchSuggestion.module.css";
import { GiBackwardTime } from "react-icons/gi";

function SearchSuggestion({ suggestion, onSelectSuggestion }) {
  return (
    <div
      onClick={() => onSelectSuggestion(suggestion.query)}
      className={styles.container}
    >
      <GiBackwardTime style={{ color: "grey" }} /> {suggestion.query}
    </div>
  );
}

export default SearchSuggestion;
