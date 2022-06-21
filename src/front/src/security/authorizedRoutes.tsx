import { Route } from "react-router-dom";

import CreateUserPage from "../pages/protected/admin/CreateUserPage";
import EditAnyAccountPage from "../pages/protected/admin/EditAnyAccountPage/EditAnyAccountPage";
import UserManagment from "../pages/protected/admin/UsersManagmentPage/UsersManagmentPage";
import { OwnAppointmentsList } from "../pages/protected/shared/OwnAppointmentsList/OwnAppointmentsList";
import { AppointmentListPage } from "../pages/protected/admin/AppointmentList";
import { CreateImplantPage } from "../pages/protected/admin/CreateImplantPage";
import { EditOwnAppointment } from "../pages/protected/shared/EditOwnAppointment";
import { EditImplantPage } from "../pages/protected/admin/EditImplantPage";
import {EditAppointment} from "../components/EditAppointment";

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
                    <Route
                        path="/implant/:id"
                        element={<EditImplantPage />}
                    />
                    <Route path="/visit/edit/:id" element={<EditAppointment />} />
                </>
            );
        }
        case "SPECIALIST": {
            return (
                <>
                    <Route path="/visits" element={<OwnAppointmentsList />} />
                    <Route path="/visit/edit/:id" element={<EditOwnAppointment />} />
                </>
            );
        }
        case "CLIENT": {
            return (
                <>
                    <Route path="/visits" element={<OwnAppointmentsList />} />
                    <Route path="/visit/edit/:id" element={<EditOwnAppointment />} />
                </>
            );
        }
        default: {
            return <></>;
        }
    }
};

export default authorizedRoutes;
