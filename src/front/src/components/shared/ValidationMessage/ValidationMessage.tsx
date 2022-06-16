import styles from "./style.module.scss";

interface ValidationMessageProps {
  message: string;
  isValid: boolean;
}

const ValidationMessage = ({ message, isValid }: ValidationMessageProps) => {
  return (
    <div className={styles.validation_message}>
      <p className={`${styles.text} ${isValid ? styles.valid : styles.invalid}`}>
        ⇦ {message}
      </p>
    </div>
  );
};

export default ValidationMessage;
