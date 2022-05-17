import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import LangSelect from "../../LangSelect/LangSelect";
import "./style.scss";

const GuestTopBar = () => {
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
        <div className="item">
          <LangSelect />
        </div>

        <div
          className="itembutton"
          onClick={() => navigate("/login", { replace: false })}
        >
          {t("log_in")}
        </div>
      </div>
    </div>
  );
};

export default GuestTopBar;
