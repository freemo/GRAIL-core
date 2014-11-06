package com.syncleus.grail.neural;

import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.modules.javahandler.*;

import java.util.Random;

public abstract class AbstractSynapse implements Synapse, EdgeFrame {
    private static final Random RANDOM = new Random();

    @Initializer
    public void init() {
        this.setWeight(((RANDOM.nextDouble() * 2.0) - 1.0) / 1000.0);
    }
}
