import style from "./createImplantPage.module.scss";
import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { showNotification } from "@mantine/notifications";
import { useContext, useState, useTransition } from "react";
import { useNavigate } from "react-router";
import { createAccount, CreateAccountRequest } from "../../../../api";

import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import InputWithValidation from "../../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../../components/shared/ValidationMessage/ValidationMessage";
import { validationContext } from "../../../../context/validationContext";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../../utils/showNotificationsItems";
import { useTranslation } from "react-i18next";
import { Center, Image } from "@mantine/core";
import { HiOutlinePhotograph } from "react-icons/hi";
import { uploadPhoto } from "../../../../utils/upload";
import { createImplant } from "../../../../api/mop";
import ConfirmActionModal from "../../../../components/shared/ConfirmActionModal/ConfirmActionModal";

export const CreateImplantPage = () => {
    const { t } = useTranslation();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });

    const [error, setError] = useState<ApiError>();
    const [opened, setOpened] = useState<boolean>(false);
    const [implant, setImplant] = useState({
        name: "",
        description: "",
        manufacturer: "",
        price: "",
        duration: "",
        url: "",
    });

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
            duration: parseInt(implant.duration) * 60000,
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
                            <div className={`${style.image} ${style.margin}`}>
                                <Center>
                                    <HiOutlinePhotograph size="80px" />
                                </Center>
                            </div>
                        ) : (
                            <Image
                                radius="md"
                                src={implant.url}
                                height="20vw"
                                alt="image create"
                                styles={{
                                    root: { marginTop: "6vh" },
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
                            <InputWithValidation
                                title={t("createImplantPage.description")}
                                value={implant?.description}
                                validationType="VALIDATE_DESCRIPTION"
                                isValid={isDescriptionValid}
                                onChange={(e) => {
                                    setImplant({
                                        ...implant,
                                        description: e.target.value,
                                    });
                                }}
                                required
                            />
                            <ValidationMessage
                                isValid={isDescriptionValid}
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
                            title="Zatwierdź"
                        />
                        <ActionButton
                            onClick={() => {
                                navigate("/implants");
                            }}
                            icon={faCancel}
                            color="red"
                            title="Anuluj"
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
