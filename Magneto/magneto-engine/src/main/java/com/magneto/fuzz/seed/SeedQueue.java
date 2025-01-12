package com.magneto.fuzz.seed;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Slf4j
public class SeedQueue extends PriorityQueue<Seed> {
    private static final int MAX_SEED = 200;
    private static final int SHRINK_SIZE = MAX_SEED / 2;

    @Override
    public Seed poll() {
        return super.poll();
    }

    @Override
    public Seed peek() {
        return super.peek();
    }

    @Override
    public synchronized boolean add(Seed seed) {
        if (size() >= MAX_SEED) {
            shrink();
        }
        return super.add(seed);
    }

    public synchronized Seed getSeed() {
        if (isEmpty()) {
            log.info("the queue is empty, return init seed");
        }
        Seed seed = poll();
        add(seed);
        return seed;
    }


    protected synchronized void shrink() {
        log.info("seed queue shrink");
        int shrinkSize = Math.min(size(), SHRINK_SIZE);
        List<Seed> tmpList = new ArrayList<>();
        for (int i = 0; i < shrinkSize; ++i) {
            tmpList.add(poll());
        }
        clear();
        super.addAll(tmpList);
    }
}
