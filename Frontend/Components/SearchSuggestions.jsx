import React from "react";

import SearchSuggestion from "./SearchSuggestion";

import styles from "./SearchSuggestions.module.css";

function SearchSuggestions({ suggestions, onSelectSuggestion }) {
  return (
    <div className={styles.container}>
      {suggestions.map((suggestion, index) => (
        <SearchSuggestion
          onSelectSuggestion={onSelectSuggestion}
          suggestion={suggestion}
          key={index}
        />
      ))}
    </div>
  );
}

export default SearchSuggestions;
