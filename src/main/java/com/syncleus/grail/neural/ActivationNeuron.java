package com.syncleus.grail.neural;

import com.syncleus.grail.activation.ActivationFunction;
import com.syncleus.grail.graph.*;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.javahandler.*;
import com.tinkerpop.frames.modules.typedgraph.TypeField;

@TypeField("type")
@JavaHandlerClass(AbstractActivationNeuron.class)
public interface ActivationNeuron extends Signaler, Propagator, VertexFrame {
    @Override
    @JavaHandler
    void propagate();

    @Property("activity")
    Double getActivity();

    @Property("activity")
    void setActivity(double activity);

    @Property("activationFunction")
    Class<? extends ActivationFunction> getActivationFunctionClass();

    @Property("activationFunction")
    void setActivationFunctionClass(Class<? extends ActivationFunction> activationFunctionClass);

    @Adjacency(label="targets", direction= Direction.IN)
    Iterable<? extends Signaler> getSources();

    @TypedAdjacency(label="targets", direction=Direction.IN)
    <N extends Signaler> Iterable<? extends N> getSources(Class<? extends N> type);

    @Adjacency(label="targets", direction=Direction.IN)
    void setSources(Iterable<? extends Signaler> targets);

    @Adjacency(label="targets", direction=Direction.IN)
    void removeSource(Signaler target);

    @Adjacency(label="targets", direction=Direction.IN)
    <N extends Signaler> N addSource(N target);

    @Adjacency(label="targets", direction=Direction.IN)
    Signaler addSource();

    @TypedAdjacency(label="targets", direction=Direction.IN)
    <N extends Signaler> N addSource(Class<? extends N> type);

    @Incidence(label = "targets", direction=Direction.IN)
    Iterable<? extends SignalMultiplyingEdge> getSourceEdges();

    @TypedIncidence(label="targets", direction=Direction.IN)
    <E extends SignalMultiplyingEdge> Iterable<? extends E> getSourceEdges(Class<? extends E> type);

    @Incidence(label = "targets", direction=Direction.IN)
    <E extends SignalMultiplyingEdge> E addSourceEdge(SignalMultiplyingEdge target);

    @Incidence(label = "targets", direction=Direction.IN)
    void removeSourceEdge(SignalMultiplyingEdge source);
}
