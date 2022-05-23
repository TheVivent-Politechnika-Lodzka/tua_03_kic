import { createSlice } from "@reduxjs/toolkit";

interface UserState {
  sub: string;
  auth: string[];
  cur: string;
  exp: string;
}

const initialState: UserState = {
  sub: "",
  auth: [],
  cur: "",
  exp: "",
};

const ACCESS_LEVEL = "currentAccessLevel";

export const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    logout: () => {
      localStorage.setItem(ACCESS_LEVEL, "");
      return initialState;
    },
    login: (state, action) => {
      const data = action.payload;

      if (
        localStorage.getItem(ACCESS_LEVEL) ||
        localStorage.getItem(ACCESS_LEVEL) !== ""
      ) {
        const res = {
          sub: data.sub,
          auth: data.auth.split(","),
          cur: localStorage.getItem(ACCESS_LEVEL),
          exp: data.exp,
        };
        return { ...state, ...res };
      } else {
        const res = {
          sub: data.sub,
          auth: data.auth.split(","),
          cur: data.auth.split(",")[0],
          exp: data.exp,
        };
        localStorage.setItem(ACCESS_LEVEL, res.cur);
        return { ...state, ...res };
      }
    },
    changeLevel: (state, action) => {
      const data = action.payload;
      const res = {
        sub: data.sub,
        auth: data.auth,
        cur: data.auth[data.index],
        exp: data.exp,
      };
      localStorage.setItem(ACCESS_LEVEL, res.cur);
      return { ...state, ...res };
    },
  },
});

export const { login, logout, changeLevel } = userSlice.actions;
export default userSlice.reducer;
