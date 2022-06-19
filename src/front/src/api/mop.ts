import axios from "axios";

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

interface ImplantDetails extends Taggable {
    name: string;
    description: string;
    manufacturer: string;
    price: number;
    archived: boolean;
    popularity: number;
    duration: number;
    image: string;
}

export interface GetImplantResponse extends ImplantDetails, Etag {}

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

/**
 * Pobierz szczegóły implantu
 *
 * @param id identyfikator implantu
 * @returns GetImplantResponse | {errorMessage, status}
 */
 export async function getImplant(id: string) {
    try {
        const { data, headers } = await axios.get<ImplantDetails>(
            `/mop/implant/details/${id}`
        );
        const etag = headers["etag"];
        return { ...data, etag } as GetImplantResponse;
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

export async function createImplant(params: CreateImplantRequest) {
    try {
        const { data } = await axios.get<CreateImplantResponse>(
            "/mop/implant/create",
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
