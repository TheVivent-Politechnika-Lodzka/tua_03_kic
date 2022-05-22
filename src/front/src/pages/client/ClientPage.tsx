import { useEffect, useState } from "react";
import { useFindAllUsersMutation } from "../../api/api";
import {
  AccountWithAccessLevelsDto,
  AccountWithAccessLevelsDtoWithLogin,
} from "../../api/types/apiParams";
import styles from "./clientPage.module.scss";

interface ViewAccount {
  login: string;
  accessLevels: string[];
  email: string;
  active: boolean;
}

const ClientPage = () => {
  const [account, setAccounts] = useState<ViewAccount[]>([]);
  const [findAll] = useFindAllUsersMutation();

  useEffect(() => {
    getAccounts();
  }, []);

  const getAccounts = async () => {
    const data = await findAll({ page: 1, limit: 3 });
    let accounts;
    if ("data" in data) {
      // wygląda paskudnie, ale na razie zostawmy
      accounts = data.data?.data?.map(
        ({
          login,
          accessLevels,
          email,
          isActive,
        }: AccountWithAccessLevelsDto): ViewAccount => {
          return {
            login: login,
            accessLevels: ["test", "nowe"],
            email: email,
            active: true,
          };
        }
      );
      setAccounts(accounts);
    }
  };

  return (
    <div className={styles.pageLayout}>
      <h2>Users</h2>
      <table>
        <thead>
          <tr>
            <th>Login</th>
            <th>Poziomy dostępu</th>
            <th>Email</th>
            <th>Czy aktywny</th>
          </tr>
        </thead>
        <tbody>
          {account?.map(({ login, accessLevels, email, active }) => {
            console.log(active);
            return (
              <tr key={email}>
                <td>{login}</td>
                <td>{accessLevels}</td>
                <td>{email}</td>
                <td>{active ? "Aktywny" : "Nieaktywny"}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default ClientPage;
