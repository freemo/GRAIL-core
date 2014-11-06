package com.syncleus.grail.graph;

import com.tinkerpop.frames.modules.javahandler.*;

public interface Propagator extends Signaler {
    void propagate();
}
