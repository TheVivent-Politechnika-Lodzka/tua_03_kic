import { createSlice } from "@reduxjs/toolkit";



  interface UserState {
    sub: string;
    auth: string[] | string;
    exp: string;
  }
  

  const initialState: UserState = {
    sub: "",
    auth: [],
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
      return { ...state, ...action.payload.data };
    },
    relogin: (state, action) => {
      return { ...state, ...action.payload };
    },
  },
});

export const {login, logout , relogin} = userSlice.actions;
export default userSlice.reducer;