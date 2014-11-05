package com.syncleus.grail.graph;

import com.tinkerpop.blueprints.Direction;

import java.lang.annotation.*;

/**
 * Incidences annotate getters and adders to represent a Vertex incident to an Edge.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TypedIncidence {
    /**
     * The labels of the edges that are incident to the vertex.
     *
     * @return the edge label
     */
    public String label();

    /**
     * The direction of the edges.
     *
     * @return the edge direction
     */
    public Direction direction() default Direction.OUT;
}
