package com.syncleus.grail.neural;

import com.syncleus.grail.activation.ActivationFunction;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.javahandler.*;

@JavaHandlerClass(AbstractActivationNeuron.class)
public interface ActivationNeuron extends Neuron {
    @JavaHandler
    void tick();

    @Property("activity")
    Double getActivity();

    @Property("activity")
    void setActivity(double activity);

    @Property("activationFunction")
    Class<? extends ActivationFunction> getActivationFunctionClass();

    @Property("activationFunction")
    void setActivationFunctionClass(Class<? extends ActivationFunction> activationFunctionClass);
}
