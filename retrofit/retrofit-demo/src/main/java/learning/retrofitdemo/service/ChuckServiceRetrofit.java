package learning.retrofitdemo.service;

import learning.retrofitdemo.models.ChuckResponseDto;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Service
public class ChuckServiceRetrofit {
    public static final String API_URL = "https://matchilling-chuck-norris-jokes-v1.p.rapidapi.com";

    public ChuckResponseDto getChuckRandom() throws IOException{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ChuckCall chuckCall = retrofit.create(ChuckCall.class);
        Call<ChuckResponseDto> call = chuckCall.getChuck();
        ChuckResponseDto chuckResponseDto = call.execute().body();

        return chuckResponseDto;
    }
}
