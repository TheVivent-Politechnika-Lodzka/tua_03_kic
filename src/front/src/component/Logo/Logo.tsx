import { useNavigate } from "react-router";
import styles from "./logo.module.scss";

const Logo = () => {
    const navigate = useNavigate();

    return (
        <div
            className={styles.logo}
            onClick={() => navigate("/", { replace: false })}
        >
            <img
                src="https://localhost:8181/logo.svg"
                alt="Logo"
                height="70px"
            />
        </div>
    );
};

export default Logo;
