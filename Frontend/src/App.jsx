import SearchPage from "../Pages/SearchPage";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import ResultsPage from "../Pages/ResultsPage";
import { useState } from "react";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SearchPage />} />
        <Route path="/search" element={<ResultsPage/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
