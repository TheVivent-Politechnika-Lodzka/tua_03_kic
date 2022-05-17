import React from "react";
import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import MainPage from "./pages/commonPages/MainPage/MainPage";
import ErrorPage from "./pages/commonPages/ErrorPage/ErrorPage";
import LoginPage from "./pages/commonPages/LoginPage/LoginPage";
import { useStoreSelector, useStoreDispatch } from "./redux/reduxHooks";
import AdminPage from "./pages/adminPages/AdminPage/AdminPage";
import ClientPage from "./pages/clientPages/ClientPage";
import SpecialistPage from "./pages/specialistPages/SpecialistPage";
import jwtDecode from "jwt-decode";
import { login as loginDispatch } from "./redux/userSlice";
import ActiveAccountPage from "./pages/commonPages/ActiveAccountPage/ActiveAccountPage";

function App() {
  const user = useStoreSelector((state) => state.user);

  const dispatch = useStoreDispatch();
  const token = localStorage.getItem("AUTH_TOKEN");
  try {
    if (user.exp === "" && token) {
      const decoded_token = jwtDecode(token);

      if (decoded_token) {
        dispatch(loginDispatch(decoded_token));
      }
    }
  } catch (error) {
    console.log(error);
  }
  console.log(user);

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
          <Route path="/active" element={<ActiveAccountPage />} />
          <Route path="/*" element={<ErrorPage />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
