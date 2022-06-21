import { showNotification } from "@mantine/notifications";
import { useEffect } from "react";
import { useTranslation } from "react-i18next";
import ReactLoading from "react-loading";
import { useNavigate, useParams } from "react-router-dom";
import { activateAccount, confirmRegistration } from "../../../api";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../utils/showNotificationsItems";
import style from "./activeAccountPage.module.scss";

const ActivateAccountPage = () => {
    const { token } = useParams();
    const navigate = useNavigate();
    const { t } = useTranslation();

    const handleSubmit = async () => {
        if (!token) return;
        const response = await confirmRegistration(token);
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            navigate("/");
            return;
        }
        showNotification(
            successNotficiationItems(t("activateAccountPage.accountActivated"))
        );
        navigate("/login");
    };

    useEffect(() => {
        handleSubmit();
    }, [token]);

    return (
        <div className={style.loader}>
            <ReactLoading
                type="cylon"
                color="#fff"
                width="10rem"
                height="10rem"
            />
        </div>
    );
};

export default ActivateAccountPage;
