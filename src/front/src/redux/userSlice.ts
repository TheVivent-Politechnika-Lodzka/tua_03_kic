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

export const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    logout: () => {
      return initialState;
    },
    login: (state, action) => {
      const data = action.payload;
      const res = {
        sub: data.sub,
        auth: data.auth.split(","),
        cur: data.auth.split(",")[0],
        exp: data.exp,
      };

      return { ...state, ...res };
    },
    changeLevel: (state, action) => {
      const data = action.payload;
      const res = {
        sub: data.sub,
        auth: data.auth,
        cur: data.auth[data.index],
        exp: data.exp,
      };

      return { ...state, ...res };
    },
  },
});

export const { login, logout, changeLevel } = userSlice.actions;
export default userSlice.reducer;
