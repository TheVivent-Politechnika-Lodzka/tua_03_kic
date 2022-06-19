import { Route } from "react-router-dom";
import AdminMainPage from "../pages/protected/admin/AdminPage/AdminMainPage";
import CreateAccountPage from "../pages/protected/admin/AdminPage/createAccountPage/CreateAccountPage";
import ChangeUserPassword from "../pages/protected/admin/ChangeUserPassword/ChangeUserPassword";
import UserDetails from "../pages/protected/admin/UserDetails/UserDetails";
import UserManagment from "../pages/protected/admin/UsersManagmentPage/UsersManagmentPage";
import ClientMainPage from "../pages/protected/client/ClientMainPage";
import { OwnAppointmentsList } from "../pages/protected/shared/OwnAppointmentsList/OwnAppointmentsList";
import SpecialistMainPage from "../pages/protected/specialist/SpecialistMainPage";

const authorizedRoutes = (accessLevel: AccessLevelType) => {
    switch (accessLevel) {
        case "ADMINISTRATOR": {
            return (
                <>
                    <Route path="/accounts" element={<UserManagment />} />
                    <Route path="/accounts/:login" element={<UserDetails />} />
                    <Route
                        path="/accounts/:login/change-password"
                        element={<ChangeUserPassword />}
                    />
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
                <Route path="/visits" element={<OwnAppointmentsList/>}/>
                </>
            );
        }
        case "CLIENT": {
            return (
                <>
                <Route path="/visits" element={<OwnAppointmentsList/>}/>
                </>
            );
        }
        default: {
            return <></>;
        }
    }
};

export default authorizedRoutes;
