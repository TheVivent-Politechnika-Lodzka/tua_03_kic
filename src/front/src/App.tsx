import React from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import MainPage from './pages/MainPage';
import ErrorPage from './pages/ErrorPage';

function App() {
  return (
      <Router>
          <Routes>
                 <Route path="/" element={<MainPage />} />
                 <Route path="/*" element={<ErrorPage />} />
          </Routes>
      </Router>
);
}

export default App;
