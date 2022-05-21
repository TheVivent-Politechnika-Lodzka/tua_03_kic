import styles from "./selectorTopBar.module.scss";
import { useStoreSelector } from "../../../redux/reduxHooks";
import AdminTopBar from "../AdminTopBar/AdminTopBar";
import SpecialistTopBar from "../SpecialistTopBar/SpecialistTopBar";
import ClientTopBar from "../ClientTopBar/ClientTopBar";
import GuestTopBar from "../GuestTopBar/GuestTopBar";
import { logout } from "../../../redux/userSlice";
import { useTranslation } from "react-i18next";

export const Logout = () => {
  const user = useStoreSelector((state) => state.user);
  const { t } = useTranslation();
  const logout = () => {
    localStorage.setItem("AUTH_TOKEN", "");
    window.location.reload();
  };
  return (
    <div className={styles.item} onClick={logout}>
      {t("log_out")}
    </div>
  );
};

const SelectorBar = () => {
  const user = useStoreSelector((state) => state.user);
  //TODO przemyśleć, czy powinniśmy mieć konta z wieloma poziomami dostępu, na chwilę obecną nav jest zrobiont tak,
  //ToDo że jeżeli jesteś tylko adminem wchodzisz w admina, a jeżeli masz masz specjaliste lub konto to nieważne, czy jesteś
  //TODO adminem i tak wejdziesz w konkretną opcję tylko.
  console.log(user);
  if (user.cur === "ADMINISTRATOR") {
    return <AdminTopBar />;
  }
  if (user.cur === "SPECIALIST") {
    return <SpecialistTopBar />;
  }
  if (user.cur === "CLIENT") {
    return <ClientTopBar />;
  } else {
    return <GuestTopBar />;
  }
};

export default SelectorBar;
