package com.syncleus.grail.neural;

import com.syncleus.grail.activation.*;
import com.syncleus.grail.graph.Edge;
import com.tinkerpop.frames.modules.javahandler.Initializer;

public abstract class AbstractActivationNeuron extends AbstractNeuron implements ActivationNeuron {

    private ActivationFunction activationFunction;

    @Initializer
    public void init() {
        this.setActivationFunctionClass(HyperbolicTangentActivationFunction.class);
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
        for (final Edge currentEdge : this.getSourceEdges()) {
            if( currentEdge instanceof Synapse ) {
                final Synapse currentSynapse = (Synapse) currentEdge;
                this.setActivity(this.getActivity() + currentSynapse.getSource().getSignal() * currentSynapse.getWeight());
            }
        }
        // calculate the activity function and set the result as the output
        this.setSignal( this.getActivationFunction().activate(this.getActivity()) );
    }
}
