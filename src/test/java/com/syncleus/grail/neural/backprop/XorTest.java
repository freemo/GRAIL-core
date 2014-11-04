package com.syncleus.grail.neural.backprop;

import com.syncleus.grail.activation.*;
import com.syncleus.grail.graph.*;
import com.tinkerpop.frames.*;
import org.junit.*;
import java.util.*;

public class XorTest {
    @Test
    public void testXor() {
        final FramedTransactionalGraph<?> graph = BlankGraphFactory.makeTinkerGraph();

        final List<BackpropNeuron> newInputNeurons = new ArrayList<BackpropNeuron>(2);
        newInputNeurons.add(XorTest.createNeuron(graph, "input"));
        newInputNeurons.add(XorTest.createNeuron(graph, "input"));
        final List<BackpropNeuron> newHiddenNeurons = new ArrayList<BackpropNeuron>(4);
        newHiddenNeurons.add(XorTest.createNeuron(graph, "hidden"));
        newHiddenNeurons.add(XorTest.createNeuron(graph, "hidden"));
        newHiddenNeurons.add(XorTest.createNeuron(graph, "hidden"));
        final BackpropNeuron newOutputNeuron = XorTest.createNeuron(graph, "output");

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
            final BackpropNeuron biasNeuron = XorTest.createNeuron(graph, "bias");
            biasNeuron.setSignal(1.0);
            graph.addEdge(null, biasNeuron.asVertex(), hiddenNeuron.asVertex(), "targets", BackpropSynapse.class);
        }
        //create bias neuron for output neuron
        final BackpropNeuron biasNeuron = XorTest.createNeuron(graph, "bias");
        biasNeuron.setSignal(1.0);
        graph.addEdge(null, biasNeuron.asVertex(), newOutputNeuron.asVertex(), "targets", BackpropSynapse.class);
        graph.commit();

        for(int i = 0; i < 10000; i++) {
            XorTest.train(graph, -1.0, 1.0, 1.0);
            XorTest.train(graph, 1.0, -1.0, 1.0);
            XorTest.train(graph, 1.0, 1.0, -1.0);
            XorTest.train(graph, -1.0, -1.0, -1.0);
        }
        Assert.assertTrue(XorTest.propagate(graph, 1.0, 1.0) < 0.0);
        Assert.assertTrue(XorTest.propagate(graph, -1.0, -1.0) < 0.0);
        Assert.assertTrue(XorTest.propagate(graph, 1.0, -1.0) > 0.0);
        Assert.assertTrue(XorTest.propagate(graph, -1.0, 1.0) > 0.0);
    }

    private static final ActivationFunction activationFunction = new SineActivationFunction();

    private static void train(final FramedTransactionalGraph<?> graph, final double input1, final double input2, final double expected) {
        XorTest.propagate(graph, input1, input2);

        final Iterator<BackpropNeuron> outputNeurons = graph.getVertices("layer", "output", BackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        outputNeuron.setDeltaTrain((expected - outputNeuron.getSignal()) * activationFunction.activateDerivative(outputNeuron.getActivity()) );
        graph.commit();

        final Iterator<BackpropNeuron> hiddenNeurons = graph.getVertices("layer", "hidden", BackpropNeuron.class).iterator();
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
        Assert.assertTrue(!biasNeurons.hasNext());
        graph.commit();
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
}