import {  useEffect } from "react";
import { useDispatch } from "react-redux";
import { AccessLevelDto } from "../../../api/types/apiParams";
import { useStoreSelector } from "../../../redux/reduxHooks";
import { changeLevel } from "../../../redux/userSlice";
import styles from "./style.module.scss";

interface AccessLevelProps {
    accessLevel: AccessLevelDto;
    clickable?: boolean;
}

const getAccessLevel = (accessLevel: string) => {
    switch (accessLevel) {
        case "ADMINISTRATOR": {
            return "Administrator";
        }
        case "SPECIALIST": {
            return "Specialista";
        }
        case "CLIENT": {
            return "Klient";
        }
        default: {
            return "";
        }
    }
};

const AccessLevel = ({ accessLevel, clickable = false }: AccessLevelProps) => {
    const user = useStoreSelector((state) => state.user);
    const level = useStoreSelector((state) => state.user.cur);
    const dispatch = useDispatch();

    const handleClick = () => {
        if (clickable) {
            for (let i = 0; i < user.auth.length; i++) {
                if (user.auth[i] === accessLevel.level) {
                    dispatch(
                        changeLevel({
                            sub: user.sub,
                            auth: user.auth,
                            index: i,
                            exp: user.exp,
                        })
                    );
                }
            }
        }
    };

    useEffect(() => {}, []);

    return (
        <div onClick={handleClick} className={styles.accessLevelWrapper}>
            <p
                className={`${
                    styles.accessLevelText
                }  ${accessLevel?.level.toLowerCase()} ${
                    accessLevel?.level === level && styles.selected && clickable
                        ? styles.selected
                        : null
                }`}
            >
                {getAccessLevel(accessLevel?.level)}
            </p>
        </div>
    );
};

export default AccessLevel;
