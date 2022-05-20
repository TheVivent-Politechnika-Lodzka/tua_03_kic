import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { logout } from "../../../redux/userSlice";
import "./style.scss";
import {Logout} from "../SelectorTopBar/SelectorTopBar";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import { useTranslation } from "react-i18next";

const AdminTopBar = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const {t} = useTranslation();
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
          onClick={() => navigate("/admin", { replace: false })}
        >
          ADMINPAGE
        </div>
        <div className="item">
          <LangSelect />
        </div>
          <Logout/>
      </div>
    </div>
  );
};

export default AdminTopBar;
