/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.util.graph;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

public class CycleDetector<T> {

    private final DirectedGraph<T> graph;
    private Set<DirectedGraph.DataNode<T>> marked;

    public CycleDetector(DirectedGraph<T> g) {
        this.graph = g;
    }

    public boolean hasCycle() {
        this.marked = Sets.newHashSet();
        List<DirectedGraph.DataNode<T>> all = Lists.newArrayList(this.graph.getNodes());
        while (!all.isEmpty()) {
            DirectedGraph.DataNode<T> n = all.get(0);
            boolean cycle = dfs(n);
            if (cycle) {
                return true;
            }
            all.removeAll(this.marked);
            this.marked.clear();
        }
        return false;
    }

    private boolean dfs(DirectedGraph.DataNode<T> root) {
        this.marked.add(root);
        for (DirectedGraph.DataNode<T> a : root.getAdjacent()) {
            if (!this.marked.contains(a)) {
                return dfs(a);
            }
            return true;
        }
        return false;
    }

}
