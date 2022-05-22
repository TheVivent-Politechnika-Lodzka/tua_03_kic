import { useEffect } from "react";
import {
  useFindAllUsersMutation,
  useGetOwnAccountDetailsQuery,
} from "../../../api/api";
import styles from "./adminPage.module.scss";

const AdminPage = () => {
  // TODO: nie działa. Wyrzuca 401, ale w postmanie podobne zapytanie działa. Do naprawy
  const [findAllUsers] = useFindAllUsersMutation();

  const test = async () => {
    const result = await findAllUsers({ page: 1, limit: 2 });
    return result;
  };

  useEffect(() => {
    // wywołaanie dwóch metod w celu sprawdzenia, czy żądania omijają CORS
    //TODO: po przetestowaniu usunąć!

    test()
      .then((data) => console.log(data))
      .catch((err) => console.log(err.message));

    fetch("https://localhost:8181/api/mok/ping")
      .then((res) => {
        return res.text();
      })
      .then((data) => console.log(data))
      .catch((err) => console.log(err.message));
  }, []);

  return (
    <div className={styles.whiteText}>
      <>
        <h1>Admin Page</h1>
        <p>imię: Marceliusz</p>
      </>
    </div>
  );
};

export default AdminPage;
