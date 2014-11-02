package com.syncleus.grail.neural;


import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.javahandler.*;

@JavaHandlerClass(AbstractNeuron.class)
public interface Neuron extends VertexFrame {
    @JavaHandler
    void tick();

    @Property("signal")
    Double getSignal();

    @Property("signal")
    void setSignal(double signal);

    @Adjacency(label="targets")
    Iterable<Neuron> getTargets();

    @Adjacency(label="targets")
    void setTargets(Iterable<Neuron> targets);

    @Adjacency(label="targets")
    void removeTarget(Neuron target);

    @Adjacency(label="targets")
    Neuron addTarget(Neuron target);

    @Adjacency(label="targets")
    Neuron addTarget();

    @Adjacency(label="targets", direction=Direction.IN)
    Iterable<Neuron> getSources();

    @Adjacency(label="targets", direction=Direction.IN)
    void setSources(Iterable<Neuron> targets);

    @Adjacency(label="targets", direction=Direction.IN)
    void removeSource(Neuron target);

    @Adjacency(label="targets", direction=Direction.IN)
    Neuron addSource(Neuron target);

    @Adjacency(label="targets", direction=Direction.IN)
    Neuron addSource();

    @Incidence(label = "targets")
    Iterable<Synapse> getTargetSynapses();

    @Incidence(label = "targets")
    Synapse addTargetSynapse(Synapse synapse);

    @Incidence(label = "targets")
    void removeTargetSynapse(Synapse synapse);

    @Incidence(label = "targets", direction=Direction.IN)
    Iterable<Synapse> getSourceSynapses();

    @Incidence(label = "targets", direction=Direction.IN)
    Synapse addSourceSynapse(Synapse synapse);

    @Incidence(label = "targets", direction=Direction.IN)
    void removeSourceSynapse(Synapse synapse);
}
