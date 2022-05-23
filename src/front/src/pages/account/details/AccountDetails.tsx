import { TextField } from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import {
  useEditOwnAccountMutation,
  useGetOwnAccountDetailsWorkaroundMutation,
} from "../../../api/api";
import { AccountWithAccessLevelsDto } from "../../../api/types/apiParams";
import style from "./accountDetails.module.scss";

const AccountDetailsPage = () => {
  const navigate = useNavigate();
  const [accountDetails, setAccountDetails] =
    useState<AccountWithAccessLevelsDto>({
      firstName: "",
      lastName: "",
      email: "",
      accessLevels: [],
      login: "",
      ETag: "",
      createdAt: "",
      lastModified: "",
      active: false,
      confirmed: false,
      language: {
        language: "",
      },
      captcha: "",
    });
  // TODO: zmienić, żeby nie korzystało z WorkaroundMutation (jest jakiś problem, że Query nie działa,
  //  tzn, robi zapytanie, w zakładce network widać, że jest ok, ale funkcja zwraca undefined)
  const [getAccountDetails, { isLoading }] =
    useGetOwnAccountDetailsWorkaroundMutation();
  const [editAccount] = useEditOwnAccountMutation();
  const [editMode, setEditMode] = useState(false);

  useEffect(() => {
    getAccountDetails().then((res) => {
      if ("data" in res) {
        setAccountDetails(res.data);
      }
    });
  }, []);

  const handleSubmit = async () => {
    const res = await editAccount(accountDetails);
    if ("data" in res) {
      setAccountDetails(res.data);
      alert("zapisane");
    }
  };

  return (
    <div className={style.whiteText}>
      <h1>Account Details Page</h1>
      <button onClick={() => setEditMode((old) => !old)}>edycja</button>
      <p>eTag: {accountDetails?.ETag}</p>
      <p>
        imię:{" "}
        {editMode ? (
          <input
            value={accountDetails?.firstName}
            onChange={(e) =>
              setAccountDetails((old) => ({
                ...old,
                firstName: e.target.value,
              }))
            }
          />
        ) : (
          accountDetails?.firstName
        )}
      </p>
      <p>
        nazwisko:{" "}
        {editMode ? (
          <input
            value={accountDetails?.lastName}
            onChange={(e) =>
              setAccountDetails((old) => ({
                ...old,
                lastName: e.target.value,
              }))
            }
          />
        ) : (
          accountDetails?.lastName
        )}
      </p>
      <p>email: {accountDetails?.email}</p>
      <p>
        accessLevels:{" "}
        {accountDetails?.accessLevels
          .map((accessLevel) => accessLevel.level)
          .join(", ")}
      </p>
      <p>login: {accountDetails?.login}</p>
      <p>język: {accountDetails?.language.language}</p>
      <button onClick={handleSubmit}>zapisz</button>
    </div>
  );
};

export default AccountDetailsPage;
