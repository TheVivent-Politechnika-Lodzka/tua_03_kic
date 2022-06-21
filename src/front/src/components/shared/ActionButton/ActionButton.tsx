import { IconProp } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import styles from "./style.module.scss";
import ReactLoading from "react-loading";
import { useEffect } from "react";

interface ActionButtonProps {
    title: string;
    color: string;
    icon: IconProp;
    onClick: () => void;
    isDisabled?: boolean;
    isLoading?: boolean;
}

const ActionButton = ({
    icon,
    title,
    color,
    onClick,
    isDisabled,
    isLoading,
}: ActionButtonProps) => {
    useEffect(() => {}, [isLoading]);

    return (
        <div
            className={`${styles.action_button} ${styles[color]} ${
                isDisabled && styles.disabled
            } ${isLoading && styles.loading}`}
            onClick={onClick}
        >
            {isLoading ? (
                <ReactLoading
                    className={styles.spinner}
                    type="spin"
                    color="#fff"
                    width="2rem"
                    height="2rem"
                />
            ) : (
                <>
                    <FontAwesomeIcon icon={icon} />
                    <p className={styles.title}>{title}</p>
                </>
            )}
        </div>
    );
};

export default ActionButton;
