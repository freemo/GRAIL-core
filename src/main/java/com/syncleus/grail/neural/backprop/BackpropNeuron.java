package com.syncleus.grail.neural.backprop;

import com.syncleus.grail.neural.*;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.javahandler.*;

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

    @Property("learningRate")
    void setDeltaTrain(double deltaTrain);
}
