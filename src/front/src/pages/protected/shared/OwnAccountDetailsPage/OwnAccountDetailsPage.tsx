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
import ChangeOwnPasswordPage from "../ChangeOwnPasswordPage/ChangeOwnPasswordPage";
import { useTranslation } from "react-i18next";

const OwnAccountDetailsPage = () => {
    const [account, setAccount] = useState<AccountDetails>();
    const [loading, setLoading] = useState<Loading>({ pageLoading: true });
    const [error, setError] = useState<ApiError>();
    const [opened, setOpened] = useState<boolean>(false);

    const accessLevel = useStoreSelector((state) => state.user.cur);
    const navigate = useNavigate();

    const { t } = useTranslation();

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
        <section className={styles.own_account_details_page}>
            {loading.pageLoading ? (
                <ReactLoading
                    type="bars"
                    color="#fff"
                    width="10rem"
                    height="10rem"
                />
            ) : (
                <>
                    <div className={styles.content}>
                        <div className={styles.account_general_details}>
                            <img
                                className={styles.avatar}
                                src={avatar}
                                alt={t("ownAccountDetailsPage.alt")}
                            />
                            <p className={styles.login}>{account?.login}</p>
                            <div
                                className={styles.account_access_levels_wrapper}
                            >
                                <p className={styles.title}>
                                    {t("ownAccountDetailsPage.accessLevel")}{" "}
                                </p>
                                {account?.accessLevels.map((accessLevel) => (
                                    <AccessLevel
                                        clickable
                                        selectable
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
                                    title={t("ownAccountDetailsPage.edit")}
                                    color="green"
                                    icon={faEdit}
                                />
                                <ActionButton
                                    onClick={() => {
                                        setOpened(true);
                                    }}
                                    title={t("ownAccountDetailsPage.change")}
                                    color="purple"
                                    icon={faKey}
                                />
                            </div>
                        </div>
                        <div className={styles.account_details}>
                            <p className={styles.title}>
                                {t("ownAccountDetailsPage.title")}
                            </p>
                            <div className={styles.details_wrapper}>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.field_title}>
                                        {t("ownAccountDetailsPage.name")}
                                    </p>
                                    <p className={styles.field_description}>
                                        {account?.firstName}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.field_title}>
                                        {t("ownAccountDetailsPage.surname")}
                                    </p>
                                    <p className={styles.field_description}>
                                        {account?.lastName}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.field_title}>
                                        {t("ownAccountDetailsPage.email")}
                                    </p>
                                    <p className={styles.field_description}>
                                        {account?.email}
                                    </p>
                                </div>
                                {accessLevel === "CLIENT" ? (
                                    <div className={styles.detail_wrapper}>
                                        <p className={styles.field_title}>
                                            {t("ownAccountDetailsPage.pesel")}
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
                                            {t("ownAccountDetailsPage.phone")}
                                        </p>
                                        <p className={styles.field_description}>
                                            {account?.accessLevels
                                                .filter(
                                                    (level) =>
                                                        level.level ===
                                                        accessLevel
                                                )
                                                .map(
                                                    (level) => level.phoneNumber
                                                )}
                                        </p>
                                    </div>
                                )}
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.field_title}>
                                        {t("ownAccountDetailsPage.language")}
                                    </p>
                                    <img
                                        src={
                                            account?.language?.language === "pl"
                                                ? flagPL
                                                : flagEN
                                        }
                                        alt={t(
                                            "ownAccountDetailsPage.altLanguage"
                                        )}
                                        className={styles.flag}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                    <ChangeOwnPasswordPage
                        isOpen={opened}
                        onClose={() => {
                            setOpened(false);
                        }}
                    />
                </>
            )}
        </section>
    );
};

export default OwnAccountDetailsPage;
