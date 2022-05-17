import { useNavigate } from "react-router-dom";
import "./style.scss";
import { Logout } from "../SelectorTopBar";
import LangSelect from "../../LangSelect/LangSelect";
import { useTranslation } from "react-i18next";

const ClientTopBar = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  return (
    <div className="topBar">
      <div className="logo" onClick={() => navigate("/", { replace: false })}>
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
      <div className="links">
        <div className="item">{t("cyber")}</div>
        <div className="item">{t("specialist")}</div>
        <div
          className="item"
          onClick={() => navigate("/client", { replace: false })}
        >
          CLIENTPAGE
        </div>
        <div className="item">
          <LangSelect />
        </div>
        <Logout />
      </div>
    </div>
  );
};

export default ClientTopBar;
