import { Link } from "react-router-dom";
import "./style.scss";

const TopBar = () => {
  return (
    <div className="topBar">
      <div className="logo">
        <Link to="/" style={{ textDecoration: "none" }}>
          <img src="logo.jpg" alt="Logo" height="80px" />
        </Link>
      </div>
      <div className="links">
        <div className="item">CYBERWSZCZEPY</div>
        <div className="item">SPECJALIŚCI</div>

        <Link to="/login" style={{ textDecoration: "none" }}>
          <div className="itembutton">LOGOWANIE</div>
        </Link>
      </div>
    </div>
  );
};

export default TopBar;
