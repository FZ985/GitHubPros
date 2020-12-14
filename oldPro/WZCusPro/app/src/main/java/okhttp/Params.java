package okhttp;

/**
 * Created by JFZ .
 * on 2018/1/16.
 */

public class Params {

    public Params() {
    }

    public Params(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Params(String key, int i) {
        this.key = key;
        this.i = i;
    }

    public String key;
    public String value;
    public Integer i;
}
