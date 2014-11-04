package com.syncleus.grail.graph;

import com.syncleus.grail.activation.*;
import com.syncleus.grail.neural.*;
import com.syncleus.grail.neural.backprop.*;
import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;
import com.tinkerpop.frames.modules.typedgraph.TypedGraphModuleBuilder;
import com.tinkerpop.rexster.util.MockTinkerTransactionalGraph;
import org.apache.commons.configuration.*;
import org.junit.*;

import java.io.File;
import java.util.*;

import static com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration.INDEX_BACKEND_KEY;
import static com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration.STORAGE_DIRECTORY_KEY;

public class OrTest {
   @Test
    public void testOr() {
        final FramedTransactionalGraph<?> graph = OrTest.makeBlankGraph();

        final List<BackpropNeuron> newInputNeurons = new ArrayList<BackpropNeuron>(2);
        newInputNeurons.add(OrTest.createNeuron(graph, "input"));
        newInputNeurons.add(OrTest.createNeuron(graph, "input"));
        final BackpropNeuron newOutputNeuron = OrTest.createNeuron(graph, "output");

        //connect all hidden neurons to the output neuron
        for( BackpropNeuron inputNeurons : newInputNeurons ) {
            graph.addEdge(null, inputNeurons.asVertex(), newOutputNeuron.asVertex(), "targets", BackpropSynapse.class);
        }
        //create bias neuron for output neuron
        final BackpropNeuron biasNeuron = OrTest.createNeuron(graph, "bias");
        biasNeuron.setSignal(1.0);
        graph.addEdge(null, biasNeuron.asVertex(), newOutputNeuron.asVertex(), "targets", BackpropSynapse.class);
        graph.commit();

        for(int i = 0; i < 10000; i++) {
            OrTest.train(graph, 0.0, 1.0, 1.0);
            OrTest.train(graph, 1.0, 0.0, 1.0);
            OrTest.train(graph, 1.0, 1.0, 1.0);
            OrTest.train(graph, 0.0, 0.0, 0.0);
        }

        Assert.assertTrue(OrTest.propagate(graph, 1.0, 1.0) > 0.75);
        Assert.assertTrue(OrTest.propagate(graph, 0.0, 0.0) < 0.25);
        Assert.assertTrue(OrTest.propagate(graph, 1.0, 0.0) > 0.75);
        Assert.assertTrue(OrTest.propagate(graph, 0.0, 1.0) > 0.75);
    }

    private static final ActivationFunction activationFunction = new SineActivationFunction();

    private static void train(final FramedTransactionalGraph<?> graph, final double input1, final double input2, final double expected) {
        OrTest.propagate(graph, input1, input2);

        final Iterator<BackpropNeuron> outputNeurons = graph.getVertices("layer", "output", BackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        outputNeuron.setDeltaTrain((expected - outputNeuron.getSignal()) * activationFunction.activateDerivative(outputNeuron.getActivity()) );
        graph.commit();

        final Iterator<BackpropNeuron> inputNeurons = graph.getVertices("layer", "input", BackpropNeuron.class).iterator();
        inputNeurons.next().backpropagate();
        inputNeurons.next().backpropagate();
        Assert.assertTrue(!inputNeurons.hasNext());
        graph.commit();

        final Iterator<BackpropNeuron> biasNeurons = graph.getVertices("layer", "bias", BackpropNeuron.class).iterator();
        biasNeurons.next().backpropagate();
        Assert.assertTrue(!biasNeurons.hasNext());
        graph.commit();
    }

    private static void printGraph(final FramedTransactionalGraph<?> graph) {
        final Iterator<BackpropNeuron> inputNeurons = graph.getVertices("layer", "input", BackpropNeuron.class).iterator();
        System.out.println("input signal: " + inputNeurons.next().getSignal());
        System.out.println("input signal: " + inputNeurons.next().getSignal());
        Assert.assertTrue(!inputNeurons.hasNext());

        final Iterator<BackpropNeuron> outputNeurons = graph.getVertices("layer", "output", BackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());

        System.out.println("outputNeuron signal: " + outputNeuron.getSignal());
        System.out.println("outputNeuron activity: " + outputNeuron.getActivity());
        System.out.println("outputNeuron source size: " + OrTest.getIteratorSize(outputNeuron.getSourceEdges().iterator()));
        System.out.println("outputNeuron source synapses: ");
        for( BackpropSynapse sourceEdge : outputNeuron.getSourceEdges(BackpropSynapse.class) ) {
            System.out.println("weight: " + sourceEdge.getWeight());
            System.out.println("signal: " + sourceEdge.getSource().getSignal());
            System.out.println("layer: " + sourceEdge.getSource().asVertex().getProperty("layer"));
        }
        System.out.println();
    }

    private static int getIteratorSize(Iterator it) {
        int count = 0;
        while(it.hasNext()) {
            it.next();
            count++;
        }
        return count;
    }

    private static double propagate(final FramedTransactionalGraph<?> graph, final double input1, final double input2) {
        final Iterator<BackpropNeuron> inputNeurons = graph.getVertices("layer", "input", BackpropNeuron.class).iterator();
        inputNeurons.next().setSignal(input1);
        inputNeurons.next().setSignal(input2);
        Assert.assertTrue(!inputNeurons.hasNext());
        graph.commit();

        final Iterator<BackpropNeuron> outputNeurons = graph.getVertices("layer", "output", BackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        outputNeuron.tick();
        graph.commit();
        return outputNeuron.getSignal();
    }

    private static BackpropNeuron createNeuron(final FramedGraph<?> graph, final String layer) {
        final BackpropNeuron neuron = graph.addVertex(null, BackpropNeuron.class);
        neuron.asVertex().setProperty("layer", layer);
        return neuron;
    }

    private static FramedTransactionalGraph makeBlankGraph() {
        BaseConfiguration config = new BaseConfiguration();
        Configuration storage = config.subset(GraphDatabaseConfiguration.STORAGE_NAMESPACE);
        // configuring local backend
        storage.setProperty(GraphDatabaseConfiguration.STORAGE_BACKEND_KEY, "local");
        storage.setProperty(GraphDatabaseConfiguration.STORAGE_DIRECTORY_KEY, "./target/TitanBackpropDB");
        // configuring elastic search index
        Configuration index = storage.subset(GraphDatabaseConfiguration.INDEX_NAMESPACE).subset("search");
        index.setProperty(INDEX_BACKEND_KEY, "elasticsearch");
        index.setProperty("local-mode", true);
        index.setProperty("client-only", false);
        index.setProperty(STORAGE_DIRECTORY_KEY, "./target/TitanBackpropDB" + File.separator + "es");

        TitanGraph graph = TitanFactory.open(config);
        if( graph.getVertices().iterator().hasNext() )
            throw new IllegalStateException("fix this!");

        final Module typedModule = new TypedGraphModuleBuilder()
                .withClass(Synapse.class)
                .withClass(BackpropSynapse.class)
                .withClass(BackpropNeuron.class)
                .build();

        final FramedGraphFactory factory = new FramedGraphFactory(typedModule, new GremlinGroovyModule(), new JavaHandlerModule());

        //return factory.create(graph);
        return factory.create(new MockTinkerTransactionalGraph());
    }
}
