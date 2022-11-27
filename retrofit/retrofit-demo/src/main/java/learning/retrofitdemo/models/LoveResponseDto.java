package learning.retrofitdemo.models;

public class LoveResponseDto {
    public String fname;
    public String sname;
    public String result;
    public String percentage;

    public LoveResponseDto(String result, String percentage, String fname, String sname) {
        this.result = result;
        this.percentage = percentage;
        this.sname = sname;
        this.fname = fname;
    }
}
