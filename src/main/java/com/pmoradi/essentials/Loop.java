package com.pmoradi.essentials;

import java.util.List;

public abstract class Loop<T> {

    public static Loop<Integer> loop(int inclusive, int exclusive) {
        return new Loop<Integer>() {
            int i = inclusive - 1;
            @Override
            Integer next() {
                return i;
            }

            @Override
            boolean canAgain() {
                return ++i < exclusive;
            }
        };
    }

    public static Loop<Integer> loop(int runs) {
        return loop(0, runs);
    }

    public static Loop<Integer> reverse(int inclusive, int exclusive) {
        return new Loop<Integer>() {
            int i = inclusive + 1;
            @Override
            Integer next() {
                return i;
            }

            @Override
            boolean canAgain() {
                return --i > exclusive;
            }
        };
    }

    public static Loop<?> condition(ConditionFunction conditionFunction) {
        return new Loop<Object>() {
            @Override
            Object next() {
                return null;
            }

            @Override
            boolean canAgain() {
                return conditionFunction.perform();
            }
        };
    }

    public static <T> Loop<T> list(List<T> list) {
        return new Loop<T>() {
            int i = 0;
            @Override
            T next() {
                return list.get(i++);
            }

            @Override
            boolean canAgain() {
                return i < list.size();
            }

            @Override
            public void each(SimpleFunction simpleFunction) {
                throw new UnsupportedOperationException("Redundant operation.");
            }
        };
    }

    @FunctionalInterface
    public interface SimpleFunction{
        void perform();
    }

    @FunctionalInterface
    public interface ObjectFunction<T>{
        void perform(T obj);
    }

    @FunctionalInterface
    public interface ConditionFunction{
        boolean perform();
    }

    private boolean done;

    private Loop(){}

    abstract T next();

    abstract boolean canAgain();

    public void each(SimpleFunction simpleFunction) {
        if(done) {
            throw new IllegalStateException("Instance is used an no longer usable.");
        }

        while(canAgain()) {
            simpleFunction.perform();
        }
        done = true;
    }

    public void each(ObjectFunction<T> objectFunction) {
        if(done) {
            throw new IllegalStateException("Instance is used an no longer usable.");
        }

        while(canAgain()) {
            objectFunction.perform(next());
        }
        done = true;
    }
}