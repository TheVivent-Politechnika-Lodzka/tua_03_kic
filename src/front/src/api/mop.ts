import { Instant } from "@js-joda/core";
import axios from "axios";
import { StringLiteralLike } from "typescript";

//----------------------------------------------------- MOP 1 -------------------------------------------------------//

export interface CreateImplantResponse extends ImplantDto, Etag {}

export async function createImplant(newImplant: CreateImplantDto) {
    try {
        const { data, headers } = await axios.put<ImplantDto>(
            "mop/implant/create",
            newImplant
        );
        const newEtag = headers["etag"];
        return { ...data, etag: newEtag } as CreateImplantResponse;
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//----------------------------------------------------- MOP 2 -------------------------------------------------------//

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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//----------------------------------------------------- MOP 3 -------------------------------------------------------//

export interface EditImplantRequest extends ImplantDto, Etag {}
export interface EditImplantResponse extends ImplantDto, Etag {}

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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//----------------------------------------------------- MOP 4 -------------------------------------------------------//

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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//----------------------------------------------------- MOP 5 -------------------------------------------------------//

export interface ListImplantsRequest extends PaginationRequest {
    archived?: boolean;
}

export interface ListImplantResponse extends Pagination {
    data: ImplantDto[];
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//----------------------------------------------------- MOP 6 -------------------------------------------------------//

export interface SpecialistListElementDto {
    id: string;
    firstName: string;
    lastName: string;
    url: string;
    email: string;
    phoneNumber: string;
}

export interface SpecialistListResponse extends Pagination {
    data: SpecialistListElementDto[];
}

export async function listSpecialist(params: PaginationRequest) {
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//----------------------------------------------------- MOP 7 -------------------------------------------------------//

export interface AppointmentListResponse extends Pagination {
    data: AppointmentListElementDto[];
}

export async function listAppointments(params: PaginationRequest) {
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//----------------------------------------------------- MOP 8 -------------------------------------------------------//

export async function listMyAppointments(params: PaginationRequest) {
    try {
        const { data } = await axios.get<AppointmentListResponse>(
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//----------------------------------------------------- MOP 9 -------------------------------------------------------//

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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//---------------------------------------------------- MOP 10 -------------------------------------------------------//

interface EditOwnAppointmentRequest extends Taggable, Etag {
    description: string;
    startDate: Instant;
    status: Status;
}

interface EditOwnAppointmentResponse extends AppointmentDto, Etag {}

export async function editOwnAppointment(params: EditOwnAppointmentRequest) {
    try {
        const { etag, ...body } = params;
        const { data, headers } = await axios.put<AppointmentDto>(
            `/mop/edit/visit/my/${body.id}`,
            body,
            {
                headers: {
                    "If-Match": etag,
                },
            }
        );
        const newEtag = headers["etag"];
        return { ...data, etag: newEtag } as EditOwnAppointmentResponse;
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//---------------------------------------------------- MOP 11 -------------------------------------------------------//

interface EditAppointmentRequest extends Taggable, Etag {
    description: string;
    status: Status;
}

interface EditAppointmentResponse extends AppointmentDto, Etag {}

export async function editAppointmentByAdmin(params: EditAppointmentRequest) {
    try {
        const { etag, ...body } = params;
        const { data, headers } = await axios.put<AppointmentDto>(
            `/mop/edit/visit/${body.id}`,
            body,
            {
                headers: {
                    "If-Match": etag,
                },
            }
        );
        const newEtag = headers["etag"];
        return { ...data, etag: newEtag } as EditAppointmentResponse;
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//---------------------------------------------------- MOP 12 -------------------------------------------------------//

export interface CancelOwnAppointmentResponse extends AppointmentDto, Etag {}

export async function cancelOwnAppointment(
    appointmentId: string,
    etag: string
) {
    try {
        const { data, headers } = await axios.patch<AppointmentDto>(
            `/mop/visit/cancel/my/${appointmentId}`,
            {},
            {
                headers: {
                    "If-Match": etag,
                },
            }
        );
        const newEtag = headers["etag"];
        return { ...data, etag: newEtag } as CancelOwnAppointmentResponse;
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//---------------------------------------------------- MOP 13 -------------------------------------------------------//

export interface CancelAppointmentResponse extends AppointmentDto, Etag {}

/**
 * Odwołaj dowolną wizytę
 *
 * @param id identyfikator wizyty
 * @returns GetAppointmentResponse | {errorMessage, status}
 */
export async function cancelAnyAppointment(id: string, etag: string) {
    try {
        const { data, headers } = await axios.delete<AppointmentDto>(
            `/mop/cancel/visit/${id}`,
            {
                headers: {
                    "If-Match": etag,
                },
            }
        );
        const newEtag = headers["etag"];
        return { ...data, etag: newEtag } as CancelAppointmentResponse;
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//---------------------------------------------------- MOP 14 -------------------------------------------------------//

export interface FinishAppointmentResponse
    extends AppointmentListElementDto,
        Etag {}

/**
 * Odwołaj dowolną wizytę
 *
 * @param id identyfikator wizyty
 * @returns GetAppointmentResponse | {errorMessage, status}
 */
export async function finishAppointment(id: string, etag: string) {
    try {
        const { data, headers } = await axios.patch<AppointmentDto>(
            `/mop/finish/visit/${id}`,
            {},
            {
                headers: {
                    "If-Match": etag,
                },
            }
        );
        const newEtag = headers["etag"];
        return { ...data, etag: newEtag } as FinishAppointmentResponse;
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//---------------------------------------------------- MOP 15 -------------------------------------------------------//

interface CreateImplantReviewResponse extends ImplantReview {}

interface CreateImplantReviewDto {
    implantId: string;
    clientLogin: string;
    review: string;
    rating: number;
}

export async function addImplantsReview(request: CreateImplantReviewDto) {
    try {
        const { data } = await axios.put<ImplantReview>(
            `/mop/implant/review`,
            request
        );
        return data as CreateImplantReviewResponse;
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//---------------------------------------------------- MOP 16 -------------------------------------------------------//

export async function deleteImplantsReview(implantId: string) {
    try {
        const { data, status } = await axios.delete(
            `/mop/implant/review/${implantId}`
        );
        return { status };
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//---------------------------------------------------- MOP 17 -------------------------------------------------------//

interface GetAppointmentDetailsResponse extends AppointmentDto, Etag {}

export async function getAppointmentDetails(id: string) {
    try {
        const { data, headers } = await axios.get<AppointmentDto>(
            "/mop/visit/" + id
        );
        const etag = headers["etag"];
        return { ...data, etag } as GetAppointmentDetailsResponse;
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

//-------------------------------------------------------------------------------------------------------------------//
//
//
//
//---------------------------------------------------- MOP 18 -------------------------------------------------------//

interface GetImplantsReviewsRequest extends PaginationRequest {
    implantId: string;
}

export interface GetImplantsReviewsResponse extends Pagination {
    data: ImplantReview[];
}

export async function getImplantsReviews(request: GetImplantsReviewsRequest) {
    try {
        const { data } = await axios.get<GetImplantsReviewsResponse>(
            `/mop/implant/${request.implantId}/reviews?page=${request.page}&size=${request.size}`
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

//-------------------------------------------------------------------------------------------------------------------//
