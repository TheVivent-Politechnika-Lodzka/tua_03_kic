import React from "react";
import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import MainPage from "./pages/commonPages/MainPage/MainPage";
import ErrorPage from "./pages/commonPages/ErrorPage/ErrorPage";
import LoginPage from "./pages/commonPages/LoginPage/LoginPage";
import { useStoreSelector } from "./redux/reduxHooks";
import AdminPage from "./pages/adminPages/AdminPage/AdminPage";
import ClientPage from "./pages/clientPages/ClientPage";
import SpecialistPage from "./pages/specialistPages/SpecialistPage";

function App() {
  const user = useStoreSelector((state) => state.user);

  return (
    <>
      <Router>
        <Routes>
          {user.sub === "administrator" ? (
            <Route path="/admin" element={<AdminPage />} />
          ) : (
            <></>
          )}
          {user.sub === "client" ? (
            <Route path="/client" element={<ClientPage />} />
          ) : (
            <></>
          )}
          {user.sub === "specialist" ? (
            <Route path="/specialist" element={<SpecialistPage />} />
          ) : (
            <></>
          )}

          <Route path="/" element={<MainPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/*" element={<ErrorPage />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
