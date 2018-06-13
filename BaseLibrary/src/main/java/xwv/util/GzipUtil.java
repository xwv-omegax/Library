package xwv.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

public class GzipUtil {
    public static String decompress(InputStream input) {
        if (input == null) {
            return null;
        }

        try (InputStreamReader inputStream =
                     new InputStreamReader(
                             new GZIPInputStream(input))) {
            StringBuilder builder = new StringBuilder();

            char[] buffer = new char[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                builder.append(buffer, 0, len);
            }
            return builder.toString();
        } catch (Exception e) {
            if (e instanceof ZipException) {
                System.err.println(e.getMessage() + "(" + e.getStackTrace()[2].getFileName() + ":" + e.getStackTrace()[2].getLineNumber() + ")");
            } else {
                e.printStackTrace(System.out);
            }
            return null;
        }

    }

}
