import "./style.scss";
import { FaRobot } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import LangSelect from "../../../component/Selector/LangBarSelect/LangSelect";
import Logo from "../../../component/Logo/Logo";

const ErrorPage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  return (
    <div>
      <Logo />
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
