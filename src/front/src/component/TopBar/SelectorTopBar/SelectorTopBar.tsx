import styles from "./selectorTopBar.module.scss";
import { useStoreDispatch, useStoreSelector } from "../../../redux/reduxHooks";
import AdminTopBar from "../AdminTopBar/AdminTopBar";
import SpecialistTopBar from "../SpecialistTopBar/SpecialistTopBar";
import ClientTopBar from "../ClientTopBar/ClientTopBar";
import GuestTopBar from "../GuestTopBar/GuestTopBar";
import { logout as logoutDispatch } from "../../../redux/userSlice";
import { useTranslation } from "react-i18next";


export const Logout = () => {
  const { t } = useTranslation();
  const dispatch = useStoreDispatch();
  const logout = () => {
    localStorage.setItem("AUTH_TOKEN", "");
    dispatch(logoutDispatch());
    window.location.reload();
  };
  return (
    <div className={styles.item} onClick={logout}>
      {t("log_out")}
    </div>
  );
};

const SelectorBar = () => {
  const user = useStoreSelector((state) => state.user.cur);

  
  if (user === "ADMINISTRATOR") {
    return <AdminTopBar />;
  }
  if (user === "SPECIALIST") {
    return <SpecialistTopBar />;
  }
  if (user === "CLIENT") {
    return <ClientTopBar />;
  } else {
    return <GuestTopBar />;
  }
};

export default SelectorBar;
