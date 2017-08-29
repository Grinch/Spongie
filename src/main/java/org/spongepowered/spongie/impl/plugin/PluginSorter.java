/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin;

import org.spongepowered.spongie.impl.plugin.loader.PluginCandidate;
import org.spongepowered.spongie.impl.util.graph.DirectedGraph;
import org.spongepowered.spongie.impl.util.graph.TopologicalOrder;

import java.util.List;

class PluginSorter {

    static List<PluginCandidate> sort(Iterable<PluginCandidate> candidates) {
        DirectedGraph<PluginCandidate> graph = new DirectedGraph<>();

        for (PluginCandidate candidate : candidates) {
            graph.add(candidate);
            for (PluginCandidate dependency : candidate.getDependencies()) {
                graph.addEdge(candidate, dependency);
            }
        }

        return TopologicalOrder.createOrderedLoad(graph);
    }
}
