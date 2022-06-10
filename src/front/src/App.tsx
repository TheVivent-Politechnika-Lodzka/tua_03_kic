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
import authorizedRoutes from "./security/authorizedRoutes";

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
        console.error(error);
    }

    return (
        <Router>
            <ValidationProvider>
                <Routes>
                    <Route path="/*" element={<ErrorPage />} />
                    {user.cur ? (
                        <Route element={<AuthorizedLayoutPage />}>
                            <Route
                                path="/account"
                                element={<AccountDetailsPage />}
                            />
                            <Route
                                path="/account/edit"
                                element={<EditOwnAccountPage />}
                            />
                            {authorizedRoutes(user.cur as AccessLevelType)}
                        </Route>
                    ) : (
                        <>
                            <Route element={<HomeLayout />}>
                                <Route path="/" element={<HomePage />} />
                            </Route>
                            <Route path="/login" element={<LoginPage />} />
                            <Route
                                path="/active"
                                element={<ActivateAccountPage />}
                            />
                            <Route
                                path="/reset-password"
                                element={<ResetPasswordForm />}
                            />
                            <Route
                                path="/reset-password-token"
                                element={<ResetPasswordTokenForm />}
                            />
                        </>
                    )}
                </Routes>
            </ValidationProvider>
        </Router>
    );
}

export default App;
