package xwv.util;

public class ExceptionUtil {
    public static String getSingleStackTrace(Exception e) {
        if (e == null) {
            return null;
        }
        return e.toString() + "(" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")";
    }
}
