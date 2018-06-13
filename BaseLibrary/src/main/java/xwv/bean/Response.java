package xwv.bean;

public class Response {
    private int code = 0;
    private String body = null;


    public Response(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public int code() {
        return code;
    }

    public String body() {
        return body;
    }

    @Override
    public String toString() {
        if (code == 200) {
            return body;
        } else {
            return null;
        }
    }
}
