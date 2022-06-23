import { useEffect, useState } from "react";
import { listSpecialist, SpecialistListElementDto } from "../../../api/mop";
import SpecialistCard from "../../../components/SpecialistCard/SpecialistCard";
import ReactLoading from "react-loading";

import style from "./style.module.scss";
import { useTranslation } from "react-i18next";
import Pagination from "../../../components/Pagination/Pagination";

const SpecialistListPage = () => {
    const [specialists, setSpecialists] =
        useState<SpecialistListElementDto[]>();

    const { t } = useTranslation();

    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [pagination, setPagination] = useState<Pagination>({
        currentPage: 1,
        pageSize: 6,
        totalPages: 0,
    });
    const [error, setError] = useState<ApiError>();
    const [phrase, setPhrase] = useState<string>(" ");
    const [rerender, setRerender] = useState<boolean>(false);

    const handleGetSpecialist = async () => {
        try {
            setLoading({ ...loading, actionLoading: true });
            const data = await listSpecialist({
                page: pagination?.currentPage as number,
                size: pagination?.pageSize as number,
                phrase: phrase,
            });
            if ("errorMessage" in data) return;
            setSpecialists(data.data);
            setPagination({ ...pagination, totalPages: data.totalPages });
            setLoading({ pageLoading: false, actionLoading: false });
            setRerender(false);
        } catch (error: ApiError | any) {
            setLoading({ pageLoading: false, actionLoading: false });
            setError(error);
            setRerender(false);
            console.error(`${error?.status} ${error?.errorMessage}`);
        }
    };

    useEffect(() => {
        handleGetSpecialist();
    }, [pagination.currentPage]);

    useEffect(() => {
        if (phrase.length === 0) {
            handleGetSpecialist();
            return;
        }
        if (phrase.length < 3) return;
        const timer = setTimeout(() => {
            handleGetSpecialist();
        }, 500);
        return () => clearTimeout(timer);
    }, [phrase]);

    return (
        <div>
            <div className={style.specialist_list}>
                {loading.pageLoading ? (
                    <ReactLoading
                        type="cylon"
                        color="#fff"
                        width="10rem"
                        height="10rem"
                    />
                ) : (
                    <>
                        {specialists?.map((specialist) => (
                            <div className={style.list}>
                                <SpecialistCard
                                    firstName={specialist.firstName}
                                    lastName={specialist.lastName}
                                    email={specialist.email}
                                    tel={specialist.phoneNumber}
                                    img={specialist.url}
                                />
                            </div>
                        ))}
                    </>
                )}
            </div>
            <Pagination
                pagination={pagination}
                handleFunction={setPagination}
            />
        </div>
    );
};

export default SpecialistListPage;
