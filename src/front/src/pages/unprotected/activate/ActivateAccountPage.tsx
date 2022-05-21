import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useLocation } from "react-router-dom";
import { useConfirmRegistrationMutation } from "../../../api/api";
import styles from "./activeAccountPage.module.scss";
const ActivateAccountPage = () => {
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
      <div className={styles.text}>{t("insert_token")}</div>
      {token !== null ? (
        <>
          <input
            className={styles.token_input}
            value={token}
            onChange={(e) => setToken(e.target.value)}
          ></input>
          <div className={styles.confirm_button} onClick={handleSubmit}>
            {t("active_account")}
          </div>
        </>
      ) : (
        <></>
      )}

      {confirmed ? (
        <div>
          <div className={styles.text}> {t("confirmed_account")}</div>
          <div className={styles.confirm_button} onClick={() => navigate("/login")}>
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
