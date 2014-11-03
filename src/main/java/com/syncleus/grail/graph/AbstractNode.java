package com.syncleus.grail.graph;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.modules.javahandler.*;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;
import com.tinkerpop.gremlin.java.GremlinPipeline;

// TODO: currently this only handles the immediate class, let it handle subclasses too.
public abstract class AbstractNode implements JavaHandlerContext<Vertex>, Node {
    @Override
    public <N extends Node> Iterable<? extends N> getTargets(final Class<? extends N> type) {
        final TypeValue typeValue = AbstractNode.determineTypeValue(type);
        return this.frameVertices(this.gremlin().out("targets").has("type", typeValue.value()).V(), type);
    }

    @Override
    public <N extends Node> N addTarget(final Node target, final Class<? extends N> type) {
        if( ! type.isInstance(target) )
            throw new IllegalArgumentException("target is not of the indicated type");
        AbstractNode.determineTypeValue(type);

        final Node newTarget = this.addTarget(target);
        assert type.isInstance(newTarget);

        return (N) newTarget;
    }

    @Override
    public <N extends Node> N addTarget(final Class<? extends N> type) {
        AbstractNode.determineTypeValue(type);

        final N target = this.frame(this.g().addVertex(null), type);
        final Node newTarget = this.addTarget(target);
        assert type.isInstance(newTarget);

        return (N) newTarget;
    }

    @Override
    public <N extends Node> Iterable<? extends N> getSources(final Class<? extends N> type) {
        final TypeValue typeValue = AbstractNode.determineTypeValue(type);
        return this.frameVertices(this.gremlin().in("targets").has("type", typeValue.value()).V(), type);
    }

    @Override
    public <N extends Node> N addSource(final Node source, final Class<? extends N> type) {
        if( ! type.isInstance(source) )
            throw new IllegalArgumentException("target is not of the indicated type");
        AbstractNode.determineTypeValue(type);

        final Node newSource = this.addSource(source);
        assert type.isInstance(newSource);

        return (N) newSource;
    }

    @Override
    public <N extends Node> N addSource(final Class<? extends N> type) {
        AbstractNode.determineTypeValue(type);

        final N source = this.frame(this.g().addVertex(null), type);
        final Node newSource = this.addSource(source);
        assert type.isInstance(newSource);

        return (N) newSource;
    }

    @Override
    public <E extends Edge> Iterable<? extends E> getTargetEdges(final Class<? extends E> type) {
//        System.out.println("in getTargetEdges");
        final TypeValue typeValue = AbstractNode.determineTypeValue(type);
//        System.out.println("determined typeValue" + typeValue);
//        GremlinPipeline<Vertex, ?> pipe = this.gremlin().out("targets");
        GremlinPipeline<Vertex, com.tinkerpop.blueprints.Edge> pipe = this.gremlin().outE("targets");//.has("type", typeValue.value());
//        pipe.count();
//        System.out.println("constructed pipe: " + pipe.count());
        return this.frameEdges(pipe, type);
    }

    @Override
    public <E extends Edge> E addTargetEdge(final Edge target, final Class<? extends E> type) {
        if( ! type.isInstance(target) )
            throw new IllegalArgumentException("target is not of the indicated type");
        AbstractNode.determineTypeValue(type);

        final Edge newTarget = this.addTargetEdge(target);
        assert type.isInstance(newTarget);

        return (E) newTarget;
    }

    @Override
    public <E extends Edge> Iterable<? extends E> getSourceEdges(final Class<? extends E> type) {
        final TypeValue typeValue = AbstractNode.determineTypeValue(type);
        return this.frameEdges(this.gremlin().inE("targets").has("type", typeValue.value()).E(), type);
    }

    @Override
    public <E extends Edge> E addSourceEdge(final Edge target, final Class<? extends E> type) {
        if( ! type.isInstance(target) )
            throw new IllegalArgumentException("target is not of the indicated type");
        AbstractNode.determineTypeValue(type);

        final Edge newTarget = this.addSourceEdge(target);
        assert type.isInstance(newTarget);

        return (E) newTarget;
    }

    private static TypeValue determineTypeValue(final Class<?> type) {
        TypeValue typeValue = type.getAnnotation(TypeValue.class);
        if( typeValue == null )
            throw new IllegalArgumentException("The specified type does not have a TypeValue annotation");
        return typeValue;
    }
}
