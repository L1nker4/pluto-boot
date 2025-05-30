package com.github.pluto.boot.base.common;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
