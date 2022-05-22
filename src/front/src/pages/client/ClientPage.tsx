import { ChangeEvent, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useFindAllUsersMutation } from "../../api/api";
import { AccountWithAccessLevelsDto } from "../../api/types/apiParams";
import styles from "./clientPage.module.scss";

interface ViewAccount {
  login: string;
  firstName: string;
  lastName: string;
  accessLevels: string[];
  email: string;
  active: boolean;
}

const ClientPage = () => {
  const [account, setAccounts] = useState<ViewAccount[]>([]);
  const [query, setQuery] = useState<string>("");
  const [findAll] = useFindAllUsersMutation();
  const { t } = useTranslation();


  useEffect(() => {
    getAccounts();
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => {
      getAccounts();
    }, 500);
    return () => clearTimeout(timer);
  }, [query]);

  const getAccounts = async () => {
    const data = await findAll({ page: 1, limit: 3, phrase: query });
    let accounts;
    if ("data" in data) {
      // wygląda paskudnie, ale na razie zostawmy
      accounts = data.data?.data?.map(
        ({
          login,
          accessLevels,
          email,
          active,
          firstName,
          lastName,
        }: AccountWithAccessLevelsDto): ViewAccount => {
          return {
            firstName: firstName,
            lastName: lastName,
            login: login,
            accessLevels: accessLevels.map(({ level }) => {
              return `${level}\n`;
            }),
            email: email,
            active: active,
          };
        }
      );
      setAccounts(accounts);
    }
  };

  const handleQuery = (e: ChangeEvent<HTMLInputElement>) => {
    setQuery(e.target.value);
  };

  return (
    <div className={styles.pageLayout}>
      <h2>{t("users_administration_page")}</h2>
      <input
        type="text"
        className={styles.searchInput}
        placeholder="Wyszukaj po frazie"
        onChange={handleQuery}
        value={query}
      />
      <table className={styles.table}>
        <thead>
          <tr>
            <th>{t("header_login")}</th>
            <th>{t("header_first_name")}</th>
            <th>{t("header_last_name")}</th>
            <th>{t("header_level_access_level")}</th>
            <th>{t("header_email")}</th>
            <th>{t("header_is_active")}</th>
            <th>{t("header_change_password")}</th>
          </tr>
        </thead>
        <tbody>
          {account?.map(
            ({ login, firstName, lastName, accessLevels, email, active }) => {
              console.log(active);
              return (
                <tr key={email}>
                  <td>{login}</td>
                  <td>{firstName}</td>
                  <td>{lastName}</td>
                  <td>{accessLevels}</td>
                  <td>{email}</td>
                  <td>{active ? t("cell_active") : t("cell_inactive")}</td>
                  <td>
                    <button className={styles.button}>Zmień hasło</button>
                  </td>
                </tr>
              );
            }
          )}
        </tbody>
      </table>
    </div>
  );
};

export default ClientPage;
