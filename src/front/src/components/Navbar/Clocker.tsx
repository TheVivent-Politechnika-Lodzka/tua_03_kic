import { useEffect, useState } from "react";
import jwtDecode from "jwt-decode";
import Countdown from "react-countdown";
import ActionButton from "../shared/ActionButton/ActionButton";
import { faCheck } from "@fortawesome/free-solid-svg-icons";
import { refreshToken } from "../../api";
import { login as loginDispatch, logout } from "../../redux/userSlice";
import { useDispatch } from "react-redux";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router";
import { failureNotificationItems } from "../../utils/showNotificationsItems";
import { showNotification } from "@mantine/notifications";

interface jwt {
    sub: string;
    exp: number;
}

const Clocker = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [time, setTime] = useState<number>(0);
    const accessJWT = "ACCESS_TOKEN";
    const refreshJWT = "REFRESH_TOKEN";
    const { t } = useTranslation();

    const handleSubmit = async () => {
        if (localStorage.getItem(refreshJWT)) {
            const response = await refreshToken(
                localStorage.getItem(refreshJWT) as string
            );
            if ("errorMessage" in response) {
                showNotification(
                    failureNotificationItems(t("clocker.logInAgain"))
                );
                localStorage.removeItem(accessJWT);
                localStorage.removeItem(refreshJWT);
                dispatch(logout());
                navigate("/");
                window.location.reload();
                return;
            }

            const decodedJWT = jwtDecode(response.accessToken) as string;
            dispatch(loginDispatch(decodedJWT));
            localStorage.setItem(accessJWT, response.accessToken);
            localStorage.setItem(refreshJWT, response.refreshToken);
            getTime();
        }
    };

    const getTime = () => {
        if (localStorage.getItem(accessJWT)) {
            const token = localStorage.getItem(accessJWT);
            if (token) {
                const decodedJWT: jwt = jwtDecode(token);
                setTime(decodedJWT.exp);
            }
        }
    };

    useEffect(() => {
        getTime();
    }, [time]);

    return (
        <Countdown
            date={new Date(time * 1000 - 600000)}
            renderer={({ minutes, seconds, completed }) => {
                if (completed) {
                    return (
                        <ActionButton
                            onClick={handleSubmit}
                            icon={faCheck}
                            color="purple"
                            title={t("clocker.refresh")}
                        />
                    );
                } else {
                    return (
                        <span>
                            {minutes
                                ? minutes < 10
                                    ? "0" + minutes
                                    : minutes
                                : "00"}
                            :
                            {seconds
                                ? seconds < 10
                                    ? "0" + seconds
                                    : seconds
                                : "00"}
                        </span>
                    );
                }
            }}
        />
    );
};

export default Clocker;
