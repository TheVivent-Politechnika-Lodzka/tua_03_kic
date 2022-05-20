import { useNavigate } from "react-router-dom";
import "./style.scss";
import { Logout } from "../SelectorTopBar/SelectorTopBar";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import { useTranslation } from "react-i18next";
import ImplantNavBar from "../../NavBar/ImplantNavBar/ImplantNavBar";
import SpecialistNavBar from "../../NavBar/SpecialistNavBar/SpecialistNavBar";
import AppointmentNavBar from "../../NavBar/AppointmentNavBar/appointmentNavBar";

const ClientTopBar = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  return (
    <div className="topBar">
      <div className="logo" onClick={() => navigate("/", { replace: false })}>
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
      <div className="links">
        <div className="item">
          <AppointmentNavBar />
        </div>
        <div className="item">
          <ImplantNavBar />
        </div>
        <div className="item">
          <SpecialistNavBar />
        </div>
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
