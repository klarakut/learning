package learning.retrofitdemo.service;

import learning.retrofitdemo.models.ChuckResponseDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ChuckCall {
    @GET("/jokes/random")
    @Headers({
            "accept:application/json",
            "#",
	        "#"
    })
    Call<ChuckResponseDto> getChuck();

}
