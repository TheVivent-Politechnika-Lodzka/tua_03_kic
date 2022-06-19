import { Route } from "react-router-dom";
import CreateAccountPage from "../pages/protected/admin/AdminPage/createAccountPage/CreateAccountPage";
import ChangeUserPassword from "../pages/protected/admin/ChangeUserPassword/ChangeUserPassword";
import EditAnyAccountPage from "../pages/protected/admin/EditAnyAccountPage/EditAnyAccountPage";
import UserManagment from "../pages/protected/admin/UsersManagmentPage/UsersManagmentPage";
import { CreateImplantPage } from "../pages/protected/admin/CreateImplantPage";
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
                    <Route
                        path="/create-implant"
                        element={<CreateImplantPage />}
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
