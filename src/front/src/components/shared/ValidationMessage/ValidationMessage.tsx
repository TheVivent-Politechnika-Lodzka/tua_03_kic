import "./style.scss";

interface ValidationMessageProps {
  message: string;
  isValid: boolean;
}

const ValidationMessage = ({ message, isValid }: ValidationMessageProps) => {
  return (
    <div className="validation-message">
      <p className={`validation-message-text ${isValid ? "valid" : "invalid"}`}>
        ⇦ {message}
      </p>
    </div>
  );
};

export default ValidationMessage;
