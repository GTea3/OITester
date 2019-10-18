package com.samsung.utils;

import java.io.Serializable;
import java.util.Objects;

public class Pair<Key, Value> implements Serializable {
    private Key key;
    private Value value;

    public Key getKey() {
        return key;
    }

    public Value getValue() {
        return value;
    }

    public Pair(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (key != null ? key.hashCode() : 0);
        hash = 31 * hash + (value != null ? value.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Pair) {
            Pair pair = (Pair) o;
            if (!Objects.equals(key, pair.key))
                return false;
            return Objects.equals(value, pair.value);
        }
        return false;
    }
}
