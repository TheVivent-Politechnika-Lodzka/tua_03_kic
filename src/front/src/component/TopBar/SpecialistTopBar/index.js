import { useNavigate } from "react-router-dom";
import "./style.scss";
import { Logout } from "../SelectorTopBar/SelectorTopBar";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import { useTranslation } from "react-i18next";
import SpecialistNavBar from "../../NavBar/SpecialistNavBar/SpecialistNavBar";

const SpecialistTopBar = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  return (
    <div className="topBar">
      <div className="logo" onClick={() => navigate("/", { replace: false })}>
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
      <div className="links">
        <div className="item">{t("cyber")}</div>
        <div className="item">
          <SpecialistNavBar />
        </div>
        <div
          className="item"
          onClick={() => navigate("/specialist", { replace: false })}
        >
          SPECIALISTPAGE
        </div>
        <div className="item">
          <LangSelect />
        </div>
        <Logout />
      </div>
    </div>
  );
};

export default SpecialistTopBar;
