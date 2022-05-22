import AccessLevelSelect from "../../Selector/AccessLevelSelect/AccessLevelSelect";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import styles from "./detailsTopBar.module.scss";

const DetailsTopBar = () => {
  return (
    <div className="topBar">
      <div className={styles.right_side}>
        <AccessLevelSelect/>
        <div className={styles.account}>Imie nazwisko</div>
        <LangSelect />
      </div>
    </div>
  );
};
export default DetailsTopBar;
