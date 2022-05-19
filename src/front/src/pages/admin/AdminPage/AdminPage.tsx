import { useGetOwnAccountDetailsQuery } from "../../../api/api";
import "./style.scss";

const AdminPage = () => {
  // TODO: nie działa. Wyrzuca 401, ale w postmanie podobne zapytanie działa. Do naprawy
  const { data } = useGetOwnAccountDetailsQuery();
  return (
    <div className="whiteText">
      <h1>Admin Page</h1>
      <p>imię: {data?.firstName}</p>
    </div>
  );
};

export default AdminPage;
