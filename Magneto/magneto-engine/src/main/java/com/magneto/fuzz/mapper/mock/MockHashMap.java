package com.magneto.fuzz.mapper.mock;

import java.util.*;

public class MockHashMap<K, V> extends HashMap<K, V> {

    private int mockSize;

    private final HashMap<K, V> hashMap;

    public MockHashMap() {
        this.hashMap = new HashMap<>();
        this.mockSize = 0;
    }

    public MockHashMap(int size) {
        this.hashMap = new HashMap<>();
        this.mockSize = size;
    }


    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<Entry<K, V>>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return hashMap.entrySet().iterator();
            }

            @Override
            public int size() {
                return mockSize;
            }
        };
    }

    @Override
    public Set<K> keySet() {
        return new AbstractSet<K>() {
            @Override
            public Iterator<K> iterator() {
                return hashMap.keySet().iterator();
            }

            @Override
            public int size() {
                return mockSize;
            }
        };
    }

    @Override
    public Collection<V> values() {
        return new AbstractCollection<V>() {
            @Override
            public Iterator<V> iterator() {
                return hashMap.values().iterator();
            }

            @Override
            public int size() {
                return mockSize;
            }
        };
    }


    @Override
    public int size() {
        return mockSize;
    }
}
