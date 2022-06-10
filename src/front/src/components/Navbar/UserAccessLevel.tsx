import {
    faUser,
    faUserCog,
    faUserDoctor,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./style.scss";

interface UserAccessLevelProps {
    accessLevel: string;
}

const UserAccessLevel = ({ accessLevel }: UserAccessLevelProps) => {
    switch (accessLevel) {
        case "ADMINISTRATOR": {
            return (
                <div className="user-access-level-wrapper">
                    <FontAwesomeIcon
                        className="access-level-icon"
                        icon={faUserCog}
                    />
                    <p className="access-level-text">Administrator</p>
                </div>
            );
        }
        case "SPECIALIST": {
            return (
                <div className="user-access-level-wrapper">
                    <FontAwesomeIcon
                        className="access-level-icon"
                        icon={faUserDoctor}
                    />
                    <p className="access-level-text">Specjalista</p>
                </div>
            );
        }
        case "CLIENT": {
            return (
                <div className="user-access-level-wrapper">
                    <FontAwesomeIcon
                        className="access-level-icon"
                        icon={faUser}
                    />
                    <p className="access-level-text">Klient</p>
                </div>
            );
        }
        default: {
            return null;
        }
    }
};

export default UserAccessLevel;
