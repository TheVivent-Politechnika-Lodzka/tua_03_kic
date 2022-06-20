import {
    faArrowAltCircleRight,
    faShoppingCart,
} from "@fortawesome/free-solid-svg-icons";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import style from "./style.module.scss";
import { Calendar } from "@mantine/dates";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import {
    getImplant,
    GetImplantResponse,
    getSpecialistAvailability,
    listSpecialist,
    SpecialistListElementDto,
    SpecialistListResponse,
} from "../../../../api/mop";
import { showNotification } from "@mantine/notifications";
import { failureNotificationItems } from "../../../../utils/showNotificationsItems";
import { Instant, LocalDateTime } from "@js-joda/core";

const CreateAppointmentPage = () => {
    const { implantId } = useParams();
    const navigate = useNavigate();
    const [specialistList, setSpecialistList] = useState<
        SpecialistListElementDto[]
    >([]);
    const [implant, setImplant] = useState<GetImplantResponse>();
    const [specialist, setSpecialist] = useState<SpecialistListElementDto>();
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [availableDates, setAvailableDates] = useState<string[][]>([]);
    const [currentMonth, setCurrentMonth] = useState(0);

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
        // setAvailableDates(response);
        const dates: string[][] = [];
        for (const date of response) {
            const localDate = LocalDateTime.ofInstant(date);
            const dayOfMonth = localDate.dayOfMonth();
            if (!dates[dayOfMonth]) dates[dayOfMonth] = [];
            const hour = localDate.hour();
            const minute = localDate.minute();
            dates[dayOfMonth].push(`${hour}:${minute}`);
        }
        console.log(dates);

        setAvailableDates(dates);
    };

    useEffect(() => {
        handleLoadSpecialistList();
    }, []);

    useEffect(() => {
        handleMonthChange();
    }, [specialist, implant]);

    return (
        <section className={style.create_appointment_page}>
            <h1>Zarezerwuj wizytę</h1>
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
                                    title="załaduj więcej"
                                    icon={faArrowAltCircleRight}
                                    color="green"
                                    onClick={handleLoadSpecialistList}
                                />
                            </>
                        )}
                    </div>
                    <div className={style.date_display}>
                        <Calendar size="xl" className={style.calendar} />
                        <div className={style.right_to_calendar}>
                            <div className={style.pick_hour}>pick hour</div>
                            <div className={style.create_appointment}>
                                <ActionButton
                                    onClick={() => {
                                        navigate(
                                            `/implant/${implantId}/create-appointment`
                                        );
                                    }}
                                    title="Przejdź do podsumowania"
                                    color="green"
                                    icon={faShoppingCart}
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
};

export default CreateAppointmentPage;

interface ImplantItemProps {
    implantId: string | undefined;
    setImplant: (implant: GetImplantResponse) => void;
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
        setImplant(response);
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
                {spec.name} {spec.surname}
            </h3>
        </div>
    );
};
