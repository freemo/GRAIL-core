package com.syncleus.grail.neural;

import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.javahandler.*;

public abstract class AbstractNeuron implements Neuron, VertexFrame {
    @Initializer
    public void init() {
        this.setSignal(0.0);
    }

}