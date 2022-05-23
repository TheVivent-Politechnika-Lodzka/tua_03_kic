import { SerializedError } from "@reduxjs/toolkit";
import { FetchBaseQueryError } from "@reduxjs/toolkit/dist/query";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router";
import {
  useChangeAccountPasswordMutation,
  useGetAccountByLoginMutation,
} from "../../../api/api";
import { ChangePasswordDto } from "../../../api/types/apiParams";
import styles from "./changeUserPassword.module.scss";

const ChangeUserPassword = () => {
  const navigate = useNavigate();
  const { login } = useParams();
  const [newPassword, setNewPassword] = useState<string>("");
  const [accountData, setAccountData] = useState<ChangePasswordDto>({
    login: login as string,
    data: {
      ETag: "",
      newPassword: "",
    },
  });
  const [getAccount] = useGetAccountByLoginMutation();
  const [changePassword] = useChangeAccountPasswordMutation();
  const {t} = useTranslation();

  const getEtag = async () => {
    const result = await getAccount(login as string);
    if ("data" in result) {
      setAccountData({
        ...accountData,
        data: {
          ...accountData.data,
          ETag: result?.data?.ETag,
        },
      });
    }
  };

  useEffect(() => {
    getEtag();
  }, []);

  return (
    <div className={styles.changeUserPasswordPage}>
      <h2>{t("ChangeAccountPassword.title")} {login}</h2>
      <div className={styles.inputWrapper}>
        <input
          onChange={(e) => {
            setNewPassword(e.target.value);
            setAccountData({
              ...accountData,
              data: {
                ...accountData.data,
                newPassword: e.target.value,
              },
            });
          }}
          value={newPassword}
          type="password"
          placeholder={t("ChangeAccountPassword.input_placeholder")}
          className={styles.changePasswordInput}
        />
        <button
          onClick={() => {
            if (newPassword.length >= 8) {
              changePassword(accountData)
                .then((res) => console.log(res))
                .catch((err) => console.log(err));
            }
          }}
          className={styles.submitButton}
        >
          {t("ChangeAccountPassword.change_password_button")}
        </button>
      </div>
      {newPassword !== "" && newPassword.length < 8 && (
        <p className={styles.error}>{t("ChangeAccountPassword.too_short_password_error")}</p>
      )}
      <button
        onClick={() => {
          navigate("/admin/users");
        }}
        className={styles.submitButton}
      >
        {t("ChangeAccountPassword.go_back_button")}
      </button>
    </div>
  );
};

export default ChangeUserPassword;
