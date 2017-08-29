/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.util.graph;

import com.google.common.collect.Sets;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

public class TopologicalOrder<T> {

    private final DirectedGraph<T> digraph;
    private Set<T> marked;

    public TopologicalOrder(DirectedGraph<T> graph) {
        this.digraph = graph;
    }

    public static <T> List<T> createOrderedLoad(DirectedGraph<T> graph) {
        final List<T> orderedList = new ArrayList<>();
        while (graph.getNodeCount() != 0) {
            DirectedGraph.DataNode<T> next = null;
            for (DirectedGraph.DataNode<T> node : graph.getNodes()) {
                if (node.getEdgeCount() == 0) {
                    next = node;
                    break;
                }
            }
            if (next == null) {
                throw new IllegalStateException("Graph is cyclic!");
            }
            orderedList.add(next.getData());
            graph.delete(next);
        }
        return orderedList;
    }

    @Nullable
    public Iterable<T> order(DirectedGraph.DataNode<T> root) {
        final CycleDetector<T> cycleDetector = new CycleDetector<>(this.digraph);
        if (cycleDetector.hasCycle()) {
            return null;
        }
        this.marked = Sets.newHashSet();
        ArrayDeque<T> order = new ArrayDeque<>();
        dfs(order, root);

        return order;
    }

    private void dfs(ArrayDeque<T> order, DirectedGraph.DataNode<T> root) {
        this.marked.add(root.getData());
        for (DirectedGraph.DataNode<T> n : root.getAdjacent()) {
            if (!this.marked.contains(n)) {
                dfs(order, n);
            }
        }
        order.push(root.getData());
    }
}