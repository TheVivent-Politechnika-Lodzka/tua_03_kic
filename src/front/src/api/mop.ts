import { Instant } from "@js-joda/core";
import axios from "axios";
import { StringLiteralLike } from "typescript";

export interface ListImplantsRequest {
    page: number;
    size: number;
    phrase?: string;
    archived?: boolean;
}

export interface ImplantListElementDto {
    id: string;
    name: string;
    description: string;
    price: number;
    url: string;
}

export interface ListImplantResponse {
    totalCounts: number;
    totalPages: number;
    currentPage: number;
    data: ImplantListElementDto[];
}

export interface AppointmentListElementDto {
    id: string;
    client: AccountDetails;
    specialist: AccountDetails;
    implant: ImplantDto;
    status: Status;
    startDate: string;
    endDate: string;
    price: number;
    description: string;
    version: number;
}

interface ListOwnAppointmentsRequest {
    page: number;
    size: number;
}

export interface ListOwnAppointmentsResponse {
    totalCount: number;
    totalPages: number;
    currentPage: number;
    data: AppointmentListElementDto[];
}

export interface GetImplantResponse extends ImplantDto, Etag {}

/**
 * zwraca listę implantów i informacje o paginacji
 * @params page aktualna strone,
 * @params size ilosc pozycji na stronie
 * @params phrase szukana fraze
 * @params archived implant zarchiwizowane
 * @returns @example {totalCount, totalPages, currentPage, data} | {errorMessage, status}
 */
export async function listImplants(params: ListImplantsRequest) {
    try {
        const { data } = await axios.get<ListImplantResponse>(
            "/mop/implant/list",
            {
                params,
            }
        );
        return data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return {
                errorMessage: error.response.data as string,
                status: error.response.status,
            } as ApiError;
        }
        throw error;
    }
}
export async function listOwnAppointments(params: ListOwnAppointmentsRequest) {
    try {
        const { data } = await axios.get<ListOwnAppointmentsResponse>(
            "/mop/list/visits/my",
            { params }
        );
        return data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return {
                errorMessage: error.response.data as string,
                status: error.response.status,
            } as ApiError;
        }
        throw error;
    }
}
export async function getAppointmentDetails(id: string) {
    try {
        const { data, headers } = await axios.get<AppointmentListElementDto>(
            "/mop/visit/" + id
        );
        const etag = headers["etag"];
        return { data, etag };
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return {
                errorMessage: error.response.data as string,
                status: error.response.status,
            } as ApiError;
        }
        throw error;
    }
}

export interface EditImplantRequest extends ImplantDto, Etag {}
export interface EditImplantResponse extends ImplantDto, Etag {}

/**
 *
 * @param id - identyfikator wszczepu
 * @param implantDetails - nowe dane wszczepu i etag
 * @returns @example EditImplantResponse | {errorMessage, status}
 */
export async function editImplant(
    id: string,
    implantDetails: EditImplantRequest
) {
    try {
        const { etag, ...implant } = implantDetails;
        const { data, headers } = await axios.put(
            `/mop/implant/edit/${id}`,
            implant,
            {
                headers: {
                    "If-Match": etag,
                },
            }
        );
        const newEtag = headers["etag"];
        return { ...data, etag: newEtag } as EditImplantResponse;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return {
                errorMessage: error.response.data as string,
                status: error.response.status,
            } as ApiError;
        }
        throw error;
    }
}

export interface GetImplantResponse extends ImplantDto, Etag {}
/**
 * Pobierz szczegóły implantu
 *
 * @param id identyfikator implantu
 * @returns GetImplantResponse | {errorMessage, status}
 */
export async function getImplant(id: string) {
    try {
        const { data, headers } = await axios.get<ImplantDto>(
            `/mop/implant/details/${id}`
        );
        const etag = headers["etag"];
        return { ...data, etag: etag } as GetImplantResponse;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return {
                errorMessage: error.response.data as string,
                status: error.response.status,
            } as ApiError;
        }
        throw error;
    }
}
export interface AppointmentListElementDto {
    id: string;
    description: string;
    client_url: string;
    specialist_url: string;
}

export interface AppointmentListResponse {
    totalCounts: number;
    totalPages: number;
    currentPage: number;
    data: AppointmentListElementDto[];
}

export interface AppointmentsListRequest {
    page: number;
    size: number;
    phrase?: string;
}

export async function listAppointments(params: AppointmentsListRequest) {
    try {
        const { data } = await axios.get<AppointmentListResponse>(
            "/mop/list/visits",
            {
                params,
            }
        );
        return data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return {
                errorMessage: error.response.data as string,
                status: error.response.status,
            } as ApiError;
        }
        throw error;
    }
}
/**
 * tworzy wszczep
 * @params name aktualna strone,
 * @params description ilosc pozycji na stronie
 * @params manufacturer szukana fraze
 * @params price implant zarchiwizowane
 * @params duration implant zarchiwizowane
 * @params url implant zarchiwizowane
 * @returns @example {archived, name, description, manufacturer, price, duration, id, image, popularity, version} | {errorMessage, status}
 */
