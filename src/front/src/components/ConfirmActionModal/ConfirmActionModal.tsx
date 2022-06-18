import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { Modal } from "@mantine/core";
import ActionButton from "../shared/ActionButton/ActionButton";
import styles from "./style.module.scss";

interface ConfirmActionModalProps {
    title: string;
    description: string;
    isOpened: boolean;
    isLoading: boolean;
    handleFunction: () => void;
    onClose: () => void;
}

const ConfirmActionModal = ({
    title,
    description,
    isOpened,
    handleFunction,
    isLoading,
    onClose,
}: ConfirmActionModalProps) => {
    return (
        <Modal
            styles={{ modal: { backgroundColor: "#262633" } }}
            size="lg"
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
                <p className={styles.description}>{description}</p>
                <div className={styles.action_buttons_wrapper}>
                    <ActionButton
                        isLoading={isLoading}
                        title="Tak"
                        color="green"
                        icon={faCheck}
                        onClick={handleFunction}
                    />
                    <ActionButton
                        title="Nie"
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
