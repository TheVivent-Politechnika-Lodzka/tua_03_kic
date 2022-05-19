import "./style.scss";
import { FaRobot } from "react-icons/fa";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import LangSelect from "../../../component/LangSelect/LangSelect";

const ErrorPage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  return (
    <div>
      <div
        onClick={() => {
          navigate("/");
        }}
      >
        <img src="./../logo.jpg" alt="Logo" height="80px" />
      </div>
      <LangSelect />
      <div className="errorbox">
        <div className="errortext">
          <FaRobot size="50px" className="icon" />
          {t("error_page")}
        </div>

        <div className="linktext">
          {t("invite_main_page")}

          <div className="button" onClick={() => navigate("/")}>
            {t("main_page")}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ErrorPage;
