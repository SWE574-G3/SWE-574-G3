import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  visitedCommunity: {
    id: "",
    name: "",
    about: "",
    public: true,
    templates: [],
    posts: [],
    subscriptions: [],
  },
};

const communitySlice = createSlice({
  name: "community",
  initialState,
  reducers: {
    setVisitedCommunity: (state, action) => {
      state.visitedCommunity = action.payload;
    },
  },
});

export const { setVisitedCommunity } = communitySlice.actions;

export default communitySlice.reducer;
