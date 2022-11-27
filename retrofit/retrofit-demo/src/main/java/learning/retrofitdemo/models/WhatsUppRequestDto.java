package learning.retrofitdemo.models;

public class WhatsUppRequestDto {

    String text;
    String phoneNumber;
    //String apiKey;

    /*public WhatsUppRequestDto(String message, String phoneNumber, String apiKey) {
        this.text = message;
        this.phoneNumber = phoneNumber;
    }*/

    public WhatsUppRequestDto(String text, String phoneNumber) {
        this.text = text;
        this.phoneNumber = phoneNumber;
    }

    public String getText() {
        return text;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    //public String getApiKey() {return apiKey;}
}
