import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";

const initialState = {
  followingAccounts: null,
  followerAccounts: null,
};





export const getAllAccounts = createAsyncThunk(
  "/api1/apps/v1.0/tweets/users/all",
  async (thunkAPI) => {
    const response = await axios({
      method: "get",
      url: "/api1/apps/v1.0/tweets/users/all",
      headers: {
        Authorization: "Bearer "+localStorage.getItem("token"),
      },
    });
    return response.data;
  }
);
export const getAccountsByUser = createAsyncThunk(
  "/api1/apps/v1.0/tweets/users/search",
  async ({username},thunkAPI) => {
    const response = await axios({
      method: "get",
      url: "/api1/apps/v1.0/tweets/users/search/"+username,
      headers: {
        Authorization: "Bearer "+localStorage.getItem("token"),
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      },
    });
    return response.data;
  }
);


export const AccountSlice = createSlice({
  name: "AccountSlice",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(getAllAccounts.fulfilled, (state, action) => {
      state.followerAccounts = action.payload;
    });
    builder.addCase(getAccountsByUser.fulfilled, (state, action) => {
      state.followerAccounts = action.payload;
    });
  },
});

export default AccountSlice.reducer;
