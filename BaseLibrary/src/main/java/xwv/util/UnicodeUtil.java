package xwv.util;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.function.IntPredicate;

public class UnicodeUtil {
    private static class Range {
        private final long start;
        private final long end;

        Range(long start, long end) {
            this.start = start;
            this.end = end;
        }

        public boolean IsBetween(int i) {
            return ((long) i - start) * ((long) i - end) <= 0;
        }
    }

    private static final Range base = new Range(0x4E00, 0x9FA5);
    private static final Range baseExt = new Range(0x9FA6, 0x9FCB);
    private static final Range extA = new Range(0x3400, 0x4DB5);
    private static final Range extB = new Range(0x20000, 0x2A6D6);
    private static final Range extC = new Range(0x2A700, 0x2B734);
    private static final Range extD = new Range(0x2B740, 0x2B81D);
    private static final Range radical = new Range(0x2F00, 0x2FD5);
    private static final Range radicalExt = new Range(0x2E80, 0x2EF3);
    private static final Range compatible = new Range(0xF900, 0xFAD9);
    private static final Range compatibleExt = new Range(0x2F800, 0x2FA1D);
    private static final Range pua = new Range(0xE815, 0xE86F);


    public static boolean isChineseCodePoint(int codePoint) {
        Range[] ranges = new Range[]{
                base, baseExt, extA, extB, extC, extD, radical, radicalExt, compatible, compatibleExt, pua
        };
        boolean result = false;
        for (int i = 0; i < ranges.length && !result; i++) {
            if (ranges[i] != null) {
                result = ranges[i].IsBetween(codePoint);
//                System.out.println("[" + i + "]" + codePoint + ":" + result);
            }
        }
        return result;
    }

    public static long calculateChCodePointCount(String str) {
        if (str == null) {
            return 0;
        }
        return str.codePoints().filter(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return isChineseCodePoint(value);
            }
        }).count();
    }

    public static double calculateChineseRate(String str) {
        if (str == null) {
            return 0;
        }
        double count = str.codePoints().count();
        double chCount = calculateChCodePointCount(str);
        return Math.round(chCount / count * 10000d) / 10000d;
    }

    public static void main(String[] args) {
        System.out.println(StringEscapeUtils.escapeJavaScript("\uD840\uDC01\u4E00"));
        System.out.println(Integer.toHexString("\uD840\uDC01\u4E00".codePoints().toArray()[1]));
        StringBuilder builder = new StringBuilder();
        System.out.println(calculateChCodePointCount("\uD840\uDC01\u4E002wsw"));
        System.out.println(calculateChineseRate("\uD840\uDC01\u4E0021wsw") * 100 + "%");
//        System.out.println(isChineseCodePoint(Integer.MIN_VALUE));
//        builder.appendCodePoint()
    }

}
