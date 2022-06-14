import { IconProp } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import styles from "./style.module.scss";

// Po implementacji usunac z onClick() opcjonalność
interface ActionButtonProps {
    title: string;
    color: string;
    icon: IconProp;
    onClick?: () => void;
    isDisabled?: boolean;
}

const ActionButton = ({
    icon,
    title,
    color,
    onClick,
    isDisabled,
}: ActionButtonProps) => {
    return (
        <div
            className={`${styles.action_button} ${styles[color]} ${
                isDisabled && styles.disabled
            }`}
            onClick={onClick}
        >
            <FontAwesomeIcon icon={icon} />
            <p className={styles.title}>{title}</p>
        </div>
    );
};

export default ActionButton;
