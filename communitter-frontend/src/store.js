import { configureStore } from "@reduxjs/toolkit";
import userReducer from "./features/userSlice";
import communityReducer from "./features/communitySlice";
import errorReducer from "./features/errorSlice";

const store = configureStore({
  reducer: {
    user: userReducer,
    community: communityReducer,
    error: errorReducer,
  },
});
export default store;
