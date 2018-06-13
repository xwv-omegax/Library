package xwv.crawler;

public abstract class Crawler<T> implements ICrawler {

    public abstract T parseOne(String url);
}
