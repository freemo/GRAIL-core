package com.syncleus.grail.neural;

import com.syncleus.grail.graph.SignalingNode;
import com.tinkerpop.frames.modules.javahandler.*;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("StaticNeuron")
@JavaHandlerClass(AbstractStaticNeuron.class)
public interface StaticNeuron extends SignalingNode {
}
