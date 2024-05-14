import React, { useEffect } from "react";

import Search from "../Components/SearchBar";

import styles from "./SearchPage.module.css";

import logo from "../assets/duck.png";
import duckDive from "../assets/duckDive.png";

function SearchPage() {
  useEffect(() => {
    const body = document.querySelector("body");
    body.style.backgroundColor = "#fffae6";
    return () => {
      body.style.backgroundColor = "";
    };
  }, []);

  return (
    <div className={styles.container}>
      <img src={logo} alt="logo" height={240} />
      <img className={styles.duckDive} src={duckDive} height={60} />
      <Search customStyle={{ position: "relative"}} />
    </div>
  );
}

export default SearchPage;
