package com.syncleus.grail.neural;

import com.syncleus.grail.graph.AbstractSignalingNode;
import com.tinkerpop.frames.modules.javahandler.Initializer;

public abstract class AbstractStaticNeuron extends AbstractSignalingNode implements StaticNeuron {
    @Initializer
    public void init() {
        this.setSignal(1.0);
    }
}
