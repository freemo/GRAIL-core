package com.syncleus.grail.neural.backprop;

import com.syncleus.grail.activation.*;
import com.syncleus.grail.graph.*;
import com.tinkerpop.frames.*;
import org.junit.*;
import java.util.*;

public class OrTest {
   @Test
    public void testOr() {
        final FramedTransactionalGraph<?> graph = BlankGraphFactory.makeTinkerGraph();

        final List<BackpropNeuron> newInputNeurons = new ArrayList<BackpropNeuron>(2);
        newInputNeurons.add(OrTest.createNeuron(graph, "input"));
        newInputNeurons.add(OrTest.createNeuron(graph, "input"));
        final BackpropNeuron newOutputNeuron = OrTest.createNeuron(graph, "output");

        //connect all hidden neurons to the output neuron
        for( BackpropNeuron inputNeuron : newInputNeurons ) {
            graph.addEdge(null, inputNeuron.asVertex(), newOutputNeuron.asVertex(), "targets", BackpropSynapse.class);//.asEdge().setProperty("type", "BackpropSynapse");
        }
        //create bias neuron for output neuron
        final BackpropNeuron biasNeuron = OrTest.createNeuron(graph, "bias");
        biasNeuron.setSignal(1.0);
        graph.addEdge(null, biasNeuron.asVertex(), newOutputNeuron.asVertex(), "targets", BackpropSynapse.class);//.asEdge().setProperty("type", "BackpropSynapse");
        graph.commit();

        for(int i = 0; i < 10000; i++) {
            OrTest.train(graph, -1.0, 1.0, 1.0);
            OrTest.train(graph, 1.0, -1.0, 1.0);
            OrTest.train(graph, 1.0, 1.0, 1.0);
            OrTest.train(graph, -1.0, -1.0, -1.0);
        }

        Assert.assertTrue("expected >0.0, got: " + OrTest.propagate(graph, 1.0, 1.0), OrTest.propagate(graph, 1.0, 1.0) > 0.0);
        Assert.assertTrue("expected <0.0, got: " + OrTest.propagate(graph, -1.0, -1.0), OrTest.propagate(graph, -1.0, -1.0) < 0.0);
        Assert.assertTrue("expected >0.0, got: " + OrTest.propagate(graph, 1.0, -1.0), OrTest.propagate(graph, 1.0, -1.0) > 0.0);
        Assert.assertTrue("expected >0.0, got: " + OrTest.propagate(graph, -1.0, 1.0), OrTest.propagate(graph, -1.0, 1.0) > 0.0);
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

    private static double propagate(final FramedTransactionalGraph<?> graph, final double input1, final double input2) {
        final Iterator<BackpropNeuron> inputNeurons = graph.getVertices("layer", "input", BackpropNeuron.class).iterator();
        inputNeurons.next().setSignal(input1);
        inputNeurons.next().setSignal(input2);
        Assert.assertTrue(!inputNeurons.hasNext());
        graph.commit();

        final Iterator<BackpropNeuron> outputNeurons = graph.getVertices("layer", "output", BackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        outputNeuron.propagate();
        graph.commit();
        return outputNeuron.getSignal();
    }

    private static BackpropNeuron createNeuron(final FramedGraph<?> graph, final String layer) {
        final BackpropNeuron neuron = graph.addVertex(null, BackpropNeuron.class);
        neuron.asVertex().setProperty("layer", layer);
        return neuron;
    }
}
