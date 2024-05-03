import React from "react";

import styles from "./PagePagination.module.css";

import { IoIosArrowForward } from "react-icons/io";
import { IoIosArrowBack } from "react-icons/io";

function PagePagination({ numPages, setCurrentPage, currentPage }) {
  let pages = [];
  for (let index = 1; index <= numPages; index++) {
    pages.push(index);
  }

  function getNextPage() {
    setCurrentPage((prev) => (prev === numPages ? prev : prev + 1));
  }

  function getPrevPage() {
    setCurrentPage((prev) => (prev === 1 ? prev : prev - 1));
  }

  return (
    <div className={styles.pagination}>
      <div className={styles.arrow} onClick={getPrevPage}>
        <IoIosArrowBack />
      </div>
      {pages.map((page, index) => (
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
      <div className={styles.arrow}>
        <IoIosArrowForward onClick={getNextPage} />
      </div>
    </div>
  );
}

export default PagePagination;
