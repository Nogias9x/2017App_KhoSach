package com.example.n50.s1212491_khosach.Common;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyWebService {

    // https://ws-thich-truyen.herokuapp.com/getAllBooksList
    @GET("/getAllBooksList")
    Call<List<Book>> getAllBooksList();


    // https://ws-thich-truyen.herokuapp.com/getChapterOfBook?bookId=2&chapterIndex=3
    @GET("/getChapterOfBook")
    Call<List<Chapter>> getAllChaptersOfBook(@Query("bookId") int bookId);


    // https://ws-thich-truyen.herokuapp.com/getChapterOfBook?bookId=2&chapterIndex=3
    @GET("/getChapterOfBook")
    Call<Chapter> getChapterOfBook(@Query("bookId") int bookId, @Query("chapterIndex") int chapterIndex);


//    @GET("/answers?order=desc&sort=activity&site=stackoverflow")
//    Call<SOAnswersResponse> getAnswers(@Query("tagged") String tags);
}