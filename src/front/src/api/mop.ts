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
        const { data } = await axios.get<ListOwnAppointmentsResponse>(
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
