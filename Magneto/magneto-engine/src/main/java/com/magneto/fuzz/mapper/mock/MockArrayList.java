package com.magneto.fuzz.mapper.mock;

import java.util.ArrayList;
import java.util.Iterator;

public class MockArrayList<E> extends ArrayList<E> {

    private int mockSize;

    private ArrayList<E> arrayList;

    public MockArrayList() {
        this.mockSize = 0;
        this.arrayList = new ArrayList<>();
    }

    public MockArrayList(int size) {
        this.mockSize = size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Iterator<E> actualIterator = arrayList.iterator();
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                // Check if the current index is less than the actual size of the arraylist
                return currentIndex < arrayList.size();
            }

            @Override
            public E next() {
                if (currentIndex >= arrayList.size()) {
                    throw new IndexOutOfBoundsException("Index: " + currentIndex + ", Size: " + arrayList.size());
                }
                currentIndex++;
                return actualIterator.next();
            }
        };
    }

    @Override
    public E get(int index) {
        if (index >= super.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + super.size());
        }
        return super.get(index);
    }

    @Override
    public boolean add(E e) {
        return super.add(e);
    }

    @Override
    public int size() {
        return mockSize;
    }

    public E set(int index, E element) {
        if (index >= super.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + super.size());
        }
        return super.set(index, element);
    }

}
