import styles from "./blueGradientButton.module.scss";

interface ButtonProps {
    label: string;
    onClick: React.Dispatch<React.SetStateAction<any>>;
}

export const BlueGradientButton = ({ onClick, label }: ButtonProps) => {
    return (
        <div className={styles.button} onClick={onClick}>
            <p> {label}</p>
        </div>
    );
};
