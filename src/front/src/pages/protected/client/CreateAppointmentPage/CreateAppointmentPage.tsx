import {
    faArrowAltCircleRight,
    faShoppingCart,
} from "@fortawesome/free-solid-svg-icons";
import "dayjs/locale/en";
import "dayjs/locale/pl";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import style from "./style.module.scss";
import { Calendar } from "@mantine/dates";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import {
    createAppointment,
    getImplant,
    GetImplantResponse,
    getSpecialistAvailability,
    listSpecialist,
    SpecialistListElementDto,
    SpecialistListResponse,
} from "../../../../api/mop";
import { showNotification } from "@mantine/notifications";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../../utils/showNotificationsItems";
import { ChronoUnit, Instant, LocalDateTime } from "@js-joda/core";
import { Indicator, Modal } from "@mantine/core";
import ReactModal from "react-modal";
import { useTranslation } from "react-i18next";

interface HourInterface {
    text: string;
    value: Instant;
}

const CreateAppointmentPage = () => {
    const { implantId } = useParams();
    const { t } = useTranslation();
    const [specialistList, setSpecialistList] = useState<
        SpecialistListElementDto[]
    >([]);
    const [implant, setImplant] = useState<GetImplantResponse>();
    const [specialist, setSpecialist] = useState<SpecialistListElementDto>();
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [availableDates, setAvailableDates] = useState<HourInterface[][]>([]);
    const [currentMonth, setCurrentMonth] = useState(new Date());
    const [selectedDate, setSelectedDate] = useState<Date | null>(null);
    const [selectedHour, setSelectedHour] = useState<HourInterface>();
    const [showSummaryModal, setShowSummaryModal] = useState(false);

    const handleLoadSpecialistList = async () => {
        const response = await listSpecialist({
            size: 1,
            page: currentPage + 1,
            phrase: "",
        });
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            return;
        }
        setCurrentPage(response.currentPage);
        setSpecialistList(specialistList.concat(response.data));
        setTotalPages(response.totalPages);
        if (specialistList.length === 0) {
            setSpecialist(response.data[0]);
        }
    };

    const handleMonthChange = async () => {
        if (!specialist) return;
        if (!implant) return;
        const response = await getSpecialistAvailability(
            specialist?.id,
            Instant.now(),
            implant.duration
        );
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            return;
        }

        const dates: HourInterface[][] = [];
        for (const date of response) {
            const localDate = LocalDateTime.ofInstant(date);
            const dayOfMonth = localDate.dayOfMonth();
            if (!dates[dayOfMonth]) dates[dayOfMonth] = [];
            const hour = localDate.hour();
            const minute = localDate.minute();
            const hourString = hour < 10 ? `0${hour}` : `${hour}`;
            const minuteString = minute < 10 ? `0${minute}` : `${minute}`;
            const hourValue = `${hourString}:${minuteString}`;
            dates[dayOfMonth].push({
                text: hourValue,
                value: date,
            });
        }
        setAvailableDates(dates);
    };

    useEffect(() => {
        handleLoadSpecialistList();
    }, []);

    useEffect(() => {
        handleMonthChange();
        setSelectedHour(undefined);
    }, [specialist, implant, currentMonth]);

    return (
        <section className={style.create_appointment_page}>
            <h1>{t("createAppointmentPage.title")}</h1>
            <div className={style.appointment_container}>
                <div className={style.implant_display}>
                    <ImplantItem
                        implantId={implantId}
                        setImplant={setImplant}
                    />
                </div>
                <div className={style.bottom_wrapper}>
                    <div className={style.specialist_display}>
                        {specialistList?.map(
                            (spec: SpecialistListElementDto) => (
                                <SpecialistItem
                                    key={spec.id}
                                    specialist={spec}
                                    onClick={setSpecialist}
                                    isGreen={specialist?.id === spec.id}
                                />
                            )
                        )}
                        {totalPages !== currentPage && (
                            <>
                                <div style={{ marginTop: "1rem" }}></div>
                                <ActionButton
                                    title={t(
                                        "createAppointmentPage.loadMoreButton"
                                    )}
                                    icon={faArrowAltCircleRight}
                                    color="green"
                                    onClick={handleLoadSpecialistList}
                                />
                            </>
                        )}
                    </div>
                    <div className={style.date_display}>
                        <Calendar
                            size="xl"
                            className={style.calendar}
                            month={currentMonth}
                            onMonthChange={setCurrentMonth}
                            value={selectedDate}
                            onChange={setSelectedDate}
                            excludeDate={(date) => {
                                return !availableDates[date.getDate()];
                            }}
                            renderDay={(date) => {
                                const day = date.getDate();
                                const month = date.getMonth();
                                const color =
                                    !availableDates[day] ||
                                    currentMonth.getMonth() !== month
                                        ? "red"
                                        : "green";
                                return (
                                    <Indicator
                                        size={6}
                                        color={color}
                                        offset={8}
                                        zIndex={0}
                                        disabled={color === "red"}
                                    >
                                        <div>{day}</div>
                                    </Indicator>
                                );
                            }}
                        />
                        <div className={style.right_to_calendar}>
                            <div className={style.hour_picker}>
                                {availableDates[
                                    selectedDate?.getDate() ?? -1
                                ]?.map((hour) => (
                                    <HourItem
                                        key={hour.text}
                                        value={hour}
                                        onClick={(x: HourInterface) =>
                                            setSelectedHour(x)
                                        }
                                        isSelected={
                                            selectedHour?.text === hour.text
                                        }
                                    />
                                ))}
                            </div>
                            <div className={style.create_appointment}>
                                <ActionButton
                                    onClick={() => {
                                        setShowSummaryModal(true);
                                    }}
                                    isDisabled={!selectedHour}
                                    title={t(
                                        "createAppointmentPage.summaryButton"
                                    )}
                                    color="green"
                                    icon={faShoppingCart}
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <SummaryModal
                show={showSummaryModal}
                onClose={() => {
                    setShowSummaryModal(false);
                    handleMonthChange();
                    setSelectedHour(undefined);
                }}
                choosenDate={selectedHour}
                choosenSpecialist={specialist}
                choosenImplant={implant}
            />
        </section>
    );
};

