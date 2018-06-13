package xwv.crawler;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface ICrawler {
    ConcurrentLinkedQueue<String> nextUrlQueue();

    boolean hasNextUrlQueue();

    void reset();
}
