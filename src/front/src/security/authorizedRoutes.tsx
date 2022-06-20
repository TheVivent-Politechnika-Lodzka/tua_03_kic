import { Route } from "react-router-dom";

import CreateUserPage from "../pages/protected/admin/CreateUserPage";
import EditAnyAccountPage from "../pages/protected/admin/EditAnyAccountPage/EditAnyAccountPage";
import UserManagment from "../pages/protected/admin/UsersManagmentPage/UsersManagmentPage";
import { AppointmentListPage } from "../pages/protected/admin/AppointmentListPage";
import { CreateImplantPage } from "../pages/protected/admin/CreateImplantPage";
import OwnAppointmentsListPage from "../pages/protected/shared/OwnAppointmentsListPage/OwnAppointmentsListPage";

const authorizedRoutes = (accessLevel: AccessLevelType) => {
    switch (accessLevel) {
        case "ADMINISTRATOR": {
            return (
                <>
                    <Route path="/accounts" element={<UserManagment />} />
                    <Route
                        path="/accounts/:login"
                        element={<EditAnyAccountPage />}
                    />
                    <Route path="/visits" element={<AppointmentListPage />} />
                    <Route
                        path="/accounts/create-account"
                        element={<CreateUserPage />}
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
                    <Route
                        path="/visits"
                        element={<OwnAppointmentsListPage />}
                    />
                </>
            );
        }
        case "CLIENT": {
            return (
                <>
                    <Route
                        path="/visits"
                        element={<OwnAppointmentsListPage />}
                    />
                </>
            );
        }
        default: {
            return <></>;
        }
    }
};

export default authorizedRoutes;
