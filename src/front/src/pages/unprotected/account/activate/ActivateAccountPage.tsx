import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useLocation } from "react-router-dom";
import { useConfirmRegistrationMutation } from "../../../../api/api";
import "./style.scss";
const ActivateAccountPage = () => {
  // TODO przerobic na pobranie tokenu z linku aktywacyjnego
  // let { token } = useParams();

  const search = useLocation().search;
  const querytoken = new URLSearchParams(search).get("token");

  const [token, setToken] = useState(querytoken);
  const [confirm, { isLoading }] = useConfirmRegistrationMutation();
  const [confirmed, setConfirmed] = useState(false);

  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleSubmit = async () => {
    if (token !== null) {
      await confirm({ token });
    }
    setConfirmed(true);
  };

  return (
    <div>
      <div className="text">{t("insert_token")}</div>
      {token !== null ? (
        <>
          <input
            className="token_input"
            value={token}
            onChange={(e) => setToken(e.target.value)}
          ></input>
          <div className="confirm_button" onClick={handleSubmit}>
            {t("active_account")}
          </div>
        </>
      ) : (
        <></>
      )}

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

export default ActivateAccountPage;
