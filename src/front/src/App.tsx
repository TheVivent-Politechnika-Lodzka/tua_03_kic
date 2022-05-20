import React from "react";
import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import MainPage from "./pages/unprotected/home/HomePage";
import ErrorPage from "./pages/unprotected/error/ErrorPage";
import LoginPage from "./pages/unprotected/account/loginPage/LoginPage";
import { useStoreSelector, useStoreDispatch } from "./redux/reduxHooks";
import AdminPage from "./pages/admin/AdminPage/AdminPage";
import ClientPage from "./pages/client/ClientPage";
import SpecialistPage from "./pages/specialist/SpecialistPage";
import jwtDecode from "jwt-decode";
import { login as loginDispatch } from "./redux/userSlice";
import ActivateAccountPage from "./pages/unprotected/account/activate/ActivateAccountPage";
import HomeLayout from "./component/Layout/HomeLayout";


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

          <Route element={<HomeLayout />}>
            <Route path="/" element={<MainPage />} />
          </Route>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/active" element={<ActivateAccountPage />} />
          <Route path="/*" element={<ErrorPage />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
