package xwv.crawler.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HeadlessChrome {

    public static class ChromeDriver {
        private org.openqa.selenium.chrome.ChromeDriver driver;
        private Lock lock = new ReentrantLock(true);
        private ChromeOptions options;

        public ChromeDriver(ChromeOptions options) {
            this.options = options;
            this.driver = new org.openqa.selenium.chrome.ChromeDriver(this.options);
        }

        public org.openqa.selenium.chrome.ChromeDriver getDriver() {
            return driver;
        }

        public Lock getLock() {
            return lock;
        }

        public ChromeOptions getOptions() {
            return options;
        }
    }

    static {
        System.setProperty("webdriver.chrome.driver", new File("./tools/chromedriver.exe").getAbsolutePath());
    }

    private static Lock lock = new ReentrantLock(true);

    private static ConcurrentLinkedQueue<ChromeDriver> drivers;


    public static ChromeDriver getDriver() {
        if (drivers == null) {
            drivers = new ConcurrentLinkedQueue<ChromeDriver>();
        }

        lock.lock();
        try {
            if (drivers.size() < 8) {
                ChromeDriver driver = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
                drivers.add(driver);
                return driver;
            }
        } finally {
            lock.unlock();
        }

        while (true) {

            for (ChromeDriver d : drivers) {
                if (d.getLock().tryLock()) {
                    d.getLock().unlock();
                    return d;
                }
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                break;
            }
        }
        return null;
    }

    public static List<Cookie> parseCookie(String cookie) {
        if (cookie == null) {
            return null;
        }
        String[] arr = cookie.split(";");
        List<Cookie> cookies = new ArrayList<>();


        for (String c : arr) {
            int mark = c.indexOf("=");
            String name = c.substring(0, mark);
            String value = c.substring(mark + 1, c.length());
            cookies.add(new Cookie(name, value));
        }

        return cookies;
    }

    public static Document parse(String url) {
        return parse(url, null);
    }

    public static Document parse(String url, String cookie) {
        lock.lock();
        ChromeDriver _driver;
        try {
            _driver = getDriver();
            _driver.getLock().lock();
        } finally {
            lock.unlock();
        }
        try {
            System.out.println("parse:" + url);
            org.openqa.selenium.chrome.ChromeDriver driver = _driver.getDriver();
//            driver.get(url);
            if (driver.findElementByTagName("body").getText().trim().isEmpty() || !driver.getCurrentUrl().startsWith("https://www.pixiv.net")) {
                driver.get("https://www.pixiv.net");
            }
            List<Cookie> cookies = parseCookie(cookie);
            if (cookies != null) {
                for (Cookie c : cookies) {
                    driver.manage().addCookie(c);
                }
            }


            driver.executeScript("location.href='" + url + "'");
            Document result = Jsoup.parse(driver.getPageSource());
            System.out.println("done");
            return result;
        } finally {
            _driver.getLock().unlock();
        }
    }

    public static void ClearWebDrivers() {
        if (drivers != null) {
            for (ChromeDriver driver : drivers) {
                driver.getDriver().quit();
                drivers.remove(driver);
            }
        }
    }
//    public static void main(String[] args) {
//        String sentence="aaaabbbbcccc";
//        String regex="([^0-9]{2,})\\1";
//        System.out.println(sentence.replaceAll(regex,"$1"));
//    }

//

    public static void main(String[] args) {
        parseCookie("p_ab_id=9; p_ab_id_2=5; device_token=aec1e6b3537bb870f30edc88374f0864; yuid=IzRBSAI56; login_ever=yes; _ga=GA1.2.963691050.1524827456; is_sensei_service_user=1; __gads=ID=b50be561fff862a9:T=1526469927:S=ALNI_MYUCvHBaZpBT7Y0JwfEz0Zajneczg; privacy_policy_agreement=1; c_type=22; a_type=0; b_type=1; PHPSESSID=4659697_58f491454a1b4159edf110cf574d3320; __utmt=1; __utma=235335808.963691050.1524827456.1526481126.1526483142.4; __utmb=235335808.1.10.1526483142; __utmc=235335808; __utmz=235335808.1524827456.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmv=235335808.|2=login%20ever=yes=1^3=plan=normal=1^5=gender=male=1^6=user_id=4659697=1^9=p_ab_id=9=1^10=p_ab_id_2=5=1^11=lang=zh=1; module_orders_mypage=%5B%7B%22name%22%3A%22sketch_live%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22tag_follow%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22recommended_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22showcase%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22everyone_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22following_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22mypixiv_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22fanbox%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22featured_tags%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22contests%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22user_events%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22sensei_courses%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22spotlight%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22booth_follow_items%22%2C%22visible%22%3Atrue%7D%5D; limited_ads=%7B%22header%22%3A%22%22%7D");
    }
}
