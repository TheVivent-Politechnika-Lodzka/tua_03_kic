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

export interface AppointmentListElementDto {
    id: string;
    client: AccountDetails;
    specialist: AccountDetails;
    implant: ImplantDetails;
    status: Status;
    startDate: string;
    endDate:string;
    price:number;
    description: string;
}

interface ImplantDetails{
    id: string;
    version: number;
    name: string;
    description: string;
    manufacturer: string;
    price: number;
    archived: boolean;
    popularity: number;
    duration: number;
    img: string;
}

interface ListOwnAppointmentsRequest{
    page: number;
    size: number;
}

export interface ListOwnAppointmentsResponse {
    totalCount: number;
    totalPages: number;
    currentPage: number;
    data: AppointmentListElementDto[];
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
export async function listOwnAppointments(params:ListOwnAppointmentsRequest) {
    try{
        const { data, headers } = await axios.get<ListOwnAppointmentsResponse>(
            "/mop/list/visits/my",{ params, });
        return data;
    }
        catch (error) {
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
    try{
        const { data, headers } = await axios.get<AppointmentListElementDto>(
            "/mop/visit/"+ id);
            const etag = headers["etag"];
        return {data, etag};
    }
        catch (error) {
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
