import { Route } from "react-router-dom";
import AdminMainPage from "../pages/protected/admin/AdminPage/AdminMainPage";
import CreateAccountPage from "../pages/protected/admin/AdminPage/createAccountPage/CreateAccountPage";
import ChangeUserPassword from "../pages/protected/admin/ChangeUserPassword/ChangeUserPassword";
import EditAnyAccountPage from "../pages/protected/admin/EditAnyAccountPage/EditAnyAccountPage";
import UserManagment from "../pages/protected/admin/UsersManagmentPage/UsersManagmentPage";
import ClientMainPage from "../pages/protected/client/ClientMainPage";
import SpecialistMainPage from "../pages/protected/specialist/SpecialistMainPage";
import {AppointmentListPage} from "../pages/protected/admin/AppointmentList";

const authorizedRoutes = (accessLevel: AccessLevelType) => {
    switch (accessLevel) {
        case "ADMINISTRATOR": {
            return (
                <>
                    <Route path="/accounts" element={<UserManagment />} />
                    <Route path="/accounts/:login" element={<EditAnyAccountPage />} />
                    <Route
                        path="/accounts/:login/change-password"
                        element={<ChangeUserPassword />}
                    />
                    <Route path="/visits" element={<AppointmentListPage />} />
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
