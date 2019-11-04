package hsdx.com.wjcxapp.Util;

public class SpinnerOption3 {

    private String key = "";
    private String value = "";
    private String parmThree = "";

    public SpinnerOption3() {
        key = "";
        value = "";
        parmThree = "";
    }

    public SpinnerOption3(String key, String value, String parmThree) {
        this.key = key;
        this.value = value;
        this.parmThree = parmThree;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public String getParmThree() {
        return parmThree;
    }

    public String getValue() {
        return value;
    }
}
