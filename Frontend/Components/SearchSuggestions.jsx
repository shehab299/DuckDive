import React from "react";

import SearchSuggestion from "./SearchSuggestion";

import styles from "./SearchSuggestions.module.css";

function SearchSuggestions({ style, suggestions, onSelectSuggestion }) {
  return (
    <div className={styles.container}>
      {suggestions.map((suggestion, index) => (
        <SearchSuggestion
          onSelectSuggestion={onSelectSuggestion}
          suggestion={suggestion}
          key={index}
          style={style}
        />
      ))}
    </div>
  );
}

export default SearchSuggestions;
