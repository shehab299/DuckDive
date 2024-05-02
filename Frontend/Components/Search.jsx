import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import styles from "./Search.module.css";

import SearchSuggestions from "./SearchSuggestions";

// import { IoIosSearch } from "react-icons/io";

function Search() {
  const [suggestions, setSuggestions] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [showSuggestions, setShowSuggestions] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    function callback(e) {
      if (e.code === "Enter" && searchTerm !== "") {
        navigate("/results");
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
      const res = await fetch(
        "https://bpkv76oojc.api.quickmocker.com/complete",
        { signal: controller.signal }
      );
      const data = await res.json();
      setSuggestions(data.suggestions);
      return function cleanup() {
        controller.abort();
      };
    }
    fetchSuggestions();
    console.log(suggestions);
  }, [searchTerm]);

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
      {showSuggestions && suggestions.length !== 0 && (
        <SearchSuggestions
          onSelectSuggestion={handleSelectSuggestion}
          suggestions={suggestions}
        />
      )}
    </>
  );
}

export default Search;
