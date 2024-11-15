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
    deletePost: (state, action) => {
      const postIdToDelete = action.payload;
      state.visitedCommunity.posts = state.visitedCommunity.posts.filter(
          (post) => post.id !== postIdToDelete
      );
    },
  },
});

export const { setVisitedCommunity, deletePost } = communitySlice.actions;

export default communitySlice.reducer;
