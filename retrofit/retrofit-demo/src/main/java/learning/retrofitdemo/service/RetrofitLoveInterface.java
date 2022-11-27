package learning.retrofitdemo.service;

import learning.retrofitdemo.models.LoveResponseDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RetrofitLoveInterface {

    @GET("/getPercentage")
    @Headers({
            //"accept:application/json",
            "#",
            "X-RapidAPI-Host:love-calculator.p.rapidapi.com"
    })
    Call<LoveResponseDto> getLovePercentage(@Query("sname") String herName,
                                            @Query("fname") String hisName);
}
