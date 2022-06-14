import { useEffect, useState } from "react";
import { useStoreSelector } from "../../../redux/reduxHooks";
import AccessLevelSelect from "../../Selector/AccessLevelSelect/AccessLevelSelect";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import styles from "./subpageTopBar.module.scss";
import "./subpage.scss";

const SubpageTopBar = () => {
  const user = useStoreSelector((state) => state.user.cur);
  const [color, setColor] = useState("topBar_default");

  useEffect(() => {
    if (user === "SPECIALIST") {
      setColor("topBar_spec");
    }
    if (user === "CLIENT") {
      setColor("topBar_user");
    }
    if (user === "ADMINISTRATOR") {
      setColor("topBar_admin");
    }
  }, []);

  return (
    <div className={`${styles.topBar} ${color}`}>
      <AccessLevelSelect />
      <div className={styles.account}>{user}</div>
      <LangSelect />
    </div>
  );
};
export default SubpageTopBar;
