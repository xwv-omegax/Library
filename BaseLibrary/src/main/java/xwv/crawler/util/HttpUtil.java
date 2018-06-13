package xwv.crawler.util;

import net.sf.json.JSONObject;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

public class HttpUtil {

    private static OkHttpClient httpClient;


    static class CallbackAdapter implements Callback {


        public void onFailure(Call call, IOException e) {

        }

        public void onResponse(Call call, Response response) throws IOException {

        }
    }


    public static OkHttpClient getHttpClient() {

        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .readTimeout(1000, TimeUnit.MILLISECONDS)
                    .connectionPool(new ConnectionPool(5, 1500, TimeUnit.MILLISECONDS))
                    .build();
        }
        return httpClient;
    }

    public static OkHttpClient getHttpClient(long timeout, Proxy proxy) {

        return new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .proxy(proxy)
                .connectionPool(new ConnectionPool(1, 200, TimeUnit.MILLISECONDS))
                .build();
    }

    public static String Post(String url) {
        return PostJson(url, null, true, 0, null);
    }

    public static boolean Test(String url, Proxy proxy, long timeout) {
        try {
            OkHttpClient client;
            if (timeout > 0 || proxy != null) {
                client = getHttpClient(timeout, proxy);
            } else {
                client = getHttpClient();
            }


            Request.Builder builder = new Request.Builder()
                    .url(url);

            Request request = builder.build();
            long start = System.currentTimeMillis();
            try (
                    Response response = client.newCall(request).execute();
                    ResponseBody body = response.body()
            ) {


                if (response.code() != 200) {
                    return false;
                }

                String resp = null;

                if (body == null || (resp = body.string()) == null) {
                    return false;
                }


                if (System.currentTimeMillis() - start > 1500) {
                    return false;
                }

                try {
                    JSONObject json;
                    return (json = JSONObject.fromObject(resp)).has("Result") && !json.getString("Result").startsWith("今天");
                } catch (Exception e) {
                    return false;
                }

            }

        } catch (Exception e) {
            if (!(e instanceof SocketTimeoutException) && !(e instanceof IOException)) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static String Post(String url, Proxy proxy, long timeout) {
        return Post(url, proxy, timeout, true);
    }

    public static String QuietPost(String url, Proxy proxy, long timeout) {
        return Post(url, proxy, timeout, false);
    }

    public static String Post(String url, Proxy proxy, long timeout, boolean verbose) {
        return PostJson(url, null, true, timeout, proxy, verbose);
    }

    public static String PostJson(String url, JSONObject json) {
        return PostJson(url, json, true, 0, null);
    }

    public static String PostJson(String url, JSONObject json, boolean sync, long timeout, Proxy proxy) {
        return PostJson(url, json, sync, timeout, proxy, true);
    }

    public static String PostJson(String url, JSONObject json, boolean sync, long timeout, Proxy proxy, boolean verbose) {
        try {
            OkHttpClient client;
            if (timeout > 0 || proxy != null) {
                client = getHttpClient(timeout, proxy);
            } else {
                client = getHttpClient();
            }


            Request.Builder builder = new Request.Builder()
                    .url(url);
            if (json != null) {
                builder.post(RequestBody.create(MediaType.parse("application/json"), json.toString()));
            }
            Request request = builder.build();
            if (sync) {
                try (Response response = client.newCall(request).execute(); ResponseBody body = response.body()) {
                    if (response.code() == 200) {
                        return body.string();
                    } else {
                        return null;
                    }
                }
            } else {
                client.newCall(request).enqueue(new CallbackAdapter());
                return null;
            }

        } catch (Exception e) {
            if (e instanceof SocketTimeoutException || e instanceof IOException) {
                if (verbose) {
                    System.out.println(e.toString());
                }
            } else {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static String getRequestBody(InputStream is) throws IOException {
        return getRequestBody(is, "UTF-8");
    }


    public static String getRequestBody(InputStream is, String charset) throws IOException {
        StringBuffer sb = new StringBuffer();

        BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));

        char[] buffer = new char[32];
        int len;
        while ((len = br.read(buffer)) != -1) {
            sb.append(buffer, 0, len);
        }
        return sb.toString();
    }
}
