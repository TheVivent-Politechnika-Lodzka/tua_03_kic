import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { logout } from "../../../redux/userSlice";
import "./style.scss";
import {Logout} from "../SelectorTopBar";

const AdminTopBar = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  return (
    <div className="topBar">
      <div className="logo" onClick={() => navigate("/", { replace: false })}>
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
      <div className="links">
        <div className="item">CYBERWSZCZEPY</div>
        <div className="item">SPECJALIŚCI</div>
        <div
          className="item"
          onClick={() => navigate("/admin", { replace: false })}
        >
          ADMINPAGE
        </div>
          <Logout/>
      </div>
    </div>
  );
};

export default AdminTopBar;
