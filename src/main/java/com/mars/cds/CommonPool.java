package com.mars.cds;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class CommonPool {

    private static final ExecutorService pool = Executors.newFixedThreadPool(20);

    public static void submit(Runnable runnable) {
        pool.submit(runnable);
    }
}
