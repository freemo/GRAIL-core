package com.syncleus.grail.graph;

import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.javahandler.*;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Synapse")
@JavaHandlerClass(AbstractSignalMultiplyingEdge.class)
public interface SignalMultiplyingEdge extends WeightedEdge, Signaler, Propagator {
    @InVertex
    SignalingNode getTarget();

    @OutVertex
    SignalingNode getSource();

    @Override
    @JavaHandler
    void propagate();
}