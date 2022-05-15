import "./style.scss";
import { useStoreSelector } from "../../../redux/reduxHooks";
import AdminTopBar from "../AdminTopBar";
import SpecialistTopBar from "../SpecialistTopBar";
import ClientTopBar from "../ClientTopBar";
import GuestTopBar from "../GuestTopBar";
import {logout} from "../../../redux/userSlice";

export const Logout = () => {
  const user = useStoreSelector(state => state.user);
  const logout = () => {
      localStorage.setItem("AUTH_TOKEN", "");
      window.location.reload();
  };
  return (
    <div className="item" onClick={logout}>
        WYLOGUJ
    </div>
  );
};

const TopBar = () => {
  const user = useStoreSelector((state) => state.user);
  //TODO przemyśleć, czy powinniśmy mieć konta z wieloma poziomami dostępu, na chwilę obecną nav jest zrobiont tak,
  //ToDo że jeżeli jesteś tylko adminem wchodzisz w admina, a jeżeli masz masz specjaliste lub konto to nieważne, czy jesteś
  //TODO adminem i tak wejdziesz w konkretną opcję tylko.
  console.log(user);
  if (user.auth === "ADMINISTRATOR") {
    return <AdminTopBar />;
  }
  if (user.auth.includes("SPECIALIST")) {
    return <SpecialistTopBar />;
  }
  if (user.auth.includes("CLIENT")) {
    return <ClientTopBar />;
  } else {
    return <GuestTopBar />;
  }
};

export default TopBar;
