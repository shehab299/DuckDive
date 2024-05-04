import React from "react";

import styles from "./SearchSuggestion.module.css";
import { GiBackwardTime } from "react-icons/gi";

function SearchSuggestion({ style, suggestion, onSelectSuggestion }) {
  return (
    <div
      onClick={() => onSelectSuggestion(suggestion.query)}
      className={styles.container}
      // style={style}
    >
      <GiBackwardTime style={{ color: "grey" }} /> {suggestion.query}
    </div>
  );
}

export default SearchSuggestion;
