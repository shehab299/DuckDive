import SearchPage from "../Pages/SearchPage";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import ResultsPage from "../Pages/ResultsPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SearchPage />} />
        <Route path="/results" element={<ResultsPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
