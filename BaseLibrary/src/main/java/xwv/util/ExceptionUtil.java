package xwv.util;

import java.io.PrintStream;

public class ExceptionUtil {
    public static String getSingleStackTrace(Exception e) {
        if (e == null) {
            return null;
        }
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement[] e_stacks = e.getStackTrace();
        StackTraceElement stack = e_stacks[0];

        for (int i = 1; i <= stacks.length && i <= e_stacks.length; i++) {
            StackTraceElement se = e_stacks[e_stacks.length - (i)];
            StackTraceElement s = stacks[stacks.length - (i)];

            stack = se;

            if (!se.getClassName().equals(s.getClassName())
                    || !se.getMethodName().equals(s.getMethodName())
                    || se.getLineNumber() != s.getLineNumber()) {

                break;

            }


        }
        return e.toString() + "(" + stack.getFileName() + ":" + stack.getLineNumber() + ")";
    }

    public static void printSingleStackTrace(Exception e) {
        System.out.println(getSingleStackTrace(e));
    }

    public static void printSingleStackTrace(Exception e, PrintStream printStream) {
        if (printStream != null) {
            printStream.println(getSingleStackTrace(e));
        }
    }
}
