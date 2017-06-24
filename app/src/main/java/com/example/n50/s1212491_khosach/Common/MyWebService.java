package com.example.n50.s1212491_khosach.Common;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyWebService {

//    https://ws-thich-truyen.herokuapp.com/getAllBooksList
    @GET("/getAllBooksList")
    Call<List<Book>> getAllBooksList();
//
//    @GET("/answers?order=desc&sort=activity&site=stackoverflow")
//    Call<SOAnswersResponse> getAnswers(@Query("tagged") String tags);
}