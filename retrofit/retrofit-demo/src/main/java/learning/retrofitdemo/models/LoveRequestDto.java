package learning.retrofitdemo.models;

public class LoveRequestDto {

    String herName;
    String hisName;

    public LoveRequestDto(String herName, String hisName) {
        this.herName = herName;
        this.hisName = hisName;
    }

    public String getHerName() {
        return herName;
    }

    public String getHisName() {
        return hisName;
    }
}
