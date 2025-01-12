package com.magneto.dependency;

import java.util.*;

public class MavenDependencyChain {
    private final List<MavenDependency> dependencyList;

    public MavenDependencyChain(List<MavenDependency> dependencyList) {
        if (dependencyList == null || dependencyList.isEmpty()) {
            throw new RuntimeException("dependency list can not be null or empty");
        }
        this.dependencyList = dependencyList;
    }

    public MavenDependency getVulDependency() {
        return this.reverseChainIterator().next();
    }

    public List<MavenDependency> forwardChainList() {
        return dependencyList;
    }

    public List<MavenDependency> reverseChainList() {
        List<MavenDependency> reversed = new ArrayList<>(dependencyList);
        Collections.reverse(reversed);
        return reversed;
    }

    public Iterator<MavenDependency> forwardChainIterator() {
        return dependencyList.iterator();
    }

    public Iterator<MavenDependency> reverseChainIterator() {
        List<MavenDependency> reversed = new ArrayList<>(dependencyList);
        Collections.reverse(reversed);
        return reversed.iterator();
    }

    public int length() {
        return dependencyList.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MavenDependencyChain that = (MavenDependencyChain) o;
        return Objects.equals(dependencyList, that.dependencyList);
    }

    @Override
    public int hashCode() {
        return dependencyList != null ? dependencyList.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dependencyList.size(); i++) {
            for (int j = 0; j < i; j++) sb.append("  ");
            sb.append(dependencyList.get(i).toString());
            sb.append('\n');
        }
        return sb.toString();
    }
}
