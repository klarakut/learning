package learning.retrofitdemo.service;

import learning.retrofitdemo.models.WhatsUppRequestDto;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;

@Service
public class WhatsAppRetrofit {

    public final String apiKey = "#";
    public final String whats_Url = "https://api.callmebot.com";

    //public void sendMsg(String message){}

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(whats_Url)
            .build();

    public void sendMsg(WhatsUppRequestDto whatsUppRequestDto) throws IOException {
       WhatsUpInterface whatsUpInterface = retrofit.create(WhatsUpInterface.class);
       Call<Void> call = whatsUpInterface.getMsg(whatsUppRequestDto.getPhoneNumber(),whatsUppRequestDto.getText(),apiKey);
       call.execute().body();
    }
}
