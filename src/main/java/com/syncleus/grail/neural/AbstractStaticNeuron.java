package com.syncleus.grail.neural;

import com.tinkerpop.frames.modules.javahandler.Initializer;

public abstract class AbstractStaticNeuron extends AbstractNeuron implements StaticNeuron {
    @Initializer
    public void init() {
        this.setSignal(1.0);
    }
}
