package learning.retrofitdemo.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WhatsUpInterface {

    @GET("/whatsapp.php")
    Call<Void> getMsg(@Query("phone") String phoneNumber,
                                    @Query("text") String message,
                                    @Query("apikey") String apiKey);
}
