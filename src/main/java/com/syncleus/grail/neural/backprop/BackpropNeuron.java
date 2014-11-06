package com.syncleus.grail.neural.backprop;

import com.syncleus.grail.graph.*;
import com.syncleus.grail.neural.*;
import com.tinkerpop.frames.*;
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

    @Adjacency(label="targets")
    Iterable<? extends BackpropNeuron> getTargets();

    @TypedAdjacency(label="targets")
    <N extends BackpropNeuron> Iterable<? extends N> getTargets(Class<? extends N> type);

    @Adjacency(label="targets")
    void setTargets(Iterable<? extends BackpropNeuron> targets);

    @Adjacency(label="targets")
    void removeTarget(BackpropNeuron target);

    @Adjacency(label="targets")
    <N extends BackpropNeuron> N addTarget(N target);

    @Adjacency(label="targets")
    BackpropNeuron addTarget();

    @TypedAdjacency(label="targets")
    <N extends BackpropNeuron> N addTarget(Class<? extends N> type);

    @Incidence(label = "targets")
    Iterable<? extends BackpropSynapse> getTargetEdges();

    @TypedIncidence(label="targets")
    <E extends BackpropSynapse> Iterable<? extends E> getTargetEdges(Class<? extends E> type);

    @Incidence(label = "targets")
    <E extends BackpropSynapse> E addTargetEdge(E target);

    @Incidence(label = "targets")
    void removeTargetEdge(BackpropSynapse target);

}
