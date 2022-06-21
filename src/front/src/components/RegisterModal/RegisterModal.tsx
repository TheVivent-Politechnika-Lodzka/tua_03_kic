import { faCheck } from "@fortawesome/free-solid-svg-icons";
import { Modal } from "@mantine/core";
import ActionButton from "../shared/ActionButton/ActionButton";
import styles from "./registerModal.module.scss";

interface RegisterModalProps {
    isOpened: boolean;
    onClose: () => void;
    children?: React.ReactNode[] | React.ReactNode;
}

export const RegisterModal = ({
    isOpened,
    onClose,
    children,
}: RegisterModalProps) => {
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
                <p className={styles.description}>{children}</p>
                <div className={styles.action_buttons_wrapper}>
                    <ActionButton
                        title="OK"
                        color="green"
                        icon={faCheck}
                        onClick={onClose}
                    />
                </div>
            </div>
        </Modal>
    );
};
