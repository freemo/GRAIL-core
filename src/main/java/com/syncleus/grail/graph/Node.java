package com.syncleus.grail.graph;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.javahandler.*;
import com.tinkerpop.frames.modules.typedgraph.TypeField;

@TypeField("type")
@JavaHandlerClass(AbstractNode.class)
public interface Node extends VertexFrame  {
    @Adjacency(label="targets")
    Iterable<? extends Node> getTargets();

    @JavaHandler
    <N extends Node> Iterable<? extends N> getTargets(Class<? extends N> type);

    @Adjacency(label="targets")
    void setTargets(Iterable<? extends Node> targets);

    @Adjacency(label="targets")
    void removeTarget(Node target);

//    @Adjacency(label="targets")
//    Node addTarget(Node target);

//    @JavaHandler
//    <N extends Node> N addTarget(Node target, Class<? extends N> type);
    @Adjacency(label="targets")
    <N extends Node> N addTarget(Node target);

    @Adjacency(label="targets")
    Node addTarget();

    @JavaHandler
    <N extends Node> N addTarget(Class<? extends N> type);

    @Adjacency(label="targets", direction= Direction.IN)
    Iterable<? extends Node> getSources();

    @JavaHandler
    <N extends Node> Iterable<? extends N> getSources(Class<? extends N> type);

    @Adjacency(label="targets", direction=Direction.IN)
    void setSources(Iterable<? extends Node> targets);

    @Adjacency(label="targets", direction=Direction.IN)
    void removeSource(Node target);

    @Adjacency(label="targets", direction=Direction.IN)
    <N extends Node> N addSource(Node target);

//    @JavaHandler
//    <N extends Node> N addSource(Node target, Class<? extends N> type);

    @Adjacency(label="targets", direction=Direction.IN)
    Node addSource();

    @JavaHandler
    <N extends Node> N addSource(Class<? extends N> type);

    @Incidence(label = "targets")
    Iterable<? extends Edge> getTargetEdges();

    @JavaHandler
    <E extends Edge> Iterable<? extends E> getTargetEdges(Class<? extends E> type);

    @Incidence(label = "targets")
    Edge addTargetEdge(Edge target);

    @JavaHandler
    <E extends Edge> E addTargetEdge(Edge target, Class<? extends E> type);

    @Incidence(label = "targets")
    void removeTargetEdge(Edge target);

    @Incidence(label = "targets", direction=Direction.IN)
    Iterable<? extends Edge> getSourceEdges();

    @JavaHandler
    <E extends Edge> Iterable<? extends E> getSourceEdges(Class<? extends E> type);

    @Incidence(label = "targets", direction=Direction.IN)
    Edge addSourceEdge(Edge source);

    @JavaHandler
    <E extends Edge> E addSourceEdge(Edge target, Class<? extends E> type);

    @Incidence(label = "targets", direction=Direction.IN)
    void removeSourceEdge(Edge source);
}
