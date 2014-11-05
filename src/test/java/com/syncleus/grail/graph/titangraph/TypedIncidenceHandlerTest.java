package com.syncleus.grail.graph.titangraph;

import com.syncleus.grail.graph.*;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule;
import org.junit.*;

public class TypedIncidenceHandlerTest {
    @Test
    public void testGetSonEdges() {
        final TitanGraph godGraph = TitanGods.create("./target/TitanTestDB");
        final FramedGraphFactory factory = new FramedGraphFactory(new GrailModule(), new GremlinGroovyModule());

        final FramedGraph<?> framedGraph = factory.create(godGraph);

        final Iterable<God> gods = (Iterable<God>) framedGraph.getVertices("name", "jupiter", God.class);
        final God father = gods.iterator().next();
        Assert.assertEquals(father.getName(), "jupiter");

        final Iterable<? extends FatherEdge> childEdges = father.getSonEdges(FatherEdge.class);
        final FatherEdge childEdge = childEdges.iterator().next();
        Assert.assertEquals(childEdge.getSon().getName(), "hercules");
    }

    @Test
    public void testGetSonEdge() {
        final TitanGraph godGraph = TitanGods.create("./target/TitanTestDB");
        final FramedGraphFactory factory = new FramedGraphFactory(new GrailModule(), new GremlinGroovyModule());

        final FramedGraph framedGraph = factory.create(godGraph);

        final Iterable<God> gods = (Iterable<God>) framedGraph.getVertices("name", "jupiter", God.class);
        final God father = gods.iterator().next();
        Assert.assertEquals(father.getName(), "jupiter");

        final FatherEdge childEdge = father.getSonEdge(FatherEdge.class);
        Assert.assertEquals(childEdge.getSon().getName(), "hercules");
    }
}
