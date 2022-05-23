import style from "./accountDetails.module.scss";
import { useState, useEffect } from "react";
import {
    useEditOwnAccountMutation,
    useGetOwnAccountDetailsWorkaroundMutation,
} from "../../../api/api";
import { AccountWithAccessLevelsDto } from "../../../api/types/apiParams";
import ChangeOwnPasswordForm from "../../../component/Form/changeOwnPasswordForm/ChangeOwnPasswordForm";
import { Button } from "@mui/material";
import {
    GoogleReCaptchaProvider,
    useGoogleReCaptcha,
} from "react-google-recaptcha-v3";

const EditAccountDetails = () => {
    const [getCurrentUser] = useGetOwnAccountDetailsWorkaroundMutation();
    const [saveCurrentUser] = useEditOwnAccountMutation();
    const [message, setMessage] = useState("");
    const [currentUserData, setCurrentUserData] =
        useState<AccountWithAccessLevelsDto>({} as AccountWithAccessLevelsDto);

    const { executeRecaptcha } = useGoogleReCaptcha();

    useEffect(() => {
        getAccountDetails();
    }, []);

    const getAccountDetails = async () => {
        const res = await getCurrentUser();
        if ("data" in res) {
            setCurrentUserData(res.data);
        }
    };

    const handleSubmit = async () => {
        if (executeRecaptcha === undefined) {
            return;
        }
        const captcha = await executeRecaptcha("register");

        currentUserData.captcha = captcha;

        const res = await saveCurrentUser(currentUserData);
        if ("data" in res) {
            setCurrentUserData(res.data);
        } else if ("error" in res && "data" in res.error) {
            setMessage(res.error.data as string);
        } else {
            setMessage("error");
        }
    };

    return (
        <div className={style.whiteText}>
            <h1>Edit Account Details Page</h1>
            <div>
                <div>
                    imię:{" "}
                    <input
                        value={currentUserData.firstName}
                        onChange={(e) =>
                            setCurrentUserData((old) => ({
                                ...old,
                                firstName: e.target.value,
                            }))
                        }
                    />
                    nazwisko:{" "}
                    <input
                        value={currentUserData.lastName}
                        onChange={(e) =>
                            setCurrentUserData((old) => ({
                                ...old,
                                lastName: e.target.value,
                            }))
                        }
                    />
                </div>
                <Button onClick={handleSubmit}>zapisz</Button>
                <div>{message}</div>
            </div>

            <ChangeOwnPasswordForm ETag={currentUserData.ETag} />
        </div>
    );
};

const EditAccountDetailsPage = () => {
    return (
        <>
            <GoogleReCaptchaProvider reCaptchaKey="6Lf85hEgAAAAAONrGfo8SoQSb9GmfzHXTTgjKJzT">
                <EditAccountDetails />
            </GoogleReCaptchaProvider>
        </>
    );
};

export default EditAccountDetailsPage;
