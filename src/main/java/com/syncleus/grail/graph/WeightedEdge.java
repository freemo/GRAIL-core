package com.syncleus.grail.graph;

import com.tinkerpop.frames.Property;

public interface WeightedEdge extends Edge {
    @Property("weight")
    public Double getWeight();
    @Property("weight")
    public void setWeight(double weight);
}
