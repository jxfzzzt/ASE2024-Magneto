package com.magneto.fuzz.mutate.strategy;

import com.magneto.fuzz.mutate.MutationStrategy;

import java.util.ArrayList;
import java.util.List;

public class MutateListStrategy implements MutationStrategy<List> {

    @Override
    public List mutate(Class clazz, List obj) {
        return new ArrayList();
    }
}
