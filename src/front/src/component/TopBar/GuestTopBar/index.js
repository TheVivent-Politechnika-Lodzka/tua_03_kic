import { useNavigate } from "react-router-dom";
import "./style.scss";

const GuestTopBar = () => {
  const navigate = useNavigate();

  return (
    <div className="topBar">
      <div className="logo" onClick={() => navigate("/", { replace: false })}>
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
      <div className="links">
        <div className="item">CYBERWSZCZEPY</div>
        <div className="item">SPECJALIŚCI</div>

        <div
          className="itembutton"
          onClick={() => navigate("/login", { replace: false })}
        >
          LOGOWANIE
        </div>
      </div>
    </div>
  );
};

export default GuestTopBar;
