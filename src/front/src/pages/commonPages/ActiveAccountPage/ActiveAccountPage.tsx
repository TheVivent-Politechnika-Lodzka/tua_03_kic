import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { useActiveAccountMutation } from "../../../api/api";
import "./style.scss";
const ActiveAccountPage = () => {
  // TODO przerobic na pobranie tokenu z linku aktywacyjnego
  // let { token } = useParams();
  const [token, setToken] = useState("");
  const [confirm, { isLoading }] = useActiveAccountMutation();
  const [confirmed, setConfirmed] = useState(false);

  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleSubmit = async () => {
    if (token !== undefined) {
      await confirm({ token });
    }
    setConfirmed(true);
  };

  return (
    <div>
      <div className="text">{t("insert_token")}</div>
      <input
        className="token_input"
        value={token}
        onChange={(e) => setToken(e.target.value)}
      ></input>
      <div className="confirm_button" onClick={handleSubmit}>
        {t("active_account")}
      </div>
      {confirmed ? (
        <div>
          <div className="text"> {t("confirmed_account")}</div>
          <div className="confirm_button" onClick={() => navigate("/login")}>
            {t("nav_log")}
          </div>
        </div>
      ) : (
        <></>
      )}
    </div>
  );
};

export default ActiveAccountPage;
