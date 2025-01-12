package com.magneto.fuzz.mapper.mock;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MockHashSet<E> extends HashSet<E> {

    private int mockSize;

    private final HashSet<E> hashSet;

    public MockHashSet() {
        this.hashSet = new HashSet<>();
        this.mockSize = 0;
    }

    public MockHashSet(int size) {
        this.hashSet = new HashSet<>();
        this.mockSize = size;
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Iterator<E> baseIterator = hashSet.iterator();

            @Override
            public boolean hasNext() {
                return baseIterator.hasNext();
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return baseIterator.next();
            }
        };
    }

    @Override
    public int size() {
        return mockSize;
    }
}
