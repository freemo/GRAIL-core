package com.syncleus.grail.neural.backprop;

import com.syncleus.grail.neural.*;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.javahandler.*;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("BackpropNeuron")
@JavaHandlerClass(AbstractBackpropNeuron.class)
public interface BackpropNeuron extends ActivationNeuron {
    @JavaHandler
    void backpropagate();

    @Property("learningRate")
    Double getLearningRate();

    @Property("learningRate")
    void setLearningRate(double learningRate);

    @Property("deltaTrain")
    Double getDeltaTrain();

    @Property("deltaTrain")
    void setDeltaTrain(double deltaTrain);
}
