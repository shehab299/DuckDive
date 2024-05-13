import React from "react";

import Search from "../Components/SearchBar";

import styles from "./SearchPage.module.css";

import logo from "../assets/duck.png";
import duckDive from "../assets/duckDive.png";

function SearchPage({searchTerm, setSearchTerm}) {
  return (
    <div className={styles.container}>
      <img src={logo} alt="logo" height={240} />
      <img src={duckDive} height={60} />
      <Search searchTerm={searchTerm} setSearchTerm={setSearchTerm} />
    </div>
  );
}

export default SearchPage;