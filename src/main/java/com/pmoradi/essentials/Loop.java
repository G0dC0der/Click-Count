package com.pmoradi.essentials;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Loop<T> {

    public static <T> List<T> create(int size, Supplier<T> func) {
        List<T> list = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            list.add(func.get());
        }
        return list;
    }

    public static Loop<Integer> range(int inclusive, int exclusive) {
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

    public static Loop<Integer> limit(int runs) {
        return range(0, runs);
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

    public static Loop<?> condition(BooleanSupplier conditionFunction) {
        return new Loop<Object>() {
            @Override
            Object next() {
                return null;
            }

            @Override
            boolean canAgain() {
                return conditionFunction.getAsBoolean();
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

    public static Loop<Character> string(String str) {
        return new Loop<Character>() {
            int i = 0;
            @Override
            Character next() {
                return str.charAt(i++);
            }

            @Override
            boolean canAgain() {
                return i < str.length();
            }
        };
    }

    public static Loop<Integer> read(File file) {
        if(!file.exists() || !file.canRead()) {
            throw new IllegalArgumentException("File invalid.");
        }
        InputStream[] in = new InputStream[1];
        try {
            in[0] = new BufferedInputStream(new FileInputStream(file));

            return new Loop<Integer>() {
                int value;

                @Override
                Integer next() {
                    return value;
                }

                @Override
                boolean canAgain() {
                    try {
                        return (value = in[0].read()) != -1;
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }

                @Override
                protected void clean() {
                    IOUtils.closeQuietly(in[0]);
                }

                @Override
                public void each(SimpleFunction simpleFunction) {
                    throw new UnsupportedOperationException("Redundant operation.");
                }
            };
        } catch (IOException e) {
            IOUtils.closeQuietly(in[0]);
            return null;
        }
    }

    public static Loop<String> lines(File file) {
        if(!file.exists() || !file.canRead()) {
            throw new IllegalArgumentException("File invalid.");
        }
        Reader[] in = new Reader[1];
        String newLine = System.getProperty("line.separator");
        try {
            in[0] = new BufferedReader(new FileReader(file));

            return new Loop<String>() {
                String value;

                @Override
                String next() {
                    return value;
                }

                @Override
                boolean canAgain() {
                    try {
                        StringBuilder bu = new StringBuilder();
                        while(true) {
                            int i = in[0].read();
                            if(i == -1)
                                return false;

                            String c = Character.toString((char)i);
                            if(c.equals(newLine))
                                break;
                            bu.append(c);
                        }
                        value = bu.toString();
                        return true;
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }

                @Override
                protected void clean() {
                    IOUtils.closeQuietly(in[0]);
                }

                @Override
                public void each(SimpleFunction simpleFunction) {
                    throw new UnsupportedOperationException("Redundant operation.");
                }
            };
        } catch (IOException e) {
            IOUtils.closeQuietly(in[0]);
            return null;
        }
    }

    public static Loop<Integer> read(InputStream in) {
        return new Loop<Integer>() {
            int value;

            @Override
            Integer next() {
                return value;
            }

            @Override
            boolean canAgain() {
                try {
                    return (value = in.read()) != -1;
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        };
    }

    @FunctionalInterface
    public interface SimpleFunction{
        void perform();
    }

    private boolean done;

    private Loop(){}

    abstract T next();

    abstract boolean canAgain();

    public void each(SimpleFunction simpleFunction) {
        if(done) {
            throw new IllegalStateException("Instance is used an no longer usable.");
        }

        try {
            while(canAgain()) {
                simpleFunction.perform();
            }
        } catch (Exception e) {
            fail(e);
        } finally {
            clean();
        }
    }

    public void each(Consumer<T> objectFunction) {
        if(done) {
            throw new IllegalStateException("Instance is used an no longer usable.");
        }

        try {
            while(canAgain()) {
                objectFunction.accept(next());
            }
        } catch (Exception e) {
            fail(e);
        } finally {
            clean();
        }
    }

    void fail(Exception e) {
        done = true;
    }

    void clean() {}
}