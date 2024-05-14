import React, { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";

import styles from "./SearchBar.module.css";

import SearchSuggestions from "./SearchSuggestions";

// import { IoIosSearch } from "react-icons/io";

function SearchBar({ customStyle }) {
  const [searchTerm, setSearchTerm] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [showSuggestions, setShowSuggestions] = useState(false);

  const searchBarRef = useRef(null);
  const suggestionsRef = useRef(null);

  const navigate = useNavigate();

  let widthInherit;

  useEffect(() => {
    function handleClickOutside(event) {
      if (
        searchBarRef.current &&
        !searchBarRef.current.contains(event.target) &&
        suggestionsRef.current &&
        !suggestionsRef.current.contains(event.target)
      ) {
        setShowSuggestions(false);
      }
    }

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);
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
          `http://localhost:8080/suggest?query=${searchTerm}`,
          {
            signal: controller.signal,
          }
        );
        if (!res.ok) {
          throw new Error("Failed to fetch results");
        }
        const data = await res.json();
        setSuggestions(data);
      } catch (error) {
        if (error.name !== "AbortError")
          console.error("Error fetching results:", error);
      }
    }

    fetchSuggestions();

    return function cleanup() {
      controller.abort();
    };
  }, [searchTerm]);

  useEffect(() => {
    function handleResize() {
      if (searchBarRef.current) {
        const inputWidth = searchBarRef.current.getBoundingClientRect().width;;
        const suggestionsContainer = suggestionsRef.current;
        if (suggestionsContainer) {
          suggestionsContainer.style.width = `${inputWidth}px`;
          widthInherit = inputWidth;
        }
      }
    }

    window.addEventListener("resize", handleResize);
    handleResize();

    return () => window.removeEventListener("resize", handleResize);
  }, [showSuggestions]);

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
        ref={searchBarRef}
        type="text"
        className={`${styles.searchBar} ${
          showSuggestions ? styles.focused : ""
        }`}
        placeholder="The Quackest Search..."
        value={searchTerm}
        onChange={(e) => handleChange(e.target.value)}
        onClick={() => setShowSuggestions(true)}
        style={{ ...customStyle }}
      />
      {/* <IoIosSearch /> */}
      {showSuggestions && suggestions.length !== 0 && (
        <SearchSuggestions
          ref={suggestionsRef}
          onSelectSuggestion={handleSelectSuggestion}
          suggestions={suggestions}
          style={{ ...customStyle, width: widthInherit }}
        />
      )}
    </>
  );
}

export default SearchBar;
