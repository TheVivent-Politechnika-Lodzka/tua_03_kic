import { useGetOwnAccountDetailsQuery } from "../../../api/api";
import "./style.scss";

const AdminPage = () => {
  const { data } = useGetOwnAccountDetailsQuery();
  return (
    <div className="whiteText">
      <h1>Admin Page</h1>
      <p>imię: {data?.firstName}</p>
    </div>
  );
};

export default AdminPage;
