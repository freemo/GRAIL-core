package com.syncleus.grail.graph;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.modules.javahandler.*;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public abstract class AbstractNode implements JavaHandlerContext<Vertex>, Node {
    @Override
    public <E extends Edge> Iterable<? extends E> getTargetEdges(final Class<? extends E> type) {
        final TypeValue typeValue = AbstractNode.determineTypeValue(type);
        GremlinPipeline<Vertex, com.tinkerpop.blueprints.Edge> pipe = this.gremlin().outE("targets");//.has("type", typeValue.value());
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
        return this.frameEdges(this.gremlin().inE("targets")/*.has("type", typeValue.value()).E()*/, type);
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
