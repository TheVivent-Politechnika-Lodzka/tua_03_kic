import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import ImplantNavBar from "../../NavBar/ImplantNavBar/ImplantNavBar";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import SpecialistNavBar from "../../NavBar/SpecialistNavBar/SpecialistNavBar";
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
        <div className="item">
          <ImplantNavBar />
        </div>
        <div className="item">
          <SpecialistNavBar />
        </div>
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
