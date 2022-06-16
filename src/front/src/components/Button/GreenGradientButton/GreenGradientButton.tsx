import styles from "./greenGradientButton.module.scss";

interface ButtonProps {
    label: string;
    onClick: React.Dispatch<React.SetStateAction<any>>;
}

export const GreenGradientButton = ({ onClick, label }: ButtonProps) => {
    return (
        <div className={styles.button} onClick={onClick}>
            <p> {label}</p>
        </div>
    );
};
