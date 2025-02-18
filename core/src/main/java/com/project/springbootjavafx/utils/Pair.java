package com.project.springbootjavafx.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@code Pair} class is a simple generic container that holds a pair of objects.
 *
 * <p>
 * It encapsulates two values: a key and a value, which can be of different types. This class is useful
 * for representing a tuple of two related objects.
 * </p>
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
@Getter
@Setter
@AllArgsConstructor
public class Pair<K, V> {
    /**
     * The key part of the pair.
     */
    private K key;

    /**
     * The value part of the pair.
     */
    private V value;
}
