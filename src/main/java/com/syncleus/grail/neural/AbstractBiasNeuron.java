package com.syncleus.grail.neural;

import com.tinkerpop.frames.modules.javahandler.Initializer;

public abstract class AbstractBiasNeuron extends AbstractNeuron {

    @Initializer
    public void init() {
        this.setSignal(1.0);
    }

    @Override
    public void tick() {
        return;
    }
}
