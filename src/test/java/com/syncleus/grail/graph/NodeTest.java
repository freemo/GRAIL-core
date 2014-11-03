package com.syncleus.grail.graph;

import com.syncleus.grail.neural.*;
import com.syncleus.grail.neural.backprop.*;
import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;
import com.tinkerpop.frames.modules.typedgraph.TypedGraphModuleBuilder;
import org.apache.commons.configuration.*;
import org.junit.*;

import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;

import static com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration.INDEX_BACKEND_KEY;
import static com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration.STORAGE_DIRECTORY_KEY;

public class NodeTest {
    @Test
    public void testNodeInstantiation() {
        final FramedTransactionalGraph<?> graph = NodeTest.makeBlankGraph();

        final List<BackpropNeuron> newInputNeurons = new ArrayList<BackpropNeuron>(2);
        newInputNeurons.add(NodeTest.createNeuron(graph, "input"));
        newInputNeurons.add(NodeTest.createNeuron(graph, "input"));
        final List<BackpropNeuron> newHiddenNeurons = new ArrayList<BackpropNeuron>(4);
        newHiddenNeurons.add(NodeTest.createNeuron(graph, "hidden"));
        newHiddenNeurons.add(NodeTest.createNeuron(graph, "hidden"));
        newHiddenNeurons.add(NodeTest.createNeuron(graph, "hidden"));
        newHiddenNeurons.add(NodeTest.createNeuron(graph, "hidden"));
        final BackpropNeuron newOutputNeuron = NodeTest.createNeuron(graph, "output");

        //connect all input neurons to hidden neurons
        for( BackpropNeuron inputNeuron : newInputNeurons ) {
            for( BackpropNeuron hiddenNeuron : newHiddenNeurons ) {
                graph.addEdge(null, inputNeuron.asVertex(), hiddenNeuron.asVertex(), "targets", BackpropSynapse.class);
            }
        }
        //connect all hidden neurons to the output neuron
        for( BackpropNeuron hiddenNeuron : newHiddenNeurons ) {
            graph.addEdge(null, hiddenNeuron.asVertex(), newOutputNeuron.asVertex(), "targets", BackpropSynapse.class);

            //create bias neuron
            final BackpropNeuron biasNeuron = NodeTest.createNeuron(graph, "bias");
            biasNeuron.setSignal(1.0);
            graph.addEdge(null, biasNeuron.asVertex(), hiddenNeuron.asVertex(), "targets", BackpropSynapse.class);
        }
        //create bias neuron for output neuron
        final BackpropNeuron biasNeuron = NodeTest.createNeuron(graph, "bias");
        biasNeuron.setSignal(1.0);
        graph.addEdge(null, biasNeuron.asVertex(), newOutputNeuron.asVertex(), "targets", BackpropSynapse.class);
        graph.commit();

        NodeTest.propagate(graph, 1.0, 1.0);
        NodeTest.printGraph(graph);
        NodeTest.propagate(graph, 0.0, 1.0);
        NodeTest.printGraph(graph);
        for(int i = 0; i < 1000; i++) {
//            try {
                NodeTest.train(graph, 0.0, 1.0, 1.0);
//            }
//            catch(UndeclaredThrowableException caught) {
//                caught.getUndeclaredThrowable().printStackTrace();
//                throw caught;
//            }
//            if(true) return;
            NodeTest.train(graph, 1.0, 0.0, 1.0);
            NodeTest.train(graph, 1.0, 1.0, 0.0);
            NodeTest.train(graph, 0.0, 0.0, 1.0);
        }
        NodeTest.propagate(graph, 1.0, 1.0);
        NodeTest.printGraph(graph);
        NodeTest.propagate(graph, 0.0, 1.0);
        NodeTest.printGraph(graph);
    }

    private static void train(final FramedTransactionalGraph<?> graph, final double input1, final double input2, final double expected) {
        NodeTest.propagate(graph, input1, input2);

        final Iterator<BackpropNeuron> outputNeurons = graph.getVertices("layer", "output", BackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        outputNeuron.setDeltaTrain(expected);
        graph.commit();

        final Iterator<BackpropNeuron> hiddenNeurons = graph.getVertices("layer", "hidden", BackpropNeuron.class).iterator();
        hiddenNeurons.next().backpropagate();
        hiddenNeurons.next().backpropagate();
        hiddenNeurons.next().backpropagate();
        hiddenNeurons.next().backpropagate();
        Assert.assertTrue(!hiddenNeurons.hasNext());
        graph.commit();

        final Iterator<BackpropNeuron> inputNeurons = graph.getVertices("layer", "input", BackpropNeuron.class).iterator();
        inputNeurons.next().backpropagate();
        inputNeurons.next().backpropagate();
        Assert.assertTrue(!inputNeurons.hasNext());
        graph.commit();

        final Iterator<BackpropNeuron> biasNeurons = graph.getVertices("layer", "bias", BackpropNeuron.class).iterator();
        biasNeurons.next().backpropagate();
        biasNeurons.next().backpropagate();
        biasNeurons.next().backpropagate();
        biasNeurons.next().backpropagate();
        biasNeurons.next().backpropagate();
        Assert.assertTrue(!biasNeurons.hasNext());
        graph.commit();
    }

    private static void printGraph(final FramedTransactionalGraph<?> graph) {
        final Iterator<BackpropNeuron> inputNeurons = graph.getVertices("layer", "input", BackpropNeuron.class).iterator();
        System.out.println("input signal: " + inputNeurons.next().getSignal());
        System.out.println("input signal: " + inputNeurons.next().getSignal());
        Assert.assertTrue(!inputNeurons.hasNext());

        final Iterator<BackpropNeuron> hiddenNeurons = graph.getVertices("layer", "hidden", BackpropNeuron.class).iterator();
        System.out.println("hiddenNeuron signal: " + hiddenNeurons.next().getSignal());
        System.out.println("hiddenNeuron signal: " + hiddenNeurons.next().getSignal());
        System.out.println("hiddenNeuron signal: " + hiddenNeurons.next().getSignal());
        System.out.println("hiddenNeuron signal: " + hiddenNeurons.next().getSignal());
        Assert.assertTrue(!hiddenNeurons.hasNext());

        final Iterator<BackpropNeuron> outputNeurons = graph.getVertices("layer", "output", BackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        System.out.println("outputNeuron signal: " + outputNeuron.getSignal());
    }

    private static double propagate(final FramedTransactionalGraph<?> graph, final double input1, final double input2) {
        final Iterator<BackpropNeuron> inputNeurons = graph.getVertices("layer", "input", BackpropNeuron.class).iterator();
        inputNeurons.next().setSignal(input1);
        inputNeurons.next().setSignal(input2);
        Assert.assertTrue(!inputNeurons.hasNext());
        graph.commit();

        final Iterator<BackpropNeuron> hiddenNeurons = graph.getVertices("layer", "hidden", BackpropNeuron.class).iterator();
        hiddenNeurons.next().tick();
        hiddenNeurons.next().tick();
        hiddenNeurons.next().tick();
        hiddenNeurons.next().tick();
        Assert.assertTrue(!hiddenNeurons.hasNext());
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

        return factory.create(graph);
    }
}
