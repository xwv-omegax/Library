package xwv.crawler.util;

import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class PhantomJS {
    public static Document parse(String url) {
        try {
            File f = new File("./tools");
            System.out.println(f.getAbsolutePath());
            System.out.println(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(new String[]{"./tools/phantomjs.exe", "./tools/phantomjs.js", url}).getInputStream()));
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[1024];
            int len = 0;
            while ((len = reader.read(buffer)) > 0) {
                builder.append(buffer, 0, len);
            }
            return Jsoup.parse(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Document parseFromServer(String url) {
        JSONObject data = new JSONObject();
        System.out.println("parse:" + url);
        data.put("url", url);
        String html = HttpUtil.PostJson("http://127.0.0.1:1401", data);
        System.out.println("done");
        if (html==null){
            return null;
        }
        return Jsoup.parse(html);
    }

    public static void main(String[] args) {
        String url = "http://shoujikanshu.org/xuanhuan/index_24742.html";
        parseFromServer(url);
//        Document document = parse("http://shoujikanshu.org/xuanhuan/index_24742.html");
//        if (document == null) {
//            System.out.println("null!");
//            return;
//        }
//        System.out.println(document.select(".content").text());
    }
}


