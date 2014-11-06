package com.syncleus.grail.graph;

import com.syncleus.grail.graph.SignalingNode;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.javahandler.*;

public abstract class AbstractSignalingNode implements SignalingNode, VertexFrame {
    @Initializer
    public void init() {
        this.setSignal(0.0);
    }

}