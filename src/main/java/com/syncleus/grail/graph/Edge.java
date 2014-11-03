package com.syncleus.grail.graph;

import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.typedgraph.TypeField;

@TypeField("type")
@JavaHandlerClass(AbstractEdge.class)
public interface Edge extends EdgeFrame {
    @InVertex
    Node getTarget();

    @OutVertex
    Node getSource();
}
