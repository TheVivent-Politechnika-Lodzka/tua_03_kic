import { IconProp } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./style.scss";

interface SideBarRouteProps {
    index?: number;
    icon: IconProp;
    title: string;
    expanded: boolean;
    active?: boolean;
    onClick?: () => void;
}

const SideBarRoute = ({
    icon,
    active,
    title,
    expanded,
    onClick,
}: SideBarRouteProps) => {
    return (
        <div className="sidebar-route-wrapper">
            <div
                onClick={onClick}
                className={`sidebar-route-button ${active && `active`} ${
                    expanded && `route-button-expanded`
                } ${title === "Wyloguj się" && `logout`}`}
            >
                <FontAwesomeIcon icon={icon} />
                {expanded && <p className="route-button-name">{title}</p>}
            </div>
            <p
                className={
                    expanded ? `tooltip-expanded` : `sidebar-route-tooltip`
                }
            >
                {title}
            </p>
        </div>
    );
};

export default SideBarRoute;
