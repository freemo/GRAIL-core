package com.syncleus.grail.graph;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.frames.FramedGraphConfiguration;
import com.tinkerpop.frames.modules.Module;

public class GrailModule implements Module {
    @Override
    public Graph configure(final Graph baseGraph, final FramedGraphConfiguration config) {
        config.addMethodHandler(new TypedAdjacencyMethodHandler());
        config.addMethodHandler(new IncidenceMethodHandler());
        return baseGraph;
    }
}
