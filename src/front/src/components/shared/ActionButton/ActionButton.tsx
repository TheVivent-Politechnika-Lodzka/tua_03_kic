import { IconProp } from "@fortawesome/fontawesome-svg-core";
import { faKey } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./style.scss";


// Po implementacji usunac z onClick() opcjonalność
interface ActionButtonProps {
  title: string;
  color: string;
  icon: IconProp;
  onClick?: () => void;
  isDisabled?: boolean;
}

const ActionButton = ({
  icon,
  title,
  color,
  onClick,
  isDisabled,
}: ActionButtonProps) => {
  return (
    <div
      className={`action-button ${color} ${isDisabled && "disabled"}`}
      onClick={onClick}
    >
      <FontAwesomeIcon icon={icon} />
      <p className="action-button-title">{title}</p>
    </div>
  );
};

export default ActionButton;
