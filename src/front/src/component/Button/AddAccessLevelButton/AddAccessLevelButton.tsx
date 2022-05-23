import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  MenuItem,
  Modal,
  Select,
  TextField,
} from "@mui/material";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useAddAccessLevelMutation } from "../../../api/api";
import {
  AccessLevelDto,
  AccountWithAccessLevelsDto,
} from "../../../api/types/apiParams";
import { AccessLevelName } from "../../../api/types/common";

interface AddAccessLevelButtonProps {
  login: string;
  callback?: (account: AccountWithAccessLevelsDto) => void;
}

const AddAccessLevelButton = ({
  login,
  callback,
}: AddAccessLevelButtonProps) => {
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [message, setMessage] = useState<string>("");
  const { t } = useTranslation();
  const [addAccessLevel, { isLoading }] = useAddAccessLevelMutation();
  const [accessLevel, setAccessLevel] = useState<AccessLevelDto>({
    level: "CLIENT",
  });

  const handleCancel = () => {
    setMessage("");
    setAccessLevel({ level: "CLIENT" });
    setIsModalOpen(false);
  };

  const handleAddAccessLevel = async () => {
    console.log({ accessLevel });
    const res = await addAccessLevel({
      login,
      accessLevel: accessLevel,
    });
    if ("data" in res) {
      if (callback) callback(res.data);
      setIsModalOpen(false);
      return;
    } else if ("data" in res.error) {
      setMessage(res.error.data as string);
    } else {
      setMessage(t("server_error"));
    }
  };

  return (
    <>
      <Button
        variant="contained"
        color="primary"
        onClick={() => setIsModalOpen(true)}
      >
        {t("button.addAccessLevel.buttonText")}
      </Button>
      <Dialog open={isModalOpen} onClose={handleCancel}>
        <DialogTitle>{t("button.addAccessLevel.dialog.title")}</DialogTitle>
        <DialogContent>
          Poziom dostępu:
          <Select
            value={accessLevel.level}
            onChange={(event) => {
              setAccessLevel((old) => ({
                ...old,
                level: event.target.value as AccessLevelName,
              }));
            }}
          >
            <MenuItem value="CLIENT">
              {t("button.addAccessLevel.dialog.select.client")}
            </MenuItem>
            <MenuItem value="SPECIALIST">
              {t("button.addAccessLevel.dialog.select.specialist")}
            </MenuItem>
            <MenuItem value="ADMINISTRATOR">
              {t("button.addAccessLevel.dialog.select.administrator")}
            </MenuItem>
          </Select>
          <div></div>
          {/* Pole email */}
          {["SPECIALIST", "ADMINISTRATOR"].includes(accessLevel.level) && (
            <div>
              <TextField
                type="email"
                placeholder={t(
                  "button.addAccessLevel.dialog.email.placeholder"
                )}
                onChange={(e) =>
                  setAccessLevel((old) => ({
                    ...old,
                    contactEmail: e.target.value,
                  }))
                }
              />
            </div>
          )}
          {/* Pole numer telefonu */}
          {["CLIENT", "SPECIALIST", "ADMINISTRATOR"].includes(
            accessLevel.level
          ) && (
            <div>
              <TextField
                type="phone"
                placeholder={t(
                  "button.addAccessLevel.dialog.phone.placeholder"
                )}
                onChange={(e) =>
                  setAccessLevel((old) => ({
                    ...old,
                    phoneNumber: e.target.value,
                  }))
                }
              />
            </div>
          )}
          {/* Pole pesel */}
          {["CLIENT"].includes(accessLevel.level) && (
            <div>
              <TextField
                placeholder={t(
                  "button.addAccessLevel.dialog.pesel.placeholder"
                )}
                onChange={(e) =>
                  setAccessLevel((old) => ({
                    ...old,
                    pesel: e.target.value,
                  }))
                }
              />
            </div>
          )}
          <div>{message}</div>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCancel}>
            {t("button.addAccessLevel.dialog.cancel")}
          </Button>
          <Button onClick={handleAddAccessLevel}>
            {t("button.addAccessLevel.dialog.add")}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default AddAccessLevelButton;
