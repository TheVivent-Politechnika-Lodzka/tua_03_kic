import styles from "./style.module.scss";

interface ValidationMessageProps {
    message: string;
    isValid: boolean;
    dot?: boolean;
}

const ValidationMessage = ({
    message,
    isValid,
    dot = true,
}: ValidationMessageProps) => {
    return (
        <div className={styles.validation_message}>
            <p
                className={`${styles.text} ${
                    isValid ? styles.valid : styles.invalid
                }`}
            >
                {dot && "•"} {message}
            </p>
        </div>
    );
};

export default ValidationMessage;
