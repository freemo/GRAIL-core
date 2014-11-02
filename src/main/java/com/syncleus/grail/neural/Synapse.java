package com.syncleus.grail.neural;

import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.javahandler.*;

@JavaHandlerClass(AbstractSynapse.class)
public interface Synapse extends EdgeFrame {
    @Property("weight")
    public Double getWeight();
    @Property("weight")
    public void setWeight(double weight);

    @OutVertex
    Neuron getTarget();

    @InVertex
    Neuron getSource();
}