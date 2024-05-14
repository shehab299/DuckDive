import React from "react";
import { NavLink } from "react-router-dom";
import styles from "./Result.module.css"; // Adjust the import as needed

function Result({ result }) {
  // Regular expression to find single quoted words
  const regex = /'(\w+)'/g;

  // Replace single quoted words with a bold version
  const formattedDescription = result.description.replace(
    regex,
    (match, p1) => `<strong>${p1}</strong>`
  );

  return (
    <div className={styles.container}>
      <p>{result.title}</p>
      <NavLink className={styles.link} to={result.url}>
        {result.url}
      </NavLink>
      <p
        className={styles.snippet}
        dangerouslySetInnerHTML={{ __html: formattedDescription }}
      />
    </div>
  );
}

export default Result;
