package com.iridium.iridiumskyblock.utils;

/**
 * Represents a function that takes 3 arguments and returns a result.
 *
 * @param <T> The first argument
 * @param <U> The second argument
 * @param <S> The third argument
 * @param <R> The result of this function
 */
@FunctionalInterface
public interface TriFunction<T, U, S, R> {

    /**
     * Applies this function to the provided arguments.
     *
     * @param t The first argument
     * @param u The second argument
     * @param s The third argument
     * @return The result of the function
     */
    R apply(T t, U u, S s);

}
