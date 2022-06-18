import { Route } from "react-router-dom";
import CreateAccountPage from "../pages/protected/admin/AdminPage/createAccountPage/CreateAccountPage";
import ChangeUserPassword from "../pages/protected/admin/ChangeUserPasswordPage/ChangeUserPasswordPage";
import UserDetails from "../pages/protected/admin/UserDetails/UserDetails";
import UserManagment from "../pages/protected/admin/UsersManagmentPage/UsersManagmentPage";

const authorizedRoutes = (accessLevel: AccessLevelType) => {
    switch (accessLevel) {
        case "ADMINISTRATOR": {
            return (
                <>
                    <Route path="/accounts" element={<UserManagment />} />
                    <Route path="/accounts/:login" element={<UserDetails />} />
                    <Route
                        path="/accounts/create-account"
                        element={<CreateAccountPage />}
                    />
                </>
            );
        }
        case "SPECIALIST": {
            return (
                <>
                </>
            );
        }
        case "CLIENT": {
            return (
                <>
                </>
            );
        }
        default: {
            return <></>;
        }
    }
};

export default authorizedRoutes;
