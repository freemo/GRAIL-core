package com.syncleus.grail.neural.backprop;

import com.syncleus.grail.neural.*;
import com.tinkerpop.frames.modules.javahandler.Initializer;

public abstract class AbstractBackpropNeuron extends AbstractActivationNeuron implements BackpropNeuron {
    @Initializer
    public void init() {
        this.setLearningRate(0.0175);
        this.setDeltaTrain(0.0);
    }

    @Override
    public void backpropagate() {
//        System.out.println(this.asVertex().getProperty("layer") + " trying to backprop");
        double newDeltaTrain = 0.0;
        for (final BackpropSynapse synapse : this.getTargetEdges(BackpropSynapse.class)) {
//            System.out.println("calculating deltaTrain for a synapse");
            final BackpropNeuron target = synapse.getTarget();
//            System.out.println("synapse weight: " + synapse.getWeight() + " target deltaTrain: " + target.getDeltaTrain() + " my layer: " + this.asVertex().getProperty("layer") + " target layer: " + target.asVertex().getProperty("layer"));
//            assert synapse.getWeight() != null;
//            assert target.getDeltaTrain() != null;
            newDeltaTrain += (synapse.getWeight() * ( target.getDeltaTrain() == null ? 0.0 : target.getDeltaTrain()));
        }
        newDeltaTrain *= this.getActivationFunction().activateDerivative(this.getActivity());
//        System.out.println("deltaTrain updated: " + this.getDeltaTrain() + " -> " + newDeltaTrain);
        this.setDeltaTrain(newDeltaTrain);

//        System.out.println("Updating weights");
        for (final BackpropSynapse synapse : this.getTargetEdges(BackpropSynapse.class)) {
//            System.out.println("calculating weight for target synapse old: " + synapse.getWeight());
            final BackpropNeuron target = synapse.getTarget();
            synapse.setWeight(synapse.getWeight() + ((target.getDeltaTrain() == null ? 0.0 : target.getDeltaTrain()) * target.getLearningRate() * this.getSignal()));
//            System.out.println("calculating weight for target synapse new: " + synapse.getWeight());
        }
    }
}
