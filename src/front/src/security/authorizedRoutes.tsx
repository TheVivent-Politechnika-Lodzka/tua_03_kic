import { Route } from "react-router-dom";

import CreateUserPage from "../pages/protected/admin/CreateUserPage";
import EditAnyAccountPage from "../pages/protected/admin/EditAnyAccountPage/EditAnyAccountPage";
import UserManagment from "../pages/protected/admin/UsersManagmentPage/UsersManagmentPage";
import { OwnAppointmentsList } from "../pages/protected/shared/OwnAppointmentsList/OwnAppointmentsList";
import { AppointmentListPage } from "../pages/protected/admin/AppointmentList";
import { CreateImplantPage } from "../pages/protected/admin/CreateImplantPage";

import ClientMainPage from "../pages/protected/client/ClientMainPage";
import CreateAppointmentPage from "../pages/protected/client/CreateAppointmentPage";
import SpecialistMainPage from "../pages/protected/specialist/SpecialistMainPage";

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
                    <Route path="/visits" element={<OwnAppointmentsList />} />
                </>
            );
        }
        case "CLIENT": {
            return (
                <>
                    <Route path="/visits" element={<OwnAppointmentsList />} />
                    <Route
                        path="/implant/:implantId/create-appointment"
                        element={<CreateAppointmentPage />}
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
