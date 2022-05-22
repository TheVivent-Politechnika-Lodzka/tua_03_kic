import style from "./accountDetails.module.scss";
import { useState, useEffect } from "react";
import { useGetOwnAccountDetailsWorkaroundMutation } from "../../../api/api";
import { AccountWithAccessLevelsDto } from "../../../api/types/apiParams";

const EditAccountDetailsPage = () => {
  const [getCurrentUser, { isLoading }] =
    useGetOwnAccountDetailsWorkaroundMutation();
  const [currentUserData, setCurrentUserData] =
    useState<AccountWithAccessLevelsDto>({} as AccountWithAccessLevelsDto);

  useEffect(() => {
    getAccountDetails();
  }, []);

  const getAccountDetails = async () => {
    const res = await getCurrentUser();
    if ("data" in res) {
      setCurrentUserData(res.data);
    }
  };

  return (
    <div className={style.whiteText}>
      <h1>Edit Account Details Page</h1>
      <div>
        <div>
          imię: <input value={currentUserData.firstName} />
          nazwisko: <input value={currentUserData.lastName} />
        </div>
      </div>
    </div>
  );
};

export default EditAccountDetailsPage;
