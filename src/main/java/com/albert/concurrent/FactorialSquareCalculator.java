package com.albert.concurrent;

import java.util.concurrent.RecursiveTask;

public class FactorialSquareCalculator extends RecursiveTask<Integer> {

    private Integer n;

    public FactorialSquareCalculator(Integer n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n <= 1) {
            return n;
        }

        FactorialSquareCalculator calculator
                = new FactorialSquareCalculator(n - 1);

        calculator.fork();

        return n * n + calculator.join();
    }
    /*
    ForkJoinPool forkJoinPool = new ForkJoinPool();

    FactorialSquareCalculator calculator = new FactorialSquareCalculator(10);

    forkJoinPool.execute(calculator);
    */
}
