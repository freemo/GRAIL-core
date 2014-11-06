package com.syncleus.grail.graph;


import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.javahandler.*;

@JavaHandlerClass(AbstractSignalingNode.class)
public interface SignalingNode extends Node, Signaler {
}
