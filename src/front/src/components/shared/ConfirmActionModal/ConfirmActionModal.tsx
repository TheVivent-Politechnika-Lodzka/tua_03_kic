import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { Modal } from "@mantine/core";
import { Children } from "react";
import { useTranslation } from "react-i18next";
import ActionButton from "../ActionButton/ActionButton";
import styles from "./style.module.scss";

interface ConfirmActionModalProps {
    title: string;
    isOpened: boolean;
    isLoading: boolean;
    handleFunction: () => void;
    onClose: () => void;
    children?: React.ReactNode[] | React.ReactNode;
    size?: string | number;
}

const ConfirmActionModal = ({
    title,
    isOpened,
    handleFunction,
    isLoading,
    onClose,
    children,
    size = "lg",
}: ConfirmActionModalProps) => {
    const { t } = useTranslation();

    return (
        <Modal
            styles={{ modal: { backgroundColor: "#262633" } }}
            size={size}
            centered
            overflow="outside"
            radius="lg"
            opened={isOpened}
            onClose={onClose}
            transition="pop-bottom-left"
            transitionDuration={200}
            transitionTimingFunction="ease-in-out"
        >
            <div className={styles.confirm_action_modal_content}>
                <p className={styles.title}>{title}</p>
                <p className={styles.description}>{children}</p>
                <div className={styles.action_buttons_wrapper}>
                    <ActionButton
                        isLoading={isLoading}
                        title={t("confirmationModal.yes")}
                        color="green"
                        icon={faCheck}
                        onClick={handleFunction}
                    />
                    <ActionButton
                        title={t("confirmationModal.no")}
                        color="red"
                        icon={faCancel}
                        onClick={onClose}
                    />
                </div>
            </div>
        </Modal>
    );
};

export default ConfirmActionModal;
