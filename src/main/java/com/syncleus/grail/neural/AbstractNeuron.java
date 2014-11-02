package com.syncleus.grail.neural;

import com.syncleus.grail.graph.AbstractNode;
import com.tinkerpop.frames.modules.javahandler.*;

public abstract class AbstractNeuron extends AbstractNode implements Neuron {
    @Initializer
    public void init() {
        this.setSignal(0.0);
    }

    @Override
    public void tick() {
    }
}