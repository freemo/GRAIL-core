package com.syncleus.grail.graph;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.javahandler.*;
import com.tinkerpop.frames.modules.typedgraph.TypeField;

@TypeField("type")
public interface Node extends VertexFrame  {
    @Adjacency(label="targets")
    Iterable<? extends Node> getTargets();

    @TypedAdjacency(label="targets")
    <N extends Node> Iterable<? extends N> getTargets(Class<? extends N> type);

    @Adjacency(label="targets")
    void setTargets(Iterable<? extends Node> targets);

    @Adjacency(label="targets")
    void removeTarget(Node target);

    @Adjacency(label="targets")
    <N extends Node> N addTarget(Node target);

    @Adjacency(label="targets")
    Node addTarget();

    @TypedAdjacency(label="targets")
    <N extends Node> N addTarget(Class<? extends N> type);

    @Adjacency(label="targets", direction= Direction.IN)
    Iterable<? extends Node> getSources();

    @TypedAdjacency(label="targets", direction=Direction.IN)
    <N extends Node> Iterable<? extends N> getSources(Class<? extends N> type);

    @Adjacency(label="targets", direction=Direction.IN)
    void setSources(Iterable<? extends Node> targets);

    @Adjacency(label="targets", direction=Direction.IN)
    void removeSource(Node target);

    @Adjacency(label="targets", direction=Direction.IN)
    <N extends Node> N addSource(Node target);

    @Adjacency(label="targets", direction=Direction.IN)
    Node addSource();

    @TypedAdjacency(label="targets", direction=Direction.IN)
    <N extends Node> N addSource(Class<? extends N> type);

    @Incidence(label = "targets")
    Iterable<? extends Edge> getTargetEdges();

    @TypedIncidence(label="targets")
    <E extends Edge> Iterable<? extends E> getTargetEdges(Class<? extends E> type);

    @Incidence(label = "targets")
    <E extends Edge> E addTargetEdge(Edge target);

    @Incidence(label = "targets")
    void removeTargetEdge(Edge target);

    @Incidence(label = "targets", direction=Direction.IN)
    Iterable<? extends Edge> getSourceEdges();

    @TypedIncidence(label="targets", direction=Direction.IN)
    <E extends Edge> Iterable<? extends E> getSourceEdges(Class<? extends E> type);

    @Incidence(label = "targets", direction=Direction.IN)
    <E extends Edge> E addSourceEdge(Edge target);

    @Incidence(label = "targets", direction=Direction.IN)
    void removeSourceEdge(Edge source);
}
