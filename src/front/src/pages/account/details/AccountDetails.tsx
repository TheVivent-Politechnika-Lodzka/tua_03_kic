import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { useGetOwnAccountDetailsWorkaroundMutation } from "../../../api/api";
import { AccountWithAccessLevelsDto } from "../../../api/types/apiParams";
import style from "./accountDetails.module.scss";

const AccountDetailsPage = () => {
  const navigate = useNavigate();
  const [accountDetails, setAccountDetails] = useState<AccountWithAccessLevelsDto>();
  // TODO: zmienić, żeby nie korzystało z WorkaroundMutation (jest jakiś problem, że Query nie działa,
  //  tzn, robi zapytanie, w zakładce network widać, że jest ok, ale funkcja zwraca undefined)
  const [getAccountDetails, {isLoading}] = useGetOwnAccountDetailsWorkaroundMutation();

  useEffect(() => {
    getAccountDetails().then((res) => {
      if ("data" in res) {
        setAccountDetails(res.data);
      }
    });
  }, []);
  return (
    <div className={style.whiteText}>
      <h1>Account Details Page</h1>
      <button onClick={() => navigate("edit")}>przejdź do edycji</button>
      <h1>Admin Page</h1>
      <p>eTag: {accountDetails?.ETag}</p>
      <p>imię: {accountDetails?.firstName}</p>
      <p>nazwisko: {accountDetails?.lastName}</p>
      <p>email: {accountDetails?.email}</p>
      <p>accessLevels: {accountDetails?.accessLevels.map((accessLevel) => accessLevel.level).join(", ")}</p>
      <p>login: {accountDetails?.login}</p>
      <p>język: {accountDetails?.language.language}</p>
    </div>
  );
};

export default AccountDetailsPage;
