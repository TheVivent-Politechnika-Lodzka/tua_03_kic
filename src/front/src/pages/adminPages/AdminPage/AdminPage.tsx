import { useGetOwnAccountDetailsQuery } from "../../../api/api";
import "./style.scss";

const AdminPage = () => {
    const {data} = useGetOwnAccountDetailsQuery();
    return (
        <div className="whiteText">AdminPage</div>
    );
}

export default AdminPage;