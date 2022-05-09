import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import jwtDecode from "jwt-decode";

// TODO przenieść do .env / package.json
// const BASE_URL = "http://studapp.it.p.lodz.pl:8003/api"
const BASE_URL = "https://localhost:8181/api";
const TOKEN_STORAGE_KEY = "AUTH_TOKEN";

// TODO przenieść do własnego pliku
export interface Credentials {
  login: string;
  password: string;
}

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
    login: builder.mutation<string, Credentials>({
      query: (credentials: Credentials) => ({
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
  }),
});

export const { useLoginMutation } = api;
