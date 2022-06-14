import { useContext, useState } from "react";
import { faCheck, faClose } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    ActionType,
    validationContext,
} from "../../../context/validationContext";
import styles from "./style.module.scss";

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
        <div className={styles.edit_field_wrapper}>
            <p className={styles.title}>{title}</p>
            <div className={styles.input_wrapper}>
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
                    className={`${styles.input} ${
                        isValid ? styles.valid : styles.invalid
                    }`}
                />
                <FontAwesomeIcon
                    className={`${styles.icon} ${
                        isValid ? styles.valid : styles.invalid
                    }`}
                    icon={isValid ? faCheck : faClose}
                    color="#00FF66"
                />
            </div>
        </div>
    );
};

export default InputWithValidation;
