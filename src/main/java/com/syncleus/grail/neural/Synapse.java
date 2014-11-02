package com.syncleus.grail.neural;

import com.syncleus.grail.graph.Edge;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.javahandler.*;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Synapse")
@JavaHandlerClass(AbstractSynapse.class)
public interface Synapse extends Edge {
    @Property("weight")
    public Double getWeight();
    @Property("weight")
    public void setWeight(double weight);

    @OutVertex
    Neuron getTarget();

    @InVertex
    Neuron getSource();
}