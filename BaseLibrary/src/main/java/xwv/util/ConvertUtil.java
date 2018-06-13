package xwv.util;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {

    public static String Number2String(long l) {

        String pre = "";
        if (l < 0) {
            l = l * -1L;
            pre = "-";
        }
        List<Character> characters = new ArrayList<>();
        do {
            long hex = Long.parseLong(String.valueOf(l % 10000L), 16);
            characters.add((char) hex);
            l /= 10000L;
        } while (l > 0);
        char[] chars = new char[characters.size()];

        for (int index = 0; index < characters.size(); index++) {
            chars[index] = characters.get(characters.size() - index - 1);
        }

        return pre + new String(chars);
    }

    public static long String2Number(String str) {
        long base = 1L;
        if (str.startsWith("-")) {
            str = str.substring(1);
            base = -1L;
        }

        char[] chars = str.toCharArray();
        int exp = 0;
        long result = 0L;
        for (int i = 0; i < chars.length; i++) {
            String hex = Long.toHexString(chars[chars.length - i - 1]);
            result += (Long.parseLong(hex, 10) * Math.round(Math.pow(10, exp)));
            exp += 4;
        }


        return base * result;

    }
}
