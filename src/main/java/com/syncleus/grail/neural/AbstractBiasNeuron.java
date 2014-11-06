package com.syncleus.grail.neural;

import com.syncleus.grail.graph.AbstractSignalingNode;
import com.tinkerpop.frames.modules.javahandler.Initializer;

public abstract class AbstractBiasNeuron extends AbstractSignalingNode {

    @Initializer
    public void init() {
        this.setSignal(1.0);
    }
}
