import { faCheckCircle, faExclamationCircle, faPersonCircleExclamation } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { NotificationProps } from "@mantine/notifications";

const successNotficiationItems = (message: string): NotificationProps => {
    return {
        title: `Powodzenie`,
        message: `${message}`,
        autoClose: 5000,
        color: "green",
        icon: <FontAwesomeIcon icon={faCheckCircle} color="white" />,
        styles: {
            title: { color: "white", fontWeight: "bold", fontSize: "1.25rem" },
            description: { color: "white", fontSize: "1.1rem" },
            root: {
                backgroundColor: "#262633",
                border: "none",
                boxShadow: "8px 8px 24px 0px rgba(66, 68, 90, 0.25);",
            },
        },
    };
};

const failureNotificationItems = (message: string): NotificationProps => {
    return {
        title: `Błąd`,
        message: `${message}`,
        autoClose: 5000,
        color: "red",
        icon: <FontAwesomeIcon icon={faExclamationCircle} color="white" />,
        styles: {
            title: { color: "white", fontWeight: "bold", fontSize: "1.25rem" },
            description: { color: "white", fonSize: "1.1rem" },
            root: {
                backgroundColor: "#262633",
                border: "none",
                boxShadow: "8px 8px 24px 0px rgba(66, 68, 90, 0.25);",
            },
        },
    };
};

export { successNotficiationItems, failureNotificationItems };
