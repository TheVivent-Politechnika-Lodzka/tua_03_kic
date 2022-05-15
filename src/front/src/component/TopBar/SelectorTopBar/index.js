import "./style.scss";
import { useStoreSelector } from "../../../redux/reduxHooks";
import AdminTopBar from "../AdminTopBar";
import SpecialistTopBar from "../SpecialistTopBar";
import ClientTopBar from "../ClientTopBar";
import GuestTopBar from "../GuestTopBar";

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

  if (user.sub === "administrator") {
    return <AdminTopBar />;
  }
  if (user.sub === "specialist") {
    return <SpecialistTopBar />;
  }
  if (user.sub === "client") {
    return <ClientTopBar />;
  } else {
    return <GuestTopBar />;
  }
};

export default TopBar;
