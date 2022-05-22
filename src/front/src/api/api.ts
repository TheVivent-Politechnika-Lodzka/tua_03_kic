import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import jwtDecode from "jwt-decode";
import {
    AccountActivationDto,
    AccountWithAccessLevelsDto,
    AccountWithAccessLevelsDtoWithLogin,
    AddAccessLevel,
    changeOwnPasswordDto,
    changePasswordDto,
    CreateAccountDto,
    LoginCredentials,
    RegisterClientConfirmDto,
    RegisterClientDto,
    RemoveAccessLevel,
    ResetPasswordTokenDto,
} from "./types/apiParams";
import {JWT} from "./types/common";
import {PaginationFilterParams, RemoveAccessLevelParams} from "./types/queryParams";

// TODO przenieść do .env / package.json
// const BASE_URL = "https://kic.agency:8403/api"
const BASE_URL = "https://localhost:8181/api";
const TOKEN_STORAGE_KEY = "AUTH_TOKEN";

const api = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: BASE_URL,
        // dodawaj token do wszystkich zapytan, jeżeli jest dostępny
        prepareHeaders: (headers) => {
            const token = localStorage.getItem(TOKEN_STORAGE_KEY);
            if (token) {
                headers.set("authorization", `Bearer ${token}`);
            }
            return headers;
        },
    }),
    // ENDPOINTY
    endpoints: (builder) => ({
        // LOGOWANIE
        login: builder.mutation<JWT, LoginCredentials>({
            query: (credentials: LoginCredentials) => ({
                url: "/mok/login",
                method: "POST",
                body: credentials,
                responseHandler: async (response) => {
                    if (response.ok) {
                        const token = await response.text();
                        localStorage.setItem(TOKEN_STORAGE_KEY, token);
                        return jwtDecode(token);
                    }
                },
            }),
        }),

        // REJESTRACJA - wysłanie danych
        registerAccount: builder.mutation<number, RegisterClientDto>({
            query: (clientAccountDto: RegisterClientDto) => ({
                url: "/mok/register",
                method: "POST",
                body: clientAccountDto,
                responseHandler: async (response) => {
                    return response.status;
                },
            }),
        }),

        // REJESTRACJA - potwierdzenie
        confirmRegistration: builder.mutation<number, RegisterClientConfirmDto>({
            query: (data: RegisterClientConfirmDto) => ({
                url: "/mok/register-confirm",
                method: "POST",
                body: data,
                responseHandler: async (response) => {
                    return response.status;
                },
            }),
        }),

        // ZNAJDŹ WSZYSTKICH UŻYTKOWNIKÓW
        findAllUsers: builder.mutation<AccountWithAccessLevelsDto[],
            PaginationFilterParams>({
            query: (params: PaginationFilterParams) => ({
                url: "/mok/list",
                method: "GET",
                params: params,
                responseHandler: async (response) => {
                    return await response.json();
                },
            }),
        }),

        // EDYTUJ WŁASNE KONTO
        editOwnAccount: builder.mutation<AccountWithAccessLevelsDto,
            AccountWithAccessLevelsDto>({
            query: (account: AccountWithAccessLevelsDto) => ({
                url: "/mok",
                method: "PUT",
                body: account,
                responseHandler: async (response) => {
                    return await response.json();
                },
            }),
        }),

        // EDYTUJ DANE KONTA INNEGO UŻYTKOWNIKA
        editOtherAccountData: builder.mutation<string,
            AccountWithAccessLevelsDtoWithLogin>({
            query: (data: AccountWithAccessLevelsDtoWithLogin) => ({
                url: `/mok/${data.login}`,
                method: "PUT",
                body: data.data,
                responseHandler: async (response) => {
                    return response.status;
                },
            }),
        }),

        // PRZEGLĄDAJ SZCZEGÓŁY WYBRANEGO KONTA
        getAccountByLogin: builder.mutation<AccountWithAccessLevelsDto, string>({
            query: (login: string) => ({
                url: `/mok/${login}`,
                method: "GET",
                responseHandler: async (response) => {
                    return await response.json();
                },
            }),
        }),

        // PRZEGLĄDAJ SZCZEGÓŁY SWOJEGO KONTA
        getOwnAccountDetails: builder.query<AccountWithAccessLevelsDto, void>({
            query: () => ({
                url: "/mok",
                method: "GET",
                responseHandler: async (response) => {
                    return await response.json();
                },
            }),
        }),

        // ZMIEŃ WŁASNE HASŁO
        changeOwnPassword: builder.mutation<string, changeOwnPasswordDto>({
            query: (changeOwnPasswordDto: changeOwnPasswordDto) => ({
                url: "/mok/password",
                method: "PATCH",
                body: changeOwnPasswordDto,
                responseHandler: async (response) => {
                    return await response.json();
                },
            }),
        }),

        // ZMIEŃ HASŁO INNEGO UŻYTKOWNIKA
        changeAccountPassword: builder.mutation<AccountWithAccessLevelsDto,
            changePasswordDto>({
            query: ({login, data}: changePasswordDto) => ({
                url: `/mok/password/${login}`,
                method: "PATCH",
                body: data,
                responseHandler: async (response) => {
                    return await response.json();
                },
            }),
        }),

        // DOŁĄCZ POZIOM DOSTĘPU DO KONTA
        addAccessLevel: builder.mutation<AccountWithAccessLevelsDto,
            AddAccessLevel>({
            query: ({login, accessLevel}: AddAccessLevel) => ({
                url: `/mok/access-level/${login}`,
                method: "PUT",
                body: accessLevel,
                responseHandler: async (response) => {
                    return await response.json();
                },
            }),
        }),

        // ODŁĄCZ POZIOM DOSTĘPU OD KONTA
        removeAccessLevel: builder.mutation<string, RemoveAccessLevel>({
            query: ({login, tag, accessLevel}: RemoveAccessLevel) => ({
                url: `/mok/access-level/${login}/${accessLevel}`,
                method: "DELETE",
                params: {eTag: tag},
                responseHandler: async (response) => {
                    return await response.json();
                },
            }),
        }),

        // UTWÓRZ KONTO
        createAccount: builder.mutation<AccountWithAccessLevelsDto,
            CreateAccountDto>({
            query: (data: CreateAccountDto) => ({
                url: "/mok/create",
                method: "PUT",
                body: data,
                responseHandler: async (response) => {
                    return await response.json();
                },
            }),
        }),

        // ZABLOKUJ KONTO
        deactivateAccount: builder.mutation<AccountWithAccessLevelsDto,
            AccountActivationDto>({
            query: ({login, tag}: AccountActivationDto) => ({
                url: `/mok/deactivate/${login}`,
                method: "PATCH",
                body: {ETag: tag},
                responseHandler: async (response) => {
                    return await response.json();
                },
            }),
        }),

        // ODBLOKUJ KONTO
        activateAccount: builder.mutation<AccountWithAccessLevelsDto,
            AccountActivationDto>({
            query: ({login, tag}: AccountActivationDto) => ({
                url: `/mok/activate/${login}`,
                method: "PATCH",
                body: {ETag: tag},
                responseHandler: async (response) => {
                    return await response.json();
                },
            }),
        }),

        // ZRESETUJ HASŁO - wysłanie prośby
        resetPasswordRequest: builder.mutation<number, string>({
            query: (login: string) => ({
                url: `/mok/reset-password/${login}`,
                method: "POST",
                responseHandler: async (response) => {
                    return response.status;
                },
            }),
        }),

        // ZRESETUJ HASŁO - zmiana hasła
        resetPasswordChange: builder.mutation<number, ResetPasswordTokenDto>({
            query: (token: ResetPasswordTokenDto) => ({
                url: `/mok/reset-password-token`,
                method: "POST",
                body: token,
                responseHandler: async (response) => {
                    return response.status;
                },
            }),
        }),
    }),
});

export const {
    useLoginMutation,
    useAddAccessLevelMutation,
    useChangeAccountPasswordMutation,
    useChangeOwnPasswordMutation,
    useConfirmRegistrationMutation,
    useEditOtherAccountDataMutation,
    useEditOwnAccountMutation,
    useFindAllUsersMutation,
    useGetAccountByLoginMutation,
    useGetOwnAccountDetailsQuery,
    useRemoveAccessLevelMutation,
    useRegisterAccountMutation,
    useResetPasswordRequestMutation,
    useResetPasswordChangeMutation,
    useActivateAccountMutation,
    useDeactivateAccountMutation,
    useCreateAccountMutation,
} = api;
