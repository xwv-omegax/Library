package xwv.crawler;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class SimpleCrawler implements ICrawler {
    public abstract void parseOne(String url);

    private ThreadPoolExecutor executor;

    public ThreadPoolExecutor getExecutor() {
        if (executor == null || executor.isShutdown()) {
            executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        }
        return executor;
    }
}
