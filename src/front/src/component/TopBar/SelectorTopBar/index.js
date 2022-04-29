import "./style.scss";
import { useStoreSelector } from "../../../redux/reduxHooks";
import AdminTopBar from "../AdminTopBar";
import SpecialistTopBar from "../SpecialistTopBar";
import ClientTopBar from "../ClientTopBar";
import GuestTopBar from "../GuestTopBar";

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
