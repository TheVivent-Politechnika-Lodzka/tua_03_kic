import "./style.scss";
import { FaRobot } from "react-icons/fa";
import { Link } from "react-router-dom";

const ErrorPage = () => {
  return (
    <div>
      <div>
        <Link to="/" style={{ textDecoration: "none" }}>
          <img src="./../logo.jpg" alt="Logo" height="80px" />
        </Link>
      </div>
      <div className="errorbox">
        <div className="errortext">
          <FaRobot size="50px" className="icon" />
          Przykro mi, strona nie istnieje
        </div>

        <div className="linktext">
          Zapraszamy na stronę główną
          <Link to="/" style={{ textDecoration: "none", color: "black" }}>
            <div className="button">STRONA GŁÓWNA</div>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default ErrorPage;
