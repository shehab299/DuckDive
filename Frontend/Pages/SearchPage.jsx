import React from "react";

import Search from "../Components/Search";

import styles from "./SearchPage.module.css";

function SearchPage() {
  return (
    <div className={styles.container}>
      <img src="./assets/duck.png" alt="My Image" height={240} />
      <img src="./assets/duckDive.png" height={60} />
      <Search />
    </div>
  );
}

export default SearchPage;