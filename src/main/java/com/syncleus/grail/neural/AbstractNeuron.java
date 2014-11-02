package com.syncleus.grail.neural;

import com.syncleus.grail.activation.HyperbolicTangentActivationFunction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.modules.javahandler.*;

public abstract class AbstractNeuron implements JavaHandlerContext<Vertex>, Neuron {
    @Initializer
    public void init() {
        this.setSignal(0.0);
    }
}