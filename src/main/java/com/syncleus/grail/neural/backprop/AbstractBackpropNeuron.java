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
        for (final Synapse synapse : this.getTargetSynapses()) {
            final Neuron targetNeuron = synapse.getTarget();
            if( ! (targetNeuron instanceof BackpropNeuron) )
                throw new IllegalStateException("A backprop neuron is connected to a non-backprop neuron.");
            final BackpropNeuron target = (BackpropNeuron) targetNeuron;
            newDeltaTrain += (synapse.getWeight() * target.getDeltaTrain());
        }
        newDeltaTrain *= this.getActivationFunction().activateDerivative(this.getActivity());
        this.setDeltaTrain(newDeltaTrain);

        for(final Synapse synapse : this.getSourceSynapses()) {
            synapse.setWeight(synapse.getWeight() + (this.getDeltaTrain() * this.getLearningRate() * synapse.getSource().getSignal()));
        }
    }
}
