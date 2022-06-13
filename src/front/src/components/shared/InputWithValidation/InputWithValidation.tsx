import { useContext, useState } from "react";
import { faCheck, faClose } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./style.scss";
import {
    ActionType,
    validationContext,
} from "../../../context/validationContext";

interface InputWithValidationProps {
    title: string;
    value: string | undefined;
    validationType: ActionType;
    isValid: boolean;
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const InputWithValidation = ({
    title,
    value,
    validationType,
    isValid,
    onChange,
}: InputWithValidationProps) => {
    const [input, setInput] = useState<string | undefined>(value);
    const { state, dispatch } = useContext(validationContext);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setInput(e.target.value);
        dispatch({
            type: validationType,
            payload: { ...state, input: e.target.value },
        });
        if (onChange) {
            onChange(e);
        }
    };

    return (
        <div className="edit-field-wrapper">
            <p className="edit-field-title">{title}</p>
            <div className="input-wrapper">
                <input
                    type="text"
                    value={input}
                    onChange={handleChange}
                    className={`edit-field-input ${
                        isValid ? "valid" : "invalid"
                    }`}
                />
                <FontAwesomeIcon
                    className={`edit-field-icon ${
                        isValid ? "valid" : "invalid"
                    }`}
                    icon={isValid ? faCheck : faClose}
                    color="#00FF66"
                />
            </div>
        </div>
    );
};

export default InputWithValidation;
