import React, { useState } from "react";

import styles from "./Search.module.css";

import SearchSuggestions from "./SearchSuggestions";

import { IoIosSearch } from "react-icons/io";

const suggestions = [
  "suggestion1",
  "suggestion2",
  "suggustion3",
  "suggestion4",
  "suggestion5",
  "suggestion6",
  "suggestion7",
];

function Search() {
  const [searchTerm, setSearchTerm] = useState("");
  const [showSuggestions, setShowSuggestions] = useState(false);

  function handleChange(value) {
    setSearchTerm(value);
  }

  function handleSelectSuggestion(value) {
    setSearchTerm(value);
    setShowSuggestions(false);
  }

  return (
    <>
      <input
        type="text"
        className={`${styles.searchBar} ${
          showSuggestions ? styles.focused : ""
        }`}
        placeholder="The Quackest Search..."
        value={searchTerm}
        onChange={(e) => handleChange(e.target.value)}
        onClick={() => setShowSuggestions(true)}
      />
      {/* <IoIosSearch /> */}
      {showSuggestions && (
        <SearchSuggestions
          onSelectSuggestion={handleSelectSuggestion}
          suggestions={suggestions}
        />
      )}
    </>
  );
}

export default Search;
