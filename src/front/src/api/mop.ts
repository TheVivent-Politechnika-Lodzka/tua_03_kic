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
