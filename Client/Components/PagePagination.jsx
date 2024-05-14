import React, { useState } from "react";

import styles from "./PagePagination.module.css";

import { IoIosArrowForward } from "react-icons/io";
import { IoIosArrowBack } from "react-icons/io";

function PagePagination({ numPages, setCurrentPage, currentPage }) {
  const [maxPageNumber, setMaxPageNumber] = useState(5);
  const [minPageNumber, setMinPageNumber] = useState(0);
  const pageNumberLimit = 5;

  let pages = [];
  for (let index = 1; index <= numPages; index++) {
    pages.push(index);
  }

  function getNextPage() {
    if (currentPage === maxPageNumber && numPages > maxPageNumber) {
      setMaxPageNumber((prev) =>
        prev + pageNumberLimit > numPages ? numPages : prev + pageNumberLimit
      );
      setMinPageNumber(currentPage);
    }
    setCurrentPage((prev) => (prev === numPages ? prev : prev + 1));
  }

  function getPrevPage() {
    if (currentPage === minPageNumber + 1 && minPageNumber !== 0) {
      setMinPageNumber((prev) =>
        prev + pageNumberLimit <= 0 ? 0 : prev - pageNumberLimit
      );
      setMaxPageNumber(currentPage - 1);
    }
    setCurrentPage((prev) => (prev === 1 ? prev : prev - 1));
  }

  return (
    <div className={styles.pagination}>
      <div className={styles.arrow} onClick={getPrevPage}>
        <IoIosArrowBack />
      </div>
      {minPageNumber > 0 && <div>...</div>}
      {pages.slice(minPageNumber, maxPageNumber).map((page, index) => (
        <div
          className={`${styles.number} ${
            currentPage === page ? styles.active : ""
          }`}
          key={index}
          onClick={() => setCurrentPage(page)}
        >
          {page}
        </div>
      ))}
      {maxPageNumber !== numPages && <div>...</div>}
      <div className={styles.arrow}>
        <IoIosArrowForward onClick={getNextPage} />
      </div>
    </div>
  );
}

export default PagePagination;
