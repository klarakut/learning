package learning.retrofitdemo.controller;

import learning.retrofitdemo.models.ChuckResponseDto;
import learning.retrofitdemo.models.LoveRequestDto;
import learning.retrofitdemo.models.LoveResponseDto;
import learning.retrofitdemo.models.WhatsUppRequestDto;
import learning.retrofitdemo.service.ChuckServiceRetrofit;
import learning.retrofitdemo.service.RetrofitLoveCalculator;
import learning.retrofitdemo.service.WhatsAppRetrofit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class MainController {

    private final RetrofitLoveCalculator retrofitLoveCalculator;
    private final ChuckServiceRetrofit chuckServiceRetrofit;
    private final WhatsAppRetrofit whatsAppRetrofit;

    @Autowired
    public MainController(RetrofitLoveCalculator retrofitLoveCalculator, ChuckServiceRetrofit chuckServiceRetrofit, WhatsAppRetrofit whatsAppRetrofit) {
        this.retrofitLoveCalculator = retrofitLoveCalculator;
        this.chuckServiceRetrofit = chuckServiceRetrofit;
        this.whatsAppRetrofit = whatsAppRetrofit;
    }


    @GetMapping("/")
    public String mainPage(){
        return "index";
    }

    @GetMapping("/match")
    public String showMatch(){
        return "q";
    }

    @PostMapping("/match")
    public String match(Model model,
                        @RequestParam(value = "herName")String herName,
                        @RequestParam(value = "hisName")String hisName
            //, @RequestParam(value = "phoneNumber") String phoneNumber, @RequestParam(value = "message") String inputText
    ) throws IOException {
        LoveRequestDto loveRequestDto = new LoveRequestDto(herName,hisName);
        //WhatsUppRequestDto whatsUppRequestDto = new WhatsUppRequestDto(inputText,phoneNumber);
        LoveResponseDto responseDto = retrofitLoveCalculator.percentageLove(loveRequestDto);

        model.addAttribute("herName",loveRequestDto.getHerName());
        model.addAttribute("hisName", loveRequestDto.getHisName());
        model.addAttribute("loveMeter",responseDto.percentage + "points");
        model.addAttribute("loveMsg",responseDto.result);

        //whatsAppRetrofit.sendMsg(whatsUppRequestDto);
        return "match";
    }

    @GetMapping("/chuck")
    public String chuck(Model model) throws IOException{
        ChuckResponseDto dto = chuckServiceRetrofit.getChuckRandom();
        model.addAttribute("text",dto.value);
        return "index";
    }

    @GetMapping("/sendMsg")
    public String sendM(){
        return "whtsup";
    }

    @PostMapping("/sendM")
    public String sendMs (Model model,
                          @RequestParam(value = "phoneNumber") String phoneNumber,
                          @RequestParam(value = "inputText") String inputText)
        throws IOException {
            WhatsUppRequestDto whatsUppRequestDto = new WhatsUppRequestDto(inputText,phoneNumber);
            whatsAppRetrofit.sendMsg(whatsUppRequestDto);
            return "a";
    }
}
