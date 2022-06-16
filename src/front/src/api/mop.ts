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
