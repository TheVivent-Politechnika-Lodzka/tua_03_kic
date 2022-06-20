import { Route } from "react-router-dom";
import CreateUserPage from "../pages/protected/admin/CreateUserPage";
import EditAnyAccountPage from "../pages/protected/admin/EditAnyAccountPage/EditAnyAccountPage";
import UserManagment from "../pages/protected/admin/UsersManagmentPage/UsersManagmentPage";
import { AppointmentListPage } from "../pages/protected/admin/AppointmentList";
import SpecialistList from "../pages/unprotected/SpecialistList";

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
                </>
            );
        }
        case "SPECIALIST": {
            return <></>;
        }
        case "CLIENT": {
            return <></>;
        }
        default: {
            return <></>;
        }
    }
};

export default authorizedRoutes;
