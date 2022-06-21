import styles from "./style.module.scss";

interface InputProps {
    placeholder: string;
    value: string;
    title: string;
    type: string;
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
    style?: React.CSSProperties;
    required?: boolean;
}

const Input = ({
    placeholder,
    value,
    title,
    type,
    onChange,
    style,
    required = false,
}: InputProps) => {
    return (
        <div className={styles.input_wrapper} style={style}>
            <p className={styles.title}>
                {title}
                {required && <span className={styles.star}> *</span>}
            </p>
            <input
                className={styles.input}
                value={value}
                type={type}
                placeholder={placeholder}
                onChange={onChange}
            />
        </div>
    );
};

export default Input;
