package com.albert.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
//@RunWith(SpringJUnit4ClassRunner.class)
public class ConcurrentServiceTest {

    //https://www.baeldung.com/java-future

    private ConcurrentService concurrentService = new ConcurrentService();

    @Test
    public void simpleUsageOfFuture() throws InterruptedException, ExecutionException, TimeoutException {

        Future<Integer> future = concurrentService.calculate(2);
        while (!future.isDone()) {
            log.debug("cal-ing");
            Thread.sleep(300);
        }
        Integer result = future.get();
        log.debug("res : {}", result);
        assertThat(result).isEqualTo(4);
    }

    @Test
    public void simpleSetTimeout() throws InterruptedException, ExecutionException, TimeoutException {

        Future<Integer> future = concurrentService.calculate(2);

        log.debug("step 1 start");
        Thread.sleep(1000);
        log.debug("step 1 end");

        log.debug("step 2 start");
        Thread.sleep(1000);
        log.debug("step 2 end");

        Integer result = future.get(2000, TimeUnit.MILLISECONDS);
        log.debug("res : {}", result);
        assertThat(result).isEqualTo(4);
    }

    @Test
    public void multiThreadNotParallel() throws InterruptedException, ExecutionException, TimeoutException {

        Future<Integer> future1 = concurrentService.calculate(2);
        Future<Integer> future2 = concurrentService.calculate(100);

        log.debug("step 1 start");
        Thread.sleep(1000);
        log.debug("step 1 end");

        log.debug("step 2 start");
        Thread.sleep(1000);
        log.debug("step 2 end");

        Integer result = future1.get(2000, TimeUnit.MILLISECONDS);
        log.debug("res : {}", result);
        Integer result2 = future1.get(2000, TimeUnit.MILLISECONDS);
        log.debug("res : {}", result2);
        assertThat(result).isEqualTo(4);
    }

    @Test
    public void multiThreadParallel() throws InterruptedException, ExecutionException, TimeoutException {
        /*
        There are other factory methods that can be used to create thread pools,
        like Executors.newCachedThreadPool() that
        reuses previously used Threads when they are available,
        and Executors.newScheduledThreadPool() which schedules commands to run after a given delay.
        */
        concurrentService.setExecutor(Executors.newFixedThreadPool(2));

        Future<Integer> future1 = concurrentService.calculate(2);

        log.debug("step 1 start");
        Thread.sleep(1000);
        log.debug("step 1 end");

        log.debug("step 2 start");
        Thread.sleep(1000);
        log.debug("step 2 end");

        Integer result = future1.get(2000, TimeUnit.MILLISECONDS);
        log.debug("res : {}", result);
        Integer result2 = future1.get(2000, TimeUnit.MILLISECONDS);
        log.debug("res : {}", result2);
        assertThat(result).isEqualTo(4);
    }
    /*
    ForkJoinPool.

    There are two abstract classes that implement ForkJoinTask:
    RecursiveTask which returns a value upon completion,
    and RecursiveAction which doesn't return anything.
    */

    @Test
    void cancel() throws InterruptedException, ExecutionException, TimeoutException {

        Future<Integer> future = concurrentService.calculate(2);
//        Integer result = future.get(2000, TimeUnit.MILLISECONDS);
        boolean canceled = future.cancel(true);
//        log.debug("res : {}", result);
        assertThat(canceled).isTrue();
    }
}