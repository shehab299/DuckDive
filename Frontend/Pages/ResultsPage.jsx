import React, { useEffect, useState } from "react";
import SearchBar from "../Components/SearchBar";
import Result from "../Components/Result";

import styles from "./ResultsPage.module.css";
import PagePagination from "../Components/PagePagination";
import duckDive from "../assets/duckDive.png";
import { useLocation } from "react-router-dom";

function ResultsPage() {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const searchTerm = searchParams.get("query");

  const [results, setResults] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);

  const postsPerPage = 10;

  const lastIndex = currentPage * postsPerPage;
  const firstIndex = lastIndex - postsPerPage;

  const numPages = Math.ceil(results.length / postsPerPage);

  useEffect(() => {
    const controller = new AbortController();

    async function fetchResults() {
      try {
        const res = await fetch(
          `http://localhost:3030/search?query=${searchTerm}`,
          {
            signal: controller.signal,
          }
        );
        if (!res.ok) {
          throw new Error("Failed to fetch results");
        }
        const data = await res.json();
        setResults(data.results);
      } catch (error) {
        if (error.name !== "AbortError")
          console.error("Error fetching results:", error);
      }
    }

    fetchResults();

    return () => {
      controller.abort();
    };
  }, []);

  return (
    <>
      <div className={styles.search}>
        <img className={styles.logo} src={duckDive} width={"110px"} />
        <div className={styles.searchBar}>
          <SearchBar customStyle={{ width: "100%" }} />
        </div>
      </div>
      <div className={styles.container}>
        <div>
          {results.slice(firstIndex, lastIndex).map((result, index) => (
            <Result result={result} key={index} />
          ))}
        </div>
        <PagePagination
          numPages={numPages}
          currentPage={currentPage}
          setCurrentPage={setCurrentPage}
        />
      </div>
    </>
  );
}

export default ResultsPage;
