package com.syncleus.grail.graph;

import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.typedgraph.TypeField;

@TypeField("type")
public interface Edge extends EdgeFrame {
    @InVertex
    Node getTarget();

    @OutVertex
    Node getSource();
}
