package xwv.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncodeUtil {
    public static String EncodeNoAsciiChar(String str) {
        if (str == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("[^\\x00-\\x19\\x21-\\xff]+");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            try {
                matcher.reset((str=matcher.replaceFirst(URLEncoder.encode(matcher.group(), "UTF-8"))));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace(System.out);
            }
        }

        return str;

    }

    public static void main(String[] args) {
        System.out.println(EncodeNoAsciiChar("a我1试试!看/！、、:看"));
    }
}
