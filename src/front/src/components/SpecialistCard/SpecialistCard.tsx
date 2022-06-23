import { Image, Table } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { Url } from "url";
import AccessLevel from "../shared/AccessLevel/AccessLevel";
import style from "./style.module.scss";

interface SpecialistCardProps {
    firstName: string;
    lastName: string;
    email: string;
    tel: string;
    img?: string;
}

const SpecialistCard = (props: SpecialistCardProps) => {
    const { firstName, lastName, email, tel, img } = props;

    const { t } = useTranslation();

    return (
        <div className={style.specialist_card}>
            <div className={style.image}>
                {img ? (
                    <Image
                        radius={100}
                        src={img}
                        width={"8rem"}
                        height={"8rem"}
                        alt={t("specialistCard.alt")}
                    />
                ) : (
                    <Image
                        radius={100}
                        src={"http://www.w3.org/2000/svg"}
                        width={"8rem"}
                        height={"8rem"}
                        alt={t("specialistCard.alt")}
                    />
                )}
                <div className={style.access_level}>
                    <AccessLevel accessLevel="SPECIALIST" />
                </div>
            </div>
            <div className={style.table}>
                <Table>
                    <tbody>
                        <tr>
                            <th colSpan={2}>
                                <span className={style.span}>
                                    {firstName} {lastName}
                                </span>
                            </th>
                        </tr>
                        <tr>
                            <td>email:</td>
                            <td>{email}</td>
                        </tr>
                        <tr>
                            <td>{t("specialistCard.tel")}</td>
                            <td>{tel}</td>
                        </tr>
                    </tbody>
                </Table>
            </div>
        </div>
    );
};

export default SpecialistCard;
