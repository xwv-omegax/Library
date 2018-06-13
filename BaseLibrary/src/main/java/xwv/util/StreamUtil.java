package xwv.util;

import java.io.*;

public class StreamUtil {
    public static String ReadStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder builder = new StringBuilder();
        int len;
        char[] buffer = new char[1024];
        while ((len = reader.read(buffer)) > -1) {
            builder.append(buffer, 0, len);
        }
        return builder.toString();
    }

    public static byte[] Read2ByteArray(InputStream input) {
        BufferedInputStream reader = new BufferedInputStream(input);
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            int len;
            byte[] buffer = new byte[1024];
            while ((len = reader.read(buffer)) > -1) {
                output.write(buffer, 0, len);
            }
            output.flush();
            return output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace(System.out);
            return null;
        }
    }
}
