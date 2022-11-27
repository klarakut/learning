package learning.retrofitdemo.service;

import learning.retrofitdemo.models.LoveRequestDto;
import learning.retrofitdemo.models.LoveResponseDto;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Service
public class RetrofitLoveCalculator {

    public static final String nameOfApiUrl = "https://love-calculator.p.rapidapi.com";

    public LoveResponseDto percentageLove(LoveRequestDto requestDto) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(nameOfApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitLoveInterface retrofitLoveInterface = retrofit.create(RetrofitLoveInterface.class);
        Call<LoveResponseDto> call = retrofitLoveInterface.getLovePercentage(requestDto.getHerName(),requestDto.getHisName());
        LoveResponseDto responseDto = call.execute().body();
        return responseDto;
    }
}
