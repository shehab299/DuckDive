import React, { useState } from "react";

import styles from "./Result.module.css";
import { NavLink } from "react-router-dom";

function Result({ result }) {
  return (
    <div className={styles.container}>
      <p>{result.title}</p>
      <NavLink className={styles.link} to={result.url}>
        {result.url}
      </NavLink>
      <p className={styles.snippet}>{result.description}</p>
    </div>
  );
}

export default Result;
