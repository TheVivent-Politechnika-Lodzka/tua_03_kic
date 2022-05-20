import { Fragment } from "react";
import { useNavigate } from "react-router";
import styles from "./appointmentNavBar.module.scss";

import Button from "@mui/material/Button";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import PopupState, { bindTrigger, bindMenu } from "material-ui-popup-state";
import { useTranslation } from "react-i18next";

const SpecialistNavBar = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();

  return (
    <PopupState variant="popover" popupId="demo-popup-menu">
      {(popupState) => (
        <Fragment>
          <Button variant="contained" {...bindTrigger(popupState)}>
            {t("specialist")}
          </Button>
          <Menu {...bindMenu(popupState)}>
            <MenuItem onClick={() => navigate("/specialist")}>PAGE1</MenuItem>
            <MenuItem onClick={popupState.close}>PAGE2</MenuItem>
            <MenuItem onClick={popupState.close}>PAGE3</MenuItem>
          </Menu>
        </Fragment>
      )}
    </PopupState>
  );
};

export default SpecialistNavBar;
