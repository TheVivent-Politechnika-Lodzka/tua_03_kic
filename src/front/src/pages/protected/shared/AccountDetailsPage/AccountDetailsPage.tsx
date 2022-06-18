import { faEdit, faKey } from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import AccessLevel from "../../../../components/shared/AccessLevel/AccessLevel";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import avatar from "../../../../assets/images/avatar.jpg";
import flagPL from "../../../../assets/images/PL.png";
import flagEN from "../../../../assets/images/EN.png";
import { useEffect, useState } from "react";
import ReactLoading from "react-loading";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import { getOwnAccount } from "../../../../api";
import styles from "./style.module.scss";
import { style } from "@mui/system";

const AccountDetailsPage = () => {
    const [account, setAccount] = useState<AccountDetails>();
    const [loading, setLoading] = useState<Loading>({ pageLoading: true });
    const [error, setError] = useState<ApiError>();

    const accessLevel = useStoreSelector((state) => state.user.cur);
    const navigate = useNavigate();

    const handleGetOwnAccount = async () => {
        try {
            const data = await getOwnAccount();
            setAccount(data as AccountDetails);
            setLoading({ pageLoading: false });
        } catch (error: ApiError | any) {
            setLoading({ pageLoading: false });
            setError(error);
            console.error(`${error?.status} ${error?.errorMessage}`);
        }
    };

    useEffect(() => {
        handleGetOwnAccount();
    }, []);

    useEffect(() => {}, [accessLevel]);

    return (
        <section className={styles.account_details_page}>
            {loading.pageLoading ? (
                <ReactLoading
                    type="cylon"
                    color="#fff"
                    width="10rem"
                    height="10rem"
                />
            ) : (
                <div className={styles.content}>
                    <div className={styles.account_general_details}>
                        <img
                            className={styles.avatar}
                            src={avatar}
                            alt="Account general: Avatar"
                        />
                        <p className={styles.login}>{account?.login}</p>
                        <div className={styles.account_access_levels_wrapper}>
                            <p className={styles.title}>Poziomy dostępu: </p>
                            {account?.accessLevels.map((accessLevel) => (
                                <AccessLevel
                                    clickable={true}
                                    selectable={true}
                                    key={accessLevel.level}
                                    accessLevel={accessLevel.level}
                                />
                            ))}
                        </div>
                        <div className={styles.account_actions_wrapper}>
                            <ActionButton
                                onClick={() => {
                                    navigate("/account/edit");
                                }}
                                title="Edytuj konto"
                                color="green"
                                icon={faEdit}
                            />
                            <ActionButton
                                onClick={() => {}}
                                title="Zmień hasło"
                                color="purple"
                                icon={faKey}
                            />
                        </div>
                    </div>
                    <div className={styles.account_details}>
                        <p className={styles.title}>Dane szczegółowe</p>
                        <div className={styles.details_wrapper}>
                            <div className={styles.detail_wrapper}>
                                <p className={styles.field_title}>Imię:</p>
                                <p className={styles.field_description}>
                                    {account?.firstName}
                                </p>
                            </div>
                            <div className={styles.detail_wrapper}>
                                <p className={styles.field_title}>Nazwisko:</p>
                                <p className={styles.field_description}>
                                    {account?.lastName}
                                </p>
                            </div>
                            <div className={styles.detail_wrapper}>
                                <p className={styles.field_title}>Email:</p>
                                <p className={styles.field_description}>
                                    {account?.email}
                                </p>
                            </div>
                            {accessLevel === "CLIENT" ? (
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.field_title}>
                                        Numer PESEL:
                                    </p>
                                    <p className={styles.field_description}>
                                        {account?.accessLevels
                                            .filter(
                                                (level) =>
                                                    level.level === "CLIENT"
                                            )
                                            .map((level) => level.pesel)}
                                    </p>
                                </div>
                            ) : (
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.field_title}>
                                        Numer telefonu:
                                    </p>
                                    <p className={styles.field_description}>
                                        {account?.accessLevels
                                            .filter(
                                                (level) =>
                                                    level.level === accessLevel
                                            )
                                            .map((level) => level.phoneNumber)}
                                    </p>
                                </div>
                            )}
                            <div className={styles.detail_wrapper}>
                                <p className={styles.field_title}>Język:</p>
                                <img
                                    src={
                                        account?.language?.language === "pl"
                                            ? flagPL
                                            : flagEN
                                    }
                                    alt="Detail description: language"
                                    className={styles.flag}
                                />
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </section>
    );
};

export default AccountDetailsPage;
