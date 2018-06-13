package xwv.proxy;

import okhttp3.*;
import xwv.util.GzipUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Proxy {

    public static final Proxy NULL = new Proxy();

    private Lock lock = new ReentrantLock(true);
    private final int[] host = new int[4];
    private int port;
    private double rate = 4;
    private OkHttpClient httpClient;
    private Dns dns;

    private volatile long startTime = 0;
    private volatile long lastTime = 0;
    private volatile int successCount = 0;
    private volatile int DeadSoonCount = 0;
    private volatile int tryCount = 0;

    public Proxy() {
        for (int i = 0; i < 4; i++) {
            this.host[i] = -1;
        }
    }

    public Proxy(String host, int port) {
        this();
        setHost(host);
        setPort(port);
    }

    public Proxy(String host, int port, Dns dns) {
        this(host, port);
        this.dns = dns;

    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getHost() {

        if (host[0] > -1 && host[1] > -1 && host[2] > -1 && host[3] > -1) {
            return host[0] + "." + host[1] + "." + host[2] + "." + host[3];
        } else {
            return null;
        }
    }

    public void setHost(String host) {
        try {
            String[] h = host.split("\\.");
            for (int i = 0; i < 4; i++) {
                this.host[i] = Integer.parseInt(h[i]);
            }
        } catch (Exception e) {
            System.out.println(e.toString() + "(" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
            for (int i = 0; i < 4; i++) {
                this.host[i] = -1;
            }
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private java.net.Proxy proxy = null;

    public java.net.Proxy getProxy() {
        if (proxy == null) {
            lock.lock();
            try {
                if (proxy == null && this.toString() != null) {
                    proxy = new java.net.Proxy(
                            java.net.Proxy.Type.HTTP,
                            new InetSocketAddress(getHost(), getPort()));
                }
            } finally {
                lock.unlock();
            }
        }
        return proxy;
    }


    public OkHttpClient getHttpClient() {

        if (httpClient == null) {
            lock.lock();
            try {
                if (httpClient == null) {

                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(3, TimeUnit.SECONDS)
                            .readTimeout(5, TimeUnit.SECONDS)
                            .connectionPool(new ConnectionPool(1024, 3, TimeUnit.SECONDS));

                    if (this.toString() != null) {
                        builder.proxy(new java.net.Proxy(
                                java.net.Proxy.Type.HTTP,
                                new InetSocketAddress(getHost(), getPort())
                        ));
                    }
                    if (dns != null) {
                        builder.dns(dns);
                    }
                    httpClient = builder.build();
                    httpClient.dispatcher().setMaxRequests(512);
                }
            } finally {
                lock.unlock();
            }
        }
        return httpClient;

    }

//    public static OkHttpClient client = new OkHttpClient.Builder()
//            .connectTimeout(3, TimeUnit.SECONDS)
//            .readTimeout(5, TimeUnit.SECONDS)
//            .connectionPool(new ConnectionPool(1024, 3, TimeUnit.SECONDS))
////            .dns(dns)
//            .build();


//    public xwv.bean.Response OkHttpPost(String url, Headers headers) {
//
//        OkHttpClient client = Proxy.client;
//        Request.Builder builder = new Request.Builder()
//                .method("GET", null)
////                    .header("Connection", "close")
//                .url(url);
//        if (headers != null) {
//            builder.headers(headers);
//        }
//        Request request = builder.build();
//        try (okhttp3.Response response = client.newCall(request).execute(); ResponseBody body = response.body()) {
//            xwv.bean.Response resp;
//            String resp_body = body != null ? GzipUtil.decompress(body.byteStream()) : null;
//            boolean isGzip = false;
//            if (resp_body == null) {
//                resp = new xwv.bean.Response(response.code(), body != null ? body.string() : null);
//            } else {
//                resp = new xwv.bean.Response(response.code(), resp_body);
//                isGzip = true;
//            }
//
//            if (response.code() < 500) {
//                return resp;
//            }
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//        return null;
//    }

    public String Post(String url) {
        return Post(url, null, null, true);
    }

    public String Post(String url, boolean verbose) {
        return Post(url, null, null, verbose);
    }

    public String Post(String url, String cookie) {
        return Post(url, cookie, null, true);
    }

    public String Post(String url, String cookie, String referer) {
        return Post(url, cookie, referer, true);
    }

    public String Post(String url, String cookie, String referer, boolean verbose) {
        Headers.Builder builder = new Headers.Builder();
        if (cookie != null && !cookie.trim().isEmpty()) {
            builder.add("Cookie", cookie);
        }

        if (referer != null && !referer.trim().isEmpty()) {
            builder.add("referer", referer);
        }


        return Post(url, builder.build(), verbose);
    }

    public String Post(String url, List<Cookie> cookies, String referer) {
        StringBuilder cookie = new StringBuilder();
        if (cookies != null) {
            for (Cookie c : cookies) {
                cookie.append(c.name()).append("=").append(c.value()).append(";");
            }
        }
        return Post(url, cookie.toString(), referer);
    }

    public String Post(String url, Headers headers) {
        xwv.bean.Response response = Post(url, headers, true, 1000, 2000);
        return response == null ? null : response.toString();
    }

    public xwv.bean.Response PostForResponse(String url, Headers headers) {
        return Post(url, headers, true, 3000, 5000);
    }

    public String Post(String url, Headers headers, boolean verbose) {
        xwv.bean.Response response = Post(url, headers, verbose, 0, 0);
        return response == null ? null : response.toString();
    }

    public xwv.bean.Response Post(String url, Headers headers, boolean verbose, int... timeout) {
        if (true) {
            return post(url, headers, verbose, timeout);
        }

        try {
//            lock.lock();
//            try {
//                if (rate < 0.1) {
//                    rate = 0.1;
//                }
//                while (System.currentTimeMillis() - lastTime < Math.round(1000d / rate)) {
//                    Thread.sleep(0);
//                }
//                lastTime = System.currentTimeMillis();
//            } finally {
//                lock.unlock();
//            }
//
            OkHttpClient client = getHttpClient();

//            OkHttpClient client = Proxy.client;
            client.dispatcher().setMaxRequests(512);

            Request.Builder builder = new Request.Builder()
//                    .header("Connection", "close")
                    .url(url);
            if (headers != null) {
                builder.headers(headers);
            }
            String accept_encoding;
            if (!(headers != null && (accept_encoding = headers.get("accept-encoding")) != null && accept_encoding.contains("gzip"))) {
                builder.header("accept-encoding", "gzip");
            }


//            if (cookie != null && !cookie.trim().isEmpty()) {
//                builder.header("Cookie", cookie);
//            }
//
//            if (referer != null && !referer.trim().isEmpty()) {
//                builder.header("referer", referer);
//            }

            Request request = builder.build();

//            for (int i = 0; i < 3; i++) {
//                if (i > 0) {
//                    Thread.sleep(1000);
//                }
            long timestamp = System.currentTimeMillis();
            try (Response response = client.newCall(request).execute(); ResponseBody body = response.body()) {
//                    System.out.println(System.currentTimeMillis() - timestamp);
                xwv.bean.Response resp;
                String resp_body = body != null ? GzipUtil.decompress(body.byteStream()) : null;
                boolean isGzip = false;
                if (resp_body == null) {
                    resp = new xwv.bean.Response(response.code(), body != null ? body.string() : null);
                } else {
                    resp = new xwv.bean.Response(response.code(), resp_body);
                    isGzip = true;
                }

                if (response.code() == 200) {
//                        lock.lock();
//                        try {
//                            successCount++;
//                        } finally {
//                            lock.unlock();
//                        }
                    return resp;
                } else if (response.code() < 500) {
                    return resp;
                }

            }
//            }
            return null;
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

    private int defTimeout = 1000;

    public void setDefTimeout(int defTimeout) {
        if (defTimeout > 99) {
            this.defTimeout = defTimeout;
        }
    }

    public int getDefTimeout() {
        return defTimeout;
    }

    public xwv.bean.Response post(String url, Headers headers, boolean verbose, int... timeout) {
        int connectTimeout = getDefTimeout();
        int readTimeout = getDefTimeout();
        if (timeout.length > 0) {
            connectTimeout = timeout[0];
        }
        if (timeout.length > 1) {
            readTimeout = timeout[1];
        }

        try {
            lock.lock();
            try {
                if (rate < 0.1) {
                    rate = 0.1;
                }
                while (System.currentTimeMillis() - lastTime < Math.round(1000d / rate)) {
                    Thread.sleep(0);
                }
                lastTime = System.currentTimeMillis();
            } finally {
                lock.unlock();
            }


            HttpURLConnection connection;
            if (this.toString() != null) {
                connection = (HttpURLConnection) new URL(url).openConnection(getProxy());
            } else {
                connection = (HttpURLConnection) new URL(url).openConnection();
            }

            String accept_encoding;
            if (!(headers != null && (accept_encoding = headers.get("accept-encoding")) != null && accept_encoding.contains("gzip"))) {
                connection.setRequestProperty("accept-encoding", "gzip");
            }
            if (headers != null) {
                for (String name : headers.names()) {

                    connection.setRequestProperty(name, headers.get(name));

                }
            }
//            connection.setRequestProperty("connection", "close");
            if (connectTimeout < 1) {
                connectTimeout = 1000;
            }

            if (readTimeout < 1) {
                readTimeout = 1000;
            }
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
            connection.setRequestMethod("GET");


            long timestamp = System.currentTimeMillis();
            connection.connect();
//            long now = System.currentTimeMillis();

            int code = connection.getResponseCode();
//            System.out.println("connect:" + (System.currentTimeMillis() - timestamp));
            try (InputStream input = code == 200 ? connection.getInputStream() : connection.getErrorStream();
//                 OutputStream ignored = connection.getOutputStream();
                 BufferedInputStream bis = new BufferedInputStream(input);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

                StringBuilder builder = new StringBuilder();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = bis.read(buffer)) > -1) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                byte[] bytes = bos.toByteArray();
//                bos.close();
//                input.close();

//            System.out.println(System.currentTimeMillis() - timestamp);
                String body = null;
                String content_encoding = connection.getHeaderField("content-encoding");
                if (content_encoding != null && content_encoding.contains("gzip")) {
                    body = GzipUtil.decompress(new ByteArrayInputStream(bytes));
                }

                if (body == null) {
                    body = new String(bytes, "UTF-8");
                }

                if (code < 500) {
                    if (code == 200) {

                        successCount++;

                    }
                    return new xwv.bean.Response(code, body);
                }
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            if (e instanceof SocketTimeoutException || e instanceof IOException) {
                if (verbose) {
//                    e.printStackTrace(System.out);
                    System.err.println(e.toString() + "(" + url + ")");
                }
            } else {
                e.printStackTrace(System.out);
            }
        }
        return null;
    }


    @Override
    public String toString() {
        if (getHost() == null) {
            return null;
        } else {
            return getHost() + ":" + port;
        }
    }

    public boolean isNull() {
        return getHost() == null;
    }


    //设置接下来
    public void disable(long sec) {
        lock.lock();
        try {
            if (tryCount > -1) {
                tryCount = 0;
                long now = System.currentTimeMillis();
//                if (now - startTime < 20 * 1000) {
//                    DeadSoonCount++;
//                } else if (now - startTime > sec * 1000 + 20 * 1000 * DeadSoonCount) {
//                    DeadSoonCount--;
//                }

                if (successCount < 2) {
                    DeadSoonCount++;
                } else if (successCount > 10) {
                    DeadSoonCount--;
                }
                successCount = 0;
                startTime = now + sec * 1000 + 20 * 1000 * DeadSoonCount;
            } else {
                tryCount++;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean isEnable() {
        return System.currentTimeMillis() > startTime;
    }
}
