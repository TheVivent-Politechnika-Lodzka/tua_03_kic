import styles from "./removeAccessLevelForm.module.scss";
import { useState } from "react";
import { useRemoveAccessLevelMutation } from "../../../api/api";
import { useTranslation } from "react-i18next";
import { AccessLevelName } from "../../../api/types/common";
import { AccountWithAccessLevelsDto } from "../../../api/types/apiParams";

interface LoginFormProps {
    login: string;
    level: AccessLevelName;
    eTag: string;
    callback?: (account: AccountWithAccessLevelsDto) => void;
}

const RemoveAccessLevelForm = ({
    eTag,
    level,
    login,
    callback,
}: LoginFormProps) => {
    const [remove] = useRemoveAccessLevelMutation();
    const [message, setMessage] = useState("");

    const { t } = useTranslation();

    const handleSubmit = async (event: any) => {
        event.preventDefault();

        if (login && eTag) {
            const res = await remove({
                login,
                eTag,
                accessLevel: level,
            });

            if ("data" in res) {
                if (callback) {
                    callback(res.data);
                }
            }

            if ("error" in res && "status" in res.error) {
                if (res.error.status === "PARSING_ERROR")
                    setMessage(res.error.data as string);
            }
        } else {
            setMessage(t("refill_data"));
        }
    };

    return (
        <>
            <button className={styles.remove_button} onClick={handleSubmit}>
                {t("REMOVE")}
            </button>
            {message}
        </>
    );
};

export default RemoveAccessLevelForm;
