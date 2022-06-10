import { faEdit, faKey } from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import AccessLevel from "../../../../component/shared/AccessLevel/AccessLevel";
import ActionButton from "../../../../component/shared/ActionButton/ActionButton";
import avatar from "../../../../assets/images/avatar.jpg";
import flagPL from "../../../../assets/images/PL.png";
import flagEN from "../../../../assets/images/EN.png";
import "./style.scss";
import { useEffect, useState } from "react";
import ReactLoading from "react-loading";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import { getOwnAccount } from "../../../../api";

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
        <section className="account-details-page">
            {loading.pageLoading ? (
                <ReactLoading
                    type="cylon"
                    color="#fff"
                    width="10rem"
                    height="10rem"
                />
            ) : (
                <div className="account-details-content">
                    <div
                        className="account-general-details"
                    >
                        <img
                            className="account-avatar"
                            src={avatar}
                            alt="Account general: Avatar"
                        />
                        <p className="account-login">{account?.login}</p>
                        <div className="account-access-levels-wrapper">
                            <p className="account-access-levels-title">
                                Poziomy dostępu:{" "}
                            </p>
                            {account?.accessLevels.map((accessLevel) => (
                                <AccessLevel
                                    clickable={true}
                                    key={accessLevel.level}
                                    accessLevel={accessLevel}
                                />
                            ))}
                        </div>
                        <div className="account-actions-wrapper">
                            <ActionButton
                                onClick={() => {
                                    navigate("/account/edit");
                                }}
                                title="Edytuj konto"
                                color="green"
                                icon={faEdit}
                            />
                            <ActionButton
                                title="Zmień hasło"
                                color="purple"
                                icon={faKey}
                            />
                        </div>
                    </div>
                    <div className="account-details">
                        <p className="details-title">Dane szczegółowe</p>
                        <div className="details-wrapper">
                            <div className="detail-wrapper">
                                <p className="detail-title">Imię:</p>
                                <p className="detail-description">
                                    {account?.firstName}
                                </p>
                            </div>
                            <div className="detail-wrapper">
                                <p className="detail-title">Nazwisko:</p>
                                <p className="detail-description">
                                    {account?.lastName}
                                </p>
                            </div>
                            <div className="detail-wrapper">
                                <p className="detail-title">Email:</p>
                                <p className="detail-description">
                                    {account?.email}
                                </p>
                            </div>
                            {accessLevel === "CLIENT" ? (
                                <div className="detail-wrapper">
                                    <p className="detail-title">Numer PESEL:</p>
                                    <p className="detail-description">
                                        {account?.accessLevels
                                            .filter(
                                                (level) =>
                                                    level.level === "CLIENT"
                                            )
                                            .map((level) => level.pesel)}
                                    </p>
                                </div>
                            ) : (
                                <div className="detail-wrapper">
                                    <p className="detail-title">
                                        Numer telefonu:
                                    </p>
                                    <p className="detail-description">
                                        {account?.accessLevels
                                            .filter(
                                                (level) =>
                                                    level.level === accessLevel
                                            )
                                            .map((level) => level.phoneNumber)}
                                    </p>
                                </div>
                            )}
                            <div className="detail-wrapper">
                                <p className="detail-title">Język:</p>
                                <img
                                    src={
                                        account?.language?.language === "pl"
                                            ? flagPL
                                            : flagEN
                                    }
                                    alt="Detail description: language"
                                    className="detail-flag"
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
