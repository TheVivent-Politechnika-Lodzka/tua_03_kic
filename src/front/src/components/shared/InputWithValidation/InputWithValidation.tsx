import { useContext, useEffect, useState } from "react";
import { faCheck, faClose } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    ActionType,
    validationContext,
} from "../../../context/validationContext";
import styles from "./style.module.scss";
import CSS from "csstype";

interface InputWithValidationProps {
    title: string;
    value: string | undefined;
    validationType: ActionType;
    isValid: boolean;
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
    required?: boolean;
    styleWidth?: CSS.Properties;
    type?: string;
}

const InputWithValidation = ({
    title,
    value,
    validationType,
    isValid,
    required,
    styleWidth,
    onChange,
    type = "text",
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

    useEffect(() => {
        dispatch({
            type: validationType,
            payload: { ...state, input: input ?? "" },
        });
    }, [input]);

    return (
        <div className={styles.edit_field_wrapper}>
            <div className={styles.input_wrapper} style={styleWidth}>
                {required ? (
                    <>
                        <p className={styles.title}>
                            {title} <span style={{ color: "red" }}>*</span>
                        </p>
                    </>
                ) : (
                    <>
                        <p className={styles.title}>{title}</p>
                    </>
                )}

                <input
                    type={type}
                    value={input}
                    onChange={handleChange}
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
