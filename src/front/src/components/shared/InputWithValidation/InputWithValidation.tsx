import { useContext, useState } from "react";
import { faCheck, faClose } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./style.scss";
import { ActionType, validationContext } from "../../../context/validationContext";

interface InputWithValidationProps {
  title: string;
  value: string | undefined;
  validationType: ActionType;
  isValid: boolean;
}

const InputWithValidation = ({
  title,
  value,
  validationType,
  isValid,
}: InputWithValidationProps) => {
  const [input, setInput] = useState<string | undefined>(value);
  const { state, dispatch } = useContext(validationContext);

  return (
    <div className="edit-field-wrapper">
      <p className="edit-field-title">{title}</p>
      <div className="input-wrapper">
        <input
          type="text"
          value={input}
          onChange={(e) => {
            setInput(e.target.value);
            dispatch({
              type: validationType,
              payload: { ...state, input: e.target.value },
            });
          }}
          className={`edit-field-input ${isValid ? "valid" : "invalid"}`}
        />
        <FontAwesomeIcon
          className={`edit-field-icon ${isValid ? "valid" : "invalid"}`}
          icon={isValid ? faCheck : faClose}
          color="#00FF66"
        />
      </div>
    </div>
  );
};

export default InputWithValidation;
