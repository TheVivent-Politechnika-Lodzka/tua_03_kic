import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import jwtDecode from "jwt-decode";
import CredentialsDto from "./types/auth";
import PaginationParams from "./types/queryParams/paginationParams";
import {
  AccessLevelDto,
  AccountDto,
  AccountWithAccessLevelDto,
  ChangeOwnPasswordDto,
} from "./types/mok.dto";

interface addAccessLevelData {
  login: string;
  accessLevel: AccessLevelDto;
}

// TODO przenieść do .env / package.json
// const BASE_URL = "https://studapp.it.p.lodz.pl:8403/api"
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
    login: builder.mutation<string, CredentialsDto>({
      query: (credentials: CredentialsDto) => ({
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

    findAllUsers: builder.mutation<AccountDto[], PaginationParams>({
      query: ({ page, limit }: PaginationParams) => ({
        url: "/mok/account",
        method: "GET",
        params: { page, limit },
        responseHandler: async (response) => {
          if (response.ok) {
            return await response.json();
          }
        },
      }),
    }),

    editOwnAccount: builder.mutation<AccountDto, AccountWithAccessLevelDto>({
      query: (account: AccountWithAccessLevelDto) => ({
        url: "/mok/edit",
        method: "PUT",
        body: account,
        responseHandler: async (response) => {
          if (response.ok) {
            return await response.json();
          }
        },
      }),
    }),

    getAccountByLogin: builder.mutation<AccountWithAccessLevelDto, string>({
      query: (login: string) => ({
        url: "/mok/" + login,
        method: "GET",
        responseHandler: async (response) => {
          if (response.ok) {
            return await response.json();
          }
        },
      }),
    }),

    changeOwnPassword: builder.mutation<string, ChangeOwnPasswordDto>({
      query: (changeOwnPasswordDto: ChangeOwnPasswordDto) => ({
        url: "/mok/password",
        method: "PUT",
        body: changeOwnPasswordDto,
        responseHandler: async (response) => {
          if (response.ok) {
            return response.status;
          }
        },
      }),
    }),

    addAccessLevel: builder.mutation<string, addAccessLevelData>({
      query: (data: addAccessLevelData) => ({
        url: `/mok/access-level/${data.login}`,
        method: "PUT",
        body: data.accessLevel,
        responseHandler: async (response) => {
          return response.status;
        },
      }),
    }),
  }),
});

export const {
  useLoginMutation,
  useFindAllUsersMutation,
  useEditOwnAccountMutation,
  useGetAccountByLoginMutation,
  useChangeOwnPasswordMutation,
} = api;
