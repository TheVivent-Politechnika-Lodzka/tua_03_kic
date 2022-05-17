import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useActiveAccountMutation } from "../../../api/api";
import "./style.scss";
const ActiveAccountPage = () => {
    // TODO przerobic na pobranie tokenu z linku aktywacyjnego
  // let { token } = useParams();
  const [token, setToken] = useState("");
  const [confirm, { isLoading }] = useActiveAccountMutation();
  const [confirmed, setConfirmed] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async () => {
    if (token !== undefined) {
      await confirm({ token });
    }
    setConfirmed(true);
  };

  return (
    <div>
      <div className="text">Wprowadź token z maila</div>
      <input
        className="token_input"
        value={token}
        onChange={(e) => setToken(e.target.value)}
      ></input>
      <div className="confirm_button" onClick={handleSubmit}>
        Aktywuj konto
      </div>
      {confirmed ? (
        <div>
          <div className="text"> Konto potwierdzone</div>
          <div className="confirm_button" onClick={() => navigate("/login")}>
            Przejdz do logowania
          </div>
        </div>
      ) : (
        <></>
      )}
    </div>
  );
};

export default ActiveAccountPage;
