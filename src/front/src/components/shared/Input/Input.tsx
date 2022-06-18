import styles from "./style.module.scss";

interface InputProps {
    placeholder: string;
    value: string;
    title: string;
    type: string;
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const Input = ({ placeholder, value, title, type, onChange }: InputProps) => {
    return (
        <div className={styles.input_wrapper}>
            <p className={styles.title}>{title}</p>
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
