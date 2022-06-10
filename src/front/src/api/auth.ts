import axios, { AxiosResponse } from "axios";

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
}

/**
 *
 * @param login    - login
 * @param password - hasło
 * @returns @example {accessToken, refreshToken} | {errorMessage, status}
 */
export async function login(login: string, password: string) {
  try {
    const { data } = await axios.post<LoginResponse>("/mok/login", {
      login,
      password,
    });

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

export interface RegisterRequest {
  login: string;
  password: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  pesel: string;
  language: {
    language: string;
  };
  captcha: string;
}

/**
 * metoda może dłużej się ładować, ponieważ wysyła maila
 *
 * @example
 *    {
 *      login:        string;
 *      password:     string;
 *      firstName:    string;
 *      lastName:     string;
 *      email:        string;
 *      phoneNumber:  string;
 *      pesel:        string;
 *      language: {
 *        language:   string;
 *      };
 *    }
 *
 * @param newAccount - konto do utworzenia
 * @returns @example {status} | {errorMessage, status}
 */
export async function register(newAccount: RegisterRequest) {
  try {
    const { status } = await axios.post("/mok/register", newAccount);

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

/**
 *
 * @param confirmationToken - token z linka w mailu
 * @returns @example {status} | {errorMessage, status}
 */
export async function confirmRegistration(confirmationToken: string) {
  try {
    const { status } = await axios.post("/mok/register-confirm", {
      token: confirmationToken,
    });

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

/**
 *
 * @param refreshToken - token odświeżania
 * @returns @example {accessToken, refreshToken} | {errorMessage, status}
 */
export async function refreshToken(refreshToken: string) {
  try {
    const { data } = await axios.post<LoginResponse>("/mok/refresh-token", {
      refreshToken,
    });

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
 *
 * @param login - login
 * @returns @example {status} | {errorMessage, status}
 */
export async function resetPassword(login: string) {
  try {
    const { status } = await axios.post(`/mok/reset-password/${login}`);

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

/**
 *
 * @param confirmationToken - token z linka w mailu
 * @param newPassword       - nowe hasło
 * @returns @example {status} | {errorMessage, status}
 */
export async function confirmResetPassword(
  confirmationToken: string,
  newPassword: string
) {
  try {
    const { status } = await axios.post("/mok/reset-password-token", {
      token: confirmationToken,
      password: newPassword,
    });

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
