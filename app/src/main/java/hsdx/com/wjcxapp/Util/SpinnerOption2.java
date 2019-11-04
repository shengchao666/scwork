package hsdx.com.wjcxapp.Util;

public class SpinnerOption2 {

    private String key = "";
    private String value = "";

    public SpinnerOption2(){
        key = "";
        value = "";
    }

    public SpinnerOption2(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
