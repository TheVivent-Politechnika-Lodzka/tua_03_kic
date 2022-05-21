import { useNavigate } from "react-router";
import styles from "./logo.module.scss";

const Logo = () => {
  const navigate = useNavigate();

  return (
    <div
      className={styles.logo}
      onClick={() => navigate("/", { replace: false })}
    >
      <img src="logo.jpg" alt="Logo" height="80px" />
    </div>
  );
};

export default Logo;
