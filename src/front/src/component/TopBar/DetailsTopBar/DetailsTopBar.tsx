import { useStoreSelector } from "../../../redux/reduxHooks";
import AccessLevelSelect from "../../Selector/AccessLevelSelect/AccessLevelSelect";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import styles from "./detailsTopBar.module.scss";

const DetailsTopBar = () => {
  const user = useStoreSelector((state) => state.user.cur);
  return (
    <div className={styles.topBar}>
      <AccessLevelSelect />
      <div className={styles.account}>{user}</div>
      <LangSelect />
    </div>
  );
};
export default DetailsTopBar;
