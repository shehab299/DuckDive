import SearchPage from "../Pages/SearchPage";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import ResultsPage from "../Pages/ResultsPage";
import { useState } from "react";

function App() {
  const [searchTerm, setSearchTerm] = useState("");
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SearchPage searchTerm={searchTerm} setSearchTerm={setSearchTerm}/>} />
        <Route path="/results" element={<ResultsPage searchTerm={searchTerm}/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
