package com.syncleus.grail.neural;

import com.syncleus.grail.graph.AbstractEdge;
import com.tinkerpop.frames.modules.javahandler.*;

import java.util.Random;

public abstract class AbstractSynapse extends AbstractEdge implements Synapse {
    private static final Random RANDOM = new Random();

    @Initializer
    public void init() {
        this.setWeight(((RANDOM.nextDouble() * 2.0) - 1.0) / 10000.0);
    }
}
