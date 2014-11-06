package com.syncleus.grail.graph;

import com.tinkerpop.frames.Property;

public interface Signaler {
    @Property("signal")
    Double getSignal();

    @Property("signal")
    void setSignal(double signal);
}
