package com.syncleus.grail.neural;


import com.syncleus.grail.graph.Node;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.javahandler.*;

@JavaHandlerClass(AbstractNeuron.class)
public interface Neuron extends Node {
    @Property("signal")
    Double getSignal();

    @Property("signal")
    void setSignal(double signal);
}