export interface CreateImplantRequest {
    name: string;
    description: string;
    manufacturer: string;
    price: number;
    duration: number;
    url: string;
}
export interface CreateImplantResponse {
    archived: boolean;
    name: string;
    description: string;
    manufacturer: string;
    price: number;
    duration: number;
    id: string;
    image: string;
    popularity: number;
    version: number;
}

interface EditOwnAppointmentRequest {
    id: string;
    version: number;
    description: string;
    etag: string;
    startDate: Instant;
    status: string;
}

interface EditOwnAppointmentRespone {
    appointment: AppointmentListElementDto;
    etag: string;
}

export async function createImplant(params: CreateImplantRequest) {
    try {
        const { data } = await axios.put<CreateImplantResponse>(
            "/mop/implant/create",

            params
        );
        return data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return {
                errorMessage: error.response.data as string,
                status: error.response.status,
            } as ApiError;
        }
        throw error;
    }
}

export async function editOwnAppointment(params: EditOwnAppointmentRequest) {
    try {
        const {etag, ...body} = params
        const { data, headers } = await axios.put<EditOwnAppointmentRespone>(
            `/mop/edit/visit/my/${body.id}`,
            body,
            {
                headers: {
                    "If-Match": etag,
                },
            }
        );
        const newEtag = headers["etag"];
        return {...data, etag:newEtag };
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return {
                errorMessage: error.response.data as string,
                status: error.response.status,
            } as ApiError;
        }
        throw error;
    }
}

//----------------------------------------------------- MOP 6 -------------------------------------------------------//
export interface SpecialistListElementDto {
    id: string;
    name: string;
    surname: string;
    email: string;
    phoneNumber: string;
}

export interface SpecialistListResponse {
    totalCounts: number;
    totalPages: number;
    currentPage: number;
    data: SpecialistListElementDto[];
}

export interface SpecialistListRequest {
    page: number;
    size: number;
    phrase?: string;
}

export async function listSpecialist(params: SpecialistListRequest) {
    try {
        const { data } = await axios.get<SpecialistListResponse>(
            "/mop/specialist/list",
            {
                params,
            }
        );
        return data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return {
                errorMessage: error.response.data as string,
                status: error.response.status,
            } as ApiError;
        }
        throw error;
    }
}
//------------------------------------------------- KONIEC MOP 6 ----------------------------------------------------//
//
//
//
//----------------------------------------------------- MOP 2 -------------------------------------------------------//

export interface ArchiveImplantRequest extends Etag {}
export interface ArchiveImplantResponse extends ImplantDto, Etag {}

export async function archiveImplant(id: string, implantEtag: string) {
    try {
        const { data, headers } = await axios.patch(
            `/mop/implant/archive/${id}`,
            {},
            {
                headers: {
                    "If-Match": implantEtag,
                },
            }
        );
        const newEtag = headers["etag"];
        return { ...data, etag: newEtag } as ArchiveImplantResponse;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return {
                errorMessage: error.response.data as string,
                status: error.response.status,
            } as ApiError;
        }
        throw error;
    }
}
//------------------------------------------------- KONIEC MOP 2 ----------------------------------------------------//

//----------------------------------------------------- MOP 9 -------------------------------------------------------//

export async function getSpecialistAvailability(
    specialistId: string,
    month: Instant,
    duration: number
) {
    try {
        const { data } = await axios.get<string[]>(
            `/mop/specialists/${specialistId}/availability/${month.toString()}/${duration}`
        );

        const availability = data.map((day) => {
            const tmp = Instant.parse(day);
            return tmp;
        });

        return availability;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return {
                errorMessage: error.response.data as string,
                status: error.response.status,
            } as ApiError;
        }
        throw error;
    }
}

interface CreateAppointmentRequest {
    specialistId: string;
    implantId: string;
    startDate: string;
}

interface CreateAppointmentResponse extends AppointmentDto, Etag {}

export async function createAppointment(request: CreateAppointmentRequest) {
    try {
        const { data, headers } = await axios.post<AppointmentDto>(
            `/mop/visit/create`,
            request
        );

        const etag = headers["etag"];
        return { ...data, etag } as CreateAppointmentResponse;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return {
                errorMessage: error.response.data as string,
                status: error.response.status,
            } as ApiError;
        }
        throw error;
    }
}

//------------------------------------------------- KONIEC MOP 6 ----------------------------------------------------//
