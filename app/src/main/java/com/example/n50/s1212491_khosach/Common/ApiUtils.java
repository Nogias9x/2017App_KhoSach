package com.example.n50.s1212491_khosach.Common;

public class ApiUtils {

    public static final String BASE_URL = "https://ws-thich-truyen.herokuapp.com/";

    public static MyWebService getMyWebService() {
        return RetrofitClient.getClient(BASE_URL).create(MyWebService.class);
    }
}