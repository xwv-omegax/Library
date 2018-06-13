package xwv.crawler.executor;

import xwv.crawler.Crawler;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.*;

import static xwv.crawler.util.HeadlessChrome.ClearWebDrivers;

public class StringCrawlerExecutor {
    public static class Msg {
        public volatile boolean flag = false;
        public volatile int maxSeq = -1;

        public Msg(boolean flag) {
            this.flag = flag;
        }

        public Msg() {
        }
    }

    private ExecutorService executor;

    public ExecutorService getExecutor() {
        if (executor == null || executor.isShutdown()) {
            executor = Executors.newFixedThreadPool(16);
        }
        return executor;
    }

    public void execute(final Crawler<String> crawler, final Writer writer) {
        int seq = 0;
        final Msg msg = new Msg(true);
        final ConcurrentMap<Integer, String> OutputCache = new ConcurrentHashMap<Integer, String>();
        getExecutor().execute(new Runnable() {
            public void run() {
                int lastSeq = -1;
                while (msg.flag || msg.maxSeq > lastSeq) {
                    String out = OutputCache.get(lastSeq + 1);
                    if (out != null) {
                        OutputCache.remove(lastSeq + 1);
                        try {
                            writer.write(out);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        lastSeq++;

                    }
                }
                ClearWebDrivers();
                getExecutor().shutdownNow();

            }
        });

        while (crawler.hasNextUrlQueue()) {
            final ConcurrentLinkedQueue<String> urls = crawler.nextUrlQueue();
            while (!urls.isEmpty()) {

                final int finalSeq = seq;
                msg.maxSeq = finalSeq;
                final String url = urls.poll();
                getExecutor().execute(new Runnable() {
                    public void run() {
                        OutputCache.put(finalSeq, crawler.parseOne(url));
                    }
                });
                seq++;
            }
        }
        msg.flag = false;


    }


}
