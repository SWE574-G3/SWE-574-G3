import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  errorMessage: "",
};

const errorSlice = createSlice({
  name: "error",
  initialState,
  reducers: {
    setErrorMessage: (state, action) => {
      state.errorMessage = action.payload;
    },
  },
});

export const { setErrorMessage } = errorSlice.actions;

export default errorSlice.reducer;
