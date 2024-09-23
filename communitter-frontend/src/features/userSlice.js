import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  loggedInUser: {
    id: "",
    about: "",
    avatar: "",
    header: "",
    email: "",
    username: "",
    subscriptions: [],
  },
  visitedUser: {
    id: "",
    about: "",
    avatar: "",
    header: "",
    email: "",
    username: "",
    subscriptions: [],
  },
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setLoggedInUser: (state, action) => {
      state.loggedInUser = action.payload;
    },
    setVisitedUser: (state, action) => {
      state.visitedUser = action.payload;
    },
  },
});

export const { setLoggedInUser, setVisitedUser } = userSlice.actions;

export default userSlice.reducer;
