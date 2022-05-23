import styles from "./removeAccessLevelForm.module.scss";
import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useRemoveAccessLevelMutation } from "../../../api/api";
import { login as loginDispatch } from "../../../redux/userSlice";
import { useTranslation } from "react-i18next";
import { formGroupClasses } from "@mui/material";
import { AccessLevelName } from "../../../api/types/common";

interface LoginFormProps {
  eTag: string;
}

const RemoveAccessLevelForm = ({ eTag }: LoginFormProps) => {
  const [remove] = useRemoveAccessLevelMutation();
  const [accessLevel, setAccessLevel] = useState<AccessLevelName>(
    "CLIENT" as AccessLevelName
  );
  const [login, setLogin] = useState("");
  const [message, setMessage] = useState("");

  const { t } = useTranslation();

  const handleSubmit = async (event: any) => {
    event.preventDefault();

    if (login && eTag) {
      const res = await remove({
        login,
        eTag,
        accessLevel,
      });

      if ("error" in res && "status" in res.error) {
        if (res.error.status === "PARSING_ERROR")
          setMessage(res.error.data as string);
      }
    } else {
      setMessage(t("refill_data"));
    }
  };

  return (
    <div className={styles.login_left}>
      <div className={styles.title_text}>{t("remove_level")}</div>
      <div className={styles.input_box}>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="login"
            className={styles.form_field}
            name="login"
            id="login"
            value={login}
            onChange={(e) => setLogin(e.target.value)}
            required
          />
          <label className={styles.form_label}>Login</label>
        </div>
      </div>

      <div className={`${styles.form_group} ${styles.field}`}>
        <select
          value={accessLevel}
          onChange={(e) => setAccessLevel(e.target.value as AccessLevelName)}
        >
          <option value="CLIENT">CLIENT</option>
          <option value="ADMINISTRATOR">ADMINISTRATOR</option>
          <option value="SPECIALIST">SPECIALIST</option>
        </select>
      </div>

      <div className={styles.remove_button} onClick={handleSubmit}>
        {t("REMOVE")}
      </div>
      <div className={styles.message_text}>{message}</div>
    </div>
  );
};

export default RemoveAccessLevelForm;
