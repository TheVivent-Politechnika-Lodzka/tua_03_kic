import React from "react";
import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomePage from "./pages/unprotected/home/HomePage";
import ErrorPage from "./pages/unprotected/error/ErrorPage";
import LoginPage from "./pages/unprotected/loginPage/LoginPage";
import { useStoreSelector, useStoreDispatch } from "./redux/reduxHooks";
import jwtDecode from "jwt-decode";
import { login as loginDispatch } from "./redux/userSlice";
import ActivateAccountPage from "./pages/unprotected/activate/ActivateAccountPage";
import HomeLayout from "./component/Layout/HomeLayout";
import ResetPasswordForm from "./component/Form/resetPasswordForm/ResetPasswordForm";
import ResetPasswordTokenForm from "./component/Form/resetPasswordTokenForm/ResetPasswordTokenForm";
import AuthorizedLayoutPage from "./pages/protected/AuthoirzedLayout/AuthorizedLayoutPage";
import { ValidationProvider } from "./context/validationContext";
import AccountDetailsPage from "./pages/protected/shared/AccountDetailsPage/AccountDetailsPage";
import EditOwnAccountPage from "./pages/protected/shared/EditOwnAccountPage/EditOwnAccountPage";
import UserDetails from "./pages/protected/admin/UserDetails/UserDetails";
import UserManagment from "./pages/protected/admin/UserManagment/UserManagment";
import ChangeUserPassword from "./pages/protected/admin/ChangeUserPassword/ChangeUserPassword";
import CreateAccountPage from "./pages/protected/admin/AdminPage/createAccountPage/CreateAccountPage";
import AdminPage from "./pages/protected/admin/AdminPage/AdminPage";

function App() {
    const user = useStoreSelector((state) => state.user);

    const dispatch = useStoreDispatch();
    const token = localStorage.getItem("ACCESS_TOKEN");
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

    return (
        <Router>
            <ValidationProvider>
                <Routes>
                    {user.cur && (
                        <Route element={<AuthorizedLayoutPage />}>
                            <Route
                                path="/account"
                                element={<AccountDetailsPage />}
                            />
                            <Route
                                path="/account/edit"
                                element={<EditOwnAccountPage />}
                            />
                            {user.cur === "ADMINISTRATOR" && (
                                <>
                                    <Route path="/" element={<AdminPage />} />
                                    <Route
                                        path="/accounts"
                                        element={<UserManagment />}
                                    />
                                    <Route
                                        path="/accounts/:login"
                                        element={<UserDetails />}
                                    />
                                    <Route
                                        path="/accounts/:login/change-password"
                                        element={<ChangeUserPassword />}
                                    />
                                    <Route
                                        path="/accounts/create-account"
                                        element={<CreateAccountPage />}
                                    />
                                </>
                            )}
                        </Route>
                    )}
                    <Route element={<HomeLayout />}>
                        <Route path="/" element={<HomePage />} />
                    </Route>
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/active" element={<ActivateAccountPage />} />
                    <Route
                        path="/reset-password"
                        element={<ResetPasswordForm />}
                    />
                    <Route
                        path="/reset-password-token"
                        element={<ResetPasswordTokenForm />}
                    />
                    <Route path="/*" element={<ErrorPage />} />
                </Routes>
            </ValidationProvider>
        </Router>
    );
}

export default App;
