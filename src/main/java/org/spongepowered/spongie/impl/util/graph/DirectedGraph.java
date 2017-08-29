/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.util.graph;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class DirectedGraph<D> {

    protected final List<DataNode<D>> nodes = Lists.newArrayList();

    public DirectedGraph() {

    }

    public int getNodeCount() {
        return this.nodes.size();
    }

    public int getEdgeCount() {
        int count = 0;
        for (DataNode<D> n : this.nodes) {
            count += n.getEdgeCount();
        }
        return count;
    }

    public boolean contains(D data) {
        for (DataNode<D> node : this.nodes) {
            if (node.data.equals(data)) {
                return true;
            }
        }
        return false;
    }

    public DataNode<D> getNode(D data) {
        for (DataNode<D> node : this.nodes) {
            if (node.data.equals(data)) {
                return node;
            }
        }
        return null;
    }

    public void addEdge(D from, D to) {
        add(from);
        add(to);
        DataNode<D> fromNode = getNode(from);
        DataNode<D> toNode = getNode(to);
        addEdge(fromNode, toNode);
    }

    public void addEdge(DataNode<D> from, DataNode<D> to) {
        if (!this.nodes.contains(from)) {
            this.nodes.add(from);
        }
        if (!this.nodes.contains(to)) {
            this.nodes.add(to);
        }
        if (!from.isAdjacent(to)) {
            from.addEdge(to);
        }
    }

    public Iterable<DataNode<D>> getNodes() {
        return this.nodes;
    }

    public DirectedGraph<D> reverse() {
        DirectedGraph<D> rev = new DirectedGraph<>();
        Map<DataNode<D>, DataNode<D>> siblings = Maps.newHashMap();
        for (DataNode<D> n : this.nodes) {
            DataNode<D> b = n.clone();
            siblings.put(n, b);
        }
        for (DataNode<D> n : this.nodes) {
            for (DataNode<D> b : n.getAdjacent()) {
                rev.addEdge(siblings.get(b), siblings.get(n));
            }
        }
        return rev;
    }

    public DataNode<D> add(D d) {
        if (!contains(d)) {
            final DataNode<D> node = new DataNode<>(d);
            this.nodes.add(node);
        }
        return getNode(d);
    }

    public void add(DataNode<D> n) {
        if (!contains(n.data)) {
            this.nodes.add(n);
        }
    }

    public void delete(D n) {
        DataNode<D> node = null;
        for (DataNode<D> node1 : this.nodes) {
            if (node1.data.equals(n)) {
                node = node1;
                break;
            }
        }
        if (node != null) {
            delete(node);
        }
    }

    public void delete(DataNode<D> n) {
        for (DataNode<D> b : this.nodes) {
            b.deleteEdge(n);
        }
        this.nodes.remove(n);
    }

    @Override
    public String toString() {
        String s = getNodeCount() + "\n" + getEdgeCount() + "\n";
        for (DataNode<D> n : this.nodes) {
            s += ((DataNode<?>) n).getData().toString() + " ";
            for (DataNode<D> a : n.getAdjacent()) {
                s += this.nodes.indexOf(a) + " ";
            }
            s += "\n";
        }
        return s;
    }

    public static class DataNode<D> {

        final D data;
        private final List<DataNode<D>> adj = Lists.newArrayList();

        public DataNode(D obj) {
            this.data = obj;
        }

        public D getData() {
            return this.data;
        }

        public void addEdge(DataNode<D> other) {
            this.adj.add(other);
        }

        public void deleteEdge(DataNode<D> other) {
            this.adj.remove(other);
        }

        public boolean isAdjacent(DataNode<D> other) {
            return this.adj.contains(other);
        }

        public int getEdgeCount() {
            return this.adj.size();
        }

        public Iterable<DataNode<D>> getAdjacent() {
            return this.adj;
        }

        @Override
        public DataNode<D> clone() {
            return new DataNode<>(this.data);
        }

        @Override
        public int hashCode() {
            return this.data.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof DataNode)) {
                return false;
            }
            DataNode<?> d = (DataNode<?>) o;
            return d.getData().equals(this.data);
        }

    }

}
