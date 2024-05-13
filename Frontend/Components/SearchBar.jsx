import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import styles from "./SearchBar.module.css";

import SearchSuggestions from "./SearchSuggestions";

// import { IoIosSearch } from "react-icons/io";

function SearchBar({ customStyle }) {
  const [searchTerm, setSearchTerm] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [showSuggestions, setShowSuggestions] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    function callback(e) {
      if (e.code === "Enter" && searchTerm !== "") {
        navigate(`/search?query=${searchTerm}`);
      }
    }

    document.addEventListener("keydown", callback);

    return function cleanup() {
      document.removeEventListener("keydown", callback);
    };
  }, [searchTerm]);

  useEffect(() => {
    const controller = new AbortController();
    async function fetchSuggestions() {
      try {
        const res = await fetch(
          `http://localhost:3030/complete?query=${searchTerm}`,
          {
            signal: controller.signal,
          }
        );
        if (!res.ok) {
          throw new Error("Failed to fetch results");
        }
        const data = await res.json();
        setSuggestions(data.suggestions);
      } catch (error) {
        if (error.name !== "AbortError")
          console.error("Error fetching results:", error);
      }
    }

    console.log(suggestions);
    fetchSuggestions();

    return function cleanup() {
      controller.abort();
    };
  }, [searchTerm]);

  function handleChange(value) {
    setSearchTerm(value);
  }

  function handleSelectSuggestion(value) {
    setSearchTerm(value);
    setShowSuggestions(false);
    navigate(`/search?query=${value}`);
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
        style={customStyle}
      />
      {/* <IoIosSearch /> */}
      {showSuggestions && suggestions.length !== 0 && (
        <SearchSuggestions
          onSelectSuggestion={handleSelectSuggestion}
          suggestions={suggestions}
          style={customStyle}
        />
      )}
    </>
  );
}

export default SearchBar;
