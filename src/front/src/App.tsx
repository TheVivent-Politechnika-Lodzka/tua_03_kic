import React from "react";
import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import MainPage from "./pages/unprotected/home/HomePage";
import ErrorPage from "./pages/unprotected/error/ErrorPage";
import LoginPage from "./pages/unprotected/loginPage/LoginPage";
import { useStoreSelector, useStoreDispatch } from "./redux/reduxHooks";
import AdminPage from "./pages/admin/AdminPage/AdminPage";
import ClientPage from "./pages/client/ClientPage";
import SpecialistPage from "./pages/specialist/SpecialistPage";
import jwtDecode from "jwt-decode";
import { login as loginDispatch } from "./redux/userSlice";
import ActivateAccountPage from "./pages/unprotected/activate/ActivateAccountPage";
import HomeLayout from "./component/Layout/HomeLayout";
import DetailsLayout from "./component/Layout/DetailsLayout";
import UserManagment from "./pages/admin/UserManagment/UserManagment";
import CreateAccountPage from "./pages/admin/AdminPage/createAccountPage/CreateAccountPage";

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
          <Route element={<DetailsLayout />}>
            {user.cur === "ADMINISTRATOR" ? (
              <>
                <Route path="/admin" element={<AdminPage />} />
                <Route path="/admin/users" element={<UserManagment />} />
                <Route path="/createAccount" element={<CreateAccountPage />} />
              </>
            ) : (
              <></>
            )}
            {user.cur === "CLIENT" ? (
              <Route path="/client" element={<ClientPage />} />
            ) : (
              <></>
            )}
            {user.cur === "SPECIALIST" ? (
              <Route path="/specialist" element={<SpecialistPage />} />
            ) : (
              <></>
            )}
          </Route>
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