export default CreateAppointmentPage;

interface SummaryModalInterface {
    show: boolean;
    onClose: () => void;
    choosenDate: HourInterface | undefined;
    choosenSpecialist: SpecialistListElementDto | undefined;
    choosenImplant: GetImplantResponse | undefined;
}

const SummaryModal = ({
    show,
    onClose,
    choosenDate,
    choosenSpecialist,
    choosenImplant,
}: SummaryModalInterface) => {
    const [dateString, setDateString] = useState("");
    const navigate = useNavigate();
    const { t } = useTranslation();

    const handleSubmit = async () => {
        if (!choosenDate || !choosenSpecialist || !choosenImplant) return;
        const response = await createAppointment({
            specialistId: choosenSpecialist.id,
            implantId: choosenImplant.id,
            startDate: choosenDate.value.toString(),
        });
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            return;
        }
        showNotification(
            successNotficiationItems(
                t("createAppointmentPage.successNotficiationItems")
            )
        );
        onClose();
        navigate("/visits");
    };

    useEffect(() => {
        if (!choosenDate) return;
        const localDate = LocalDateTime.ofInstant(choosenDate.value);
        const startString = localDate
            .truncatedTo(ChronoUnit.MINUTES)
            .toString()
            .replace("T", " ");

        setDateString(startString);
    }, [choosenDate]);

    const customStyles = {
        overlay: {
            backgroundColor: "rgba(0, 0, 0, 0.85)",
        },
        content: {
            top: "10vh",
            left: "10vw",
            width: "80vw",
            height: "80vh",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            backgroundColor: "transparent",
            border: "none",
        },
    };

    return (
        <ReactModal isOpen={show} style={customStyles} onAfterClose={onClose}>
            <div className={style.modal_content}>
                <div className={style.date_title}>
                    <h4>
                        {t("createAppointmentPage.choosenDate")} {dateString}
                    </h4>
                </div>
                <div className={style.choosen_implant}>
                    <h4>{t("createAppointmentPage.choosenImplant")}</h4>
                    <div>
                        <img
                            className={style.implant_image}
                            src={
                                choosenImplant?.image ??
                                "https://picsum.photos/200"
                            }
                            alt="implant"
                        />
                        <div>{choosenImplant?.name}</div>
                    </div>
                </div>
                <div className={style.choosen_specialist}>
                    <h2>
                        {t("createAppointmentPage.choosenSpecialist")}:{" "}
                        {choosenSpecialist?.firstName}{" "}
                        {choosenSpecialist?.lastName}
                    </h2>
                </div>
                <div style={{ width: "20rem" }}>
                    <ActionButton
                        title={t("createAppointmentPage.reserveButton")}
                        color="green"
                        icon={faShoppingCart}
                        onClick={() => {
                            handleSubmit();
                        }}
                    />
                </div>
            </div>
        </ReactModal>
    );
};

interface HourItemProps {
    value: HourInterface;
    onClick: (value: HourInterface) => void;
    isSelected: boolean;
}

const HourItem = ({ value, onClick, isSelected }: HourItemProps) => {
    const handleClick = () => {
        const text = value.text;
        const inst = value.value.toString();
        console.log({ text, inst });
        onClick(value);
    };

    return (
        <div
            className={style.hour_item}
            style={isSelected ? { backgroundColor: "#00b046" } : {}}
            onClick={handleClick}
        >
            <h2>{value.text}</h2>
        </div>
    );
};

interface ImplantItemProps {
    implantId: string | undefined;
    setImplant?: (implant: GetImplantResponse) => void;
}
const ImplantItem = ({ implantId, setImplant }: ImplantItemProps) => {
    const id = implantId;
    const [implant, setImplantInternal] = useState<GetImplantResponse>();

    const handleLoadImplant = async () => {
        if (!id) return;
        const response = await getImplant(id);
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            return;
        }
        setImplantInternal(response);
        if (setImplant) setImplant(response);
    };

    useEffect(() => {
        handleLoadImplant();
    }, [id]);

    return (
        <div className={style.implant}>
            <img
                className={style.implant_image}
                src={implant?.image ?? "https://picsum.photos/200"}
                alt="implant"
            />
            <div className={style.implant_title}>
                <h2>{implant?.name}</h2>
                <div className={style.implant_price_tag}>
                    {implant?.price} zł
                </div>
            </div>
        </div>
    );
};

interface SpecialistItemInterface {
    specialist: SpecialistListElementDto;
    onClick: (specialist: SpecialistListElementDto) => void;
    isGreen?: boolean;
}
const SpecialistItem = ({
    onClick,
    specialist,
    isGreen = false,
}: SpecialistItemInterface) => {
    const spec = specialist;

    const handleClick = () => {
        onClick(spec);
    };

    return (
        <div
            className={style.specialist_card}
            style={
                isGreen
                    ? {
                          boxShadow: "8px 8px 24px 0px rgb(0, 176, 70)",
                      }
                    : {}
            }
            onClick={handleClick}
        >
            <img
                className={style.specialist_image}
                src="https://picsum.photos/200"
                alt="specialist"
            />
            <h3 className={style.specialist_name}>
                {spec.firstName} {spec.lastName}
            </h3>
        </div>
    );
};
