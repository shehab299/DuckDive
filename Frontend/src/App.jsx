import SearchPage from "../Pages/SearchPage";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Results from "../Pages/Results";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SearchPage />} />
        <Route path="/results" element={<Results />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;