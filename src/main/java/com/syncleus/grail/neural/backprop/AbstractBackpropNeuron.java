package com.syncleus.grail.neural.backprop;

import com.syncleus.grail.neural.*;
import com.tinkerpop.frames.modules.javahandler.Initializer;

public abstract class AbstractBackpropNeuron extends AbstractActivationNeuron implements BackpropNeuron {
    @Initializer
    public void init() {
        this.setLearningRate(0.001);
        this.setDeltaTrain(0.0);
    }

    @Override
    public void backpropagate() {
        double newDeltaTrain = 0.0;
        for (final BackpropSynapse synapse : this.getTargetEdges(BackpropSynapse.class)) {
            final BackpropNeuron target = synapse.getTarget();
            newDeltaTrain += (synapse.getWeight() * target.getDeltaTrain());
        }
        newDeltaTrain *= this.getActivationFunction().activateDerivative(this.getActivity());
        this.setDeltaTrain(newDeltaTrain);

        for(final Synapse synapse : this.getSourceEdges(Synapse.class)) {
            synapse.setWeight(synapse.getWeight() + (this.getDeltaTrain() * this.getLearningRate() * synapse.getSource().getSignal()));
        }
    }
}
