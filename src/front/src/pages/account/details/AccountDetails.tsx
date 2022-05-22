import { useNavigate } from "react-router";
import style from "./accountDetails.module.scss";

const AccountDetailsPage = () => {
  const navigate = useNavigate();
  return (
    <div className={style.whiteText}>
      <h1>Account Details Page</h1>
      <button onClick={() => navigate("edit")}>przejdź do edycji</button>
    </div>
  );
};

export default AccountDetailsPage;
