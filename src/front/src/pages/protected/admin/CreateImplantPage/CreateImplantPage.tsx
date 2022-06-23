import style from "./createImplantPage.module.scss";
import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { showNotification } from "@mantine/notifications";
import { useContext, useState } from "react";
import { useNavigate } from "react-router";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import InputWithValidation from "../../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../../components/shared/ValidationMessage/ValidationMessage";
import { validationContext } from "../../../../context/validationContext";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../../utils/showNotificationsItems";
import { useTranslation } from "react-i18next";
import {  Image } from "@mantine/core";
import { uploadPhoto } from "../../../../utils/upload";
import { createImplant } from "../../../../api/mop";
import ConfirmActionModal from "../../../../components/shared/ConfirmActionModal/ConfirmActionModal";

export const CreateImplantPage = () => {
    const { t } = useTranslation();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [opened, setOpened] = useState<boolean>(false);
    const [implant, setImplant] = useState({
        name: "",
        description: "",
        manufacturer: "",
        price: "",
        duration: "",
        url: "",
    });
    const [count, setCount] = useState(implant.description.length);
    const {
        state,
        state: {
            isImplantNameValid,
            isManufacturerValid,
            isPriceValid,
            isDurationValid,
            isDescriptionValid,
        },
        dispatch,
    } = useContext(validationContext);

    const navigate = useNavigate();

    const isEveryFieldValid =
        isImplantNameValid &&
        isManufacturerValid &&
        isPriceValid &&
        isDurationValid &&
        isDescriptionValid;

    const handleSubmit = async () => {
        const response = await createImplant({
            name: implant.name,
            description: implant.description,
            manufacturer: implant.manufacturer,
            price: parseInt(implant.price),
            duration: parseInt(implant.duration) * 60,
            url: implant.url,
        });
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            return;
        }
        showNotification(
            successNotficiationItems(t("createImplantPage.implantCreated"))
        );
        navigate("/implants");
    };

    return (
        <section className={style.create_implant_page}>
            <div className={style.create_implant_page_header}>
                <h2> {t("createImplantPage.addImplant")}</h2>
            </div>
            <div className={style.create_implant_page_content}>
                <div className={style.create_data_account_wrapper}>
                    <div className={style.edit_fields_wrapper}>
                        {implant.url.length === 0 ? (
                            <Image
                                radius="md"
                                withPlaceholder
                                height="15rem"
                                width="25rem"
                                styles={{
                                    root: { marginTop: "2vh" },
                                }}
                            />
                        ) : (
                            <Image
                                radius="md"
                                src={implant.url}
                                height="15rem"
                                width="25rem"
                                alt="image create"
                                styles={{
                                    root: { marginTop: "2vh" },
                                }}
                            />
                        )}

                        <input
                            id="file-input"
                            type="file"
                            onChange={async (event) => {
                                const u = await uploadPhoto(event);
                                if (u) {
                                    setImplant({
                                        ...implant,
                                        url: u,
                                    });
                                }
                            }}
                        />
                    </div>
                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title={t("createImplantPage.name")}
                                value={implant?.name}
                                validationType="VALIDATE_IMPLANT_NAME"
                                isValid={isImplantNameValid}
                                styleWidth={{ width: "20rem" }}
                                onChange={(e) => {
                                    setImplant({
                                        ...implant,
                                        name: e.target.value,
                                    });
                                }}
                                required
                            />
                            <ValidationMessage
                                isValid={isImplantNameValid}
                                message={t("createImplantPage.nameMsg")}
                            />
                        </div>
                    </div>
                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title={t("createImplantPage.manufacturer")}
                                value={implant?.manufacturer}
                                validationType="VALIDATE_MANUFACTURER"
                                isValid={isManufacturerValid}
                                styleWidth={{ width: "20rem" }}
                                onChange={(e) => {
                                    setImplant({
                                        ...implant,
                                        manufacturer: e.target.value,
                                    });
                                }}
                                required
                            />
                            <ValidationMessage
                                isValid={isManufacturerValid}
                                message={t("createImplantPage.manufacturerMsg")}
                            />
                        </div>
                    </div>

                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title={t("createImplantPage.price")}
                                value={implant?.price}
                                validationType="VALIDATE_PRICE"
                                isValid={isPriceValid}
                                styleWidth={{ width: "20rem" }}
                                onChange={(e) => {
                                    setImplant({
                                        ...implant,
                                        price: e.target.value,
                                    });
                                }}
                                required
                            />
                            <ValidationMessage
                                isValid={isPriceValid}
                                message={t("createImplantPage.priceMsg")}
                            />
                        </div>
                    </div>

                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title={t("createImplantPage.duration")}
                                value={implant?.duration}
                                validationType="VALIDATE_DURATION"
                                isValid={isDurationValid}
                                styleWidth={{ width: "20rem" }}
                                onChange={(e) => {
                                    setImplant({
                                        ...implant,
                                        duration: e.target.value,
                                    });
                                }}
                                required
                            />
                            <ValidationMessage
                                isValid={isDurationValid}
                                message={t("createImplantPage.durationMsg")}
                            />
                        </div>
                    </div>

                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field}>
                            <textarea
                                className={style.description_input}
                                value={implant?.description}
                                maxLength={950}
                                onChange={(e) => {
                                    setImplant({
                                        ...implant,
                                        description: e.target.value,
                                    });
                                    setCount(e.target.value.length);
                                }}
                            />

                            <div className={style.description_length}>
                                {count}/1000
                            </div>

                            <ValidationMessage
                                isValid={count > 100}
                                message={t("createImplantPage.descriptionMsg")}
                            />
                        </div>
                    </div>

                    <div className={style.create_implant_buttons_wrapper}>
                        <ActionButton
                            onClick={() => {
                                setOpened(true);
                            }}
                            isDisabled={!isEveryFieldValid}
                            icon={faCheck}
                            color="green"
                            title={t("addImplantReviewPage.confirm")}
                        />
                        <ActionButton
                            onClick={() => {
                                navigate("/implants");
                            }}
                            icon={faCancel}
                            color="red"
                            title={t("addImplantReviewPage.cancel")}
                        />
                    </div>
                </div>
            </div>

            <ConfirmActionModal
                isOpened={opened}
                onClose={() => {
                    setOpened(false);
                }}
                handleFunction={async () => {
                    setOpened(false);
                    handleSubmit();
                }}
                isLoading={loading.actionLoading as boolean}
                title={t("createImplantPage.modalTitle")}
            >
                {t("createImplantPage.confirmMsg")}
            </ConfirmActionModal>
        </section>
    );
};
