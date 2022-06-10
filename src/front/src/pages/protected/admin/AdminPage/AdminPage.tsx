import styles from "./adminPage.module.scss";
import { useEffect, useState } from "react";
import { useGetOwnAccountDetailsWorkaroundMutation } from "../../../../api/api";
import { AccountWithAccessLevelsDto } from "../../../../api/types/apiParams";

const AdminPage = () => {
    const [accountDetails, setAccountDetails] =
        useState<AccountWithAccessLevelsDto>();
    // TODO: zmienić, żeby nie korzystało z WorkaroundMutation (jest jakiś problem, że Query nie działa,
    //  tzn, robi zapytanie, w zakładce network widać, że jest ok, ale funkcja zwraca undefined)
    const [getAccountDetails, { isLoading }] =
        useGetOwnAccountDetailsWorkaroundMutation();

    useEffect(() => {
        getAccountDetails().then((res) => {
            if ("data" in res) {
                setAccountDetails(res.data);
            }
        });
    }, []);

    return (
        <div className={styles.whiteText}>
            <h1>Admin Page</h1>
            <p>eTag: {accountDetails?.ETag}</p>
            <p>imię: {accountDetails?.firstName}</p>
            <p>nazwisko: {accountDetails?.lastName}</p>
            {/*<p>email: {accountDetails?.email}</p>*/}
            <p>
                accessLevels:{" "}
                {accountDetails?.accessLevels
                    .map((accessLevel) => accessLevel.level)
                    .join(", ")}
            </p>
        </div>
    );
};

export default AdminPage;
