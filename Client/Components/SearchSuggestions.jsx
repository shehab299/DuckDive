import React, { forwardRef } from "react";

import SearchSuggestion from "./SearchSuggestion";

import styles from "./SearchSuggestions.module.css";

const SearchSuggestions = forwardRef(
  ({ style, suggestions, onSelectSuggestion }, ref) => {
    return (
      <div ref={ref} className={styles.container} style={{ ...style }}>
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
);

export default SearchSuggestions;
