package com.syncleus.grail.neural;

import com.syncleus.grail.activation.*;
import com.syncleus.grail.graph.Edge;
import com.syncleus.grail.neural.backprop.*;
import com.tinkerpop.frames.modules.javahandler.Initializer;

public abstract class AbstractActivationNeuron extends AbstractNeuron implements ActivationNeuron {

    private ActivationFunction activationFunction;

    @Initializer
    public void init() {
        this.setActivationFunctionClass(SineActivationFunction.class);
        this.setActivity(0.0);
    }

    protected ActivationFunction getActivationFunction() {
        final Class<? extends ActivationFunction> activationClass = this.getActivationFunctionClass();
        if( (this.activationFunction != null) && (this.activationFunction.getClass().equals(activationClass)) )
            return this.activationFunction;

        this.activationFunction = null;
        try {
            this.activationFunction = activationClass.newInstance();
        }
        catch( final InstantiationException caughtException ) {
            throw new IllegalStateException("activation function does not have a public default constructor", caughtException);
        }
        catch( final IllegalAccessException caughtException ) {
            throw new IllegalStateException("activation function does not have a public default constructor", caughtException);
        }

        return this.activationFunction;
    }

    @Override
    public void tick() {
        // calculate the current input activity
        this.setActivity(0.0);
//        System.out.println("trying to tick");
        for (final Edge currentEdge : this.getSourceEdges(BackpropSynapse.class)) {
//            System.out.println("got an edge");
            if( currentEdge instanceof Synapse ) {
//                System.out.println("it was a synapse");
                final Synapse currentSynapse = (Synapse) currentEdge;
                this.setActivity(this.getActivity() + currentSynapse.getSource().getSignal() * currentSynapse.getWeight());
//                System.out.println();
//                System.out.println("activity was set: " + this.getActivity() + " the weight detected is: " + currentSynapse.getWeight() + "the source signal detected is: " + currentSynapse.getSource().getSignal());
//                System.out.println("source layer: " + currentSynapse.getSource().asVertex().getProperty("layer").toString());
//                System.out.println("target layer: " + currentSynapse.getTarget().asVertex().getProperty("layer").toString());
//                System.out.println("my layer: " + this.asVertex().getProperty("layer").toString());
            }
        }
        // calculate the activity function and set the result as the output
        this.setSignal( this.getActivationFunction().activate(this.getActivity()) );
//        System.out.println("signal was set: " + this.getSignal());
    }
}
