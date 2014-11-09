package com.syncleus.grail.neural.backprop;

import com.syncleus.grail.neural.activation.*;
import com.syncleus.grail.graph.BlankGraphFactory;
import com.syncleus.grail.graph.action.*;
import com.tinkerpop.frames.*;
import org.junit.*;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;

public class ActionTriggerXor3InputTest {
    @Test
    public void testXor() {
        final FramedTransactionalGraph<?> graph = BlankGraphFactory.makeTinkerGraph();

        //
        //Construct the Neural Graph
        //

        final List<BackpropNeuron> newInputNeurons = new ArrayList<BackpropNeuron>(2);
        newInputNeurons.add(ActionTriggerXor3InputTest.createNeuron(graph, "input"));
        newInputNeurons.add(ActionTriggerXor3InputTest.createNeuron(graph, "input"));
        newInputNeurons.add(ActionTriggerXor3InputTest.createNeuron(graph, "input"));
        final List<BackpropNeuron> newHiddenNeurons = new ArrayList<BackpropNeuron>(4);
        newHiddenNeurons.add(ActionTriggerXor3InputTest.createNeuron(graph, "hidden"));
        newHiddenNeurons.add(ActionTriggerXor3InputTest.createNeuron(graph, "hidden"));
        newHiddenNeurons.add(ActionTriggerXor3InputTest.createNeuron(graph, "hidden"));
        final BackpropNeuron newOutputNeuron = ActionTriggerXor3InputTest.createNeuron(graph, "output");

        //connect all input neurons to hidden neurons
        for( final BackpropNeuron inputNeuron : newInputNeurons ) {
            for( final BackpropNeuron hiddenNeuron : newHiddenNeurons ) {
                graph.addEdge(null, inputNeuron.asVertex(), hiddenNeuron.asVertex(), "signals", BackpropSynapse.class);
            }
        }
        //connect all hidden neurons to the output neuron
        for( final BackpropNeuron hiddenNeuron : newHiddenNeurons ) {
            graph.addEdge(null, hiddenNeuron.asVertex(), newOutputNeuron.asVertex(), "signals", BackpropSynapse.class);

            //create bias neuron
            final BackpropNeuron biasNeuron = ActionTriggerXor3InputTest.createNeuron(graph, "bias");
            biasNeuron.setSignal(1.0);
            graph.addEdge(null, biasNeuron.asVertex(), hiddenNeuron.asVertex(), "signals", BackpropSynapse.class);
        }
        //create bias neuron for output neuron
        final BackpropNeuron biasNeuron = ActionTriggerXor3InputTest.createNeuron(graph, "bias");
        biasNeuron.setSignal(1.0);
        graph.addEdge(null, biasNeuron.asVertex(), newOutputNeuron.asVertex(), "signals", BackpropSynapse.class);

        //
        //Construct the Action Triggers for the neural Graph
        //

        //First lets handle the output layer for propagation
        final PrioritySerialTrigger propagateOutputTrigger = ActionTriggerXor3InputTest.createPrioritySerialTrigger(graph);
        //connect it to the output neuron with a priority of 0 (highest priority)
        final PrioritySerialTriggerEdge outputTriggerEdge = graph.addEdge(null, propagateOutputTrigger.asVertex(), newOutputNeuron.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
        outputTriggerEdge.setTriggerPriority(0);
        outputTriggerEdge.setTriggerAction("propagate");

        //now lets handle the hidden layer for propagation
        final PrioritySerialTrigger propagateHiddenTrigger = ActionTriggerXor3InputTest.createPrioritySerialTrigger(graph);
        propagateHiddenTrigger.asVertex().setProperty("triggerPointer", "propagate");
        //connect it to each of the hidden neurons with a priority of 0 (highest priority)
        for( final BackpropNeuron hiddenNeuron : newHiddenNeurons ) {
            final PrioritySerialTriggerEdge newEdge = graph.addEdge(null, propagateHiddenTrigger.asVertex(), hiddenNeuron.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
            newEdge.setTriggerPriority(0);
            newEdge.setTriggerAction("propagate");
        }

        //chain the prop[agation of the hidden layer to the propagation of the output layer, but make sure it has less of a priority than the other triggers
        final PrioritySerialTriggerEdge chainTriggerPropagateEdge = graph.addEdge(null, propagateHiddenTrigger.asVertex(), propagateOutputTrigger.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
        chainTriggerPropagateEdge.setTriggerPriority(1000);
        chainTriggerPropagateEdge.setTriggerAction("actionTrigger");

        //next lets handle the input layer for back propagation
        final PrioritySerialTrigger backpropInputTrigger = ActionTriggerXor3InputTest.createPrioritySerialTrigger(graph);
        //connect it to each of the input neurons
        for( final BackpropNeuron inputNeuron : newInputNeurons ) {
            final PrioritySerialTriggerEdge newEdge = graph.addEdge(null, backpropInputTrigger.asVertex(), inputNeuron.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
            newEdge.setTriggerPriority(0);
            newEdge.setTriggerAction("backpropagate");
        }

        //create backpropagation trigger for the hidden layer
        final PrioritySerialTrigger backpropHiddenTrigger = ActionTriggerXor3InputTest.createPrioritySerialTrigger(graph);
        backpropHiddenTrigger.asVertex().setProperty("triggerPointer", "backpropagate");
        //connect it to each of the hidden neurons with a priority of 0 (highest priority)
        for( final BackpropNeuron hiddenNeuron : newHiddenNeurons ) {
            final PrioritySerialTriggerEdge newEdge = graph.addEdge(null, backpropHiddenTrigger.asVertex(), hiddenNeuron.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
            newEdge.setTriggerPriority(0);
            newEdge.setTriggerAction("backpropagate");
        }

        //finally chain the hidden layers back propagation to the input layers trigger
        final PrioritySerialTriggerEdge chainTriggerBackpropEdge = graph.addEdge(null, backpropHiddenTrigger.asVertex(), backpropInputTrigger.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
        chainTriggerBackpropEdge.setTriggerPriority(1000);
        chainTriggerBackpropEdge.setTriggerAction("actionTrigger");

        //commit everything
        graph.commit();

        //
        // Graph is constructed, just need to train and test our network now.
        //

        for(int i = 0; i < 1000; i++) {
            ActionTriggerXor3InputTest.train(graph, 1.0, 1.0, 1.0, -1.0);
            ActionTriggerXor3InputTest.train(graph, -1.0, 1.0, 1.0, -1.0);
            ActionTriggerXor3InputTest.train(graph, 1.0, -1.0, 1.0, -1.0);
            ActionTriggerXor3InputTest.train(graph, 1.0, 1.0, -1.0, -1.0);
            ActionTriggerXor3InputTest.train(graph, -1.0, -1.0, 1.0, 1.0);
            ActionTriggerXor3InputTest.train(graph, -1.0, 1.0, -1.0, 1.0);
            ActionTriggerXor3InputTest.train(graph, 1.0, -1.0, -1.0, 1.0);
            ActionTriggerXor3InputTest.train(graph, -1.0, -1.0, -1.0, -1.0);
        }
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, 1.0, 1.0, 1.0) < 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, -1.0, 1.0, 1.0) < 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, 1.0, -1.0, 1.0) < 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, 1.0, 1.0, -1.0) < 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, -1.0, -1.0, 1.0) > 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, -1.0, 1.0, -1.0) > 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, 1.0, -1.0, -1.0) > 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, -1.0, -1.0, -1.0) < 0.0);
    }

    private static final ActivationFunction activationFunction = new SineActivationFunction();

    private static void train(final FramedTransactionalGraph<?> graph, final double input1, final double input2, final double input3, final double expected) {
        ActionTriggerXor3InputTest.propagate(graph, input1, input2, input3);

        final Iterator<BackpropNeuron> outputNeurons = graph.getVertices("layer", "output", BackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        outputNeuron.setDeltaTrain((expected - outputNeuron.getSignal()) * activationFunction.activateDerivative(outputNeuron.getActivity()) );
        graph.commit();

        final Iterator<PrioritySerialTrigger> backpropTriggers = graph.getVertices("triggerPointer", "backpropagate", PrioritySerialTrigger.class).iterator();
        final PrioritySerialTrigger backpropTrigger = backpropTriggers.next();
        Assert.assertTrue(!backpropTriggers.hasNext());
        backpropTrigger.trigger();
        /*
        final Iterator<BackpropNeuron> hiddenNeurons = graph.getVertices("layer", "hidden", BackpropNeuron.class).iterator();
        hiddenNeurons.next().backpropagate();
        hiddenNeurons.next().backpropagate();
        hiddenNeurons.next().backpropagate();
        Assert.assertTrue(!hiddenNeurons.hasNext());
        graph.commit();

        final Iterator<BackpropNeuron> inputNeurons = graph.getVertices("layer", "input", BackpropNeuron.class).iterator();
        inputNeurons.next().backpropagate();
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
        */
        graph.commit();
    }

    private static double propagate(final FramedTransactionalGraph<?> graph, final double input1, final double input2, final double input3) {
        final Iterator<BackpropNeuron> inputNeurons = graph.getVertices("layer", "input", BackpropNeuron.class).iterator();
        inputNeurons.next().setSignal(input1);
        inputNeurons.next().setSignal(input2);
        inputNeurons.next().setSignal(input3);
        Assert.assertTrue(!inputNeurons.hasNext());
        graph.commit();

        final Iterator<PrioritySerialTrigger> propagateTriggers = graph.getVertices("triggerPointer", "propagate", PrioritySerialTrigger.class).iterator();
        final PrioritySerialTrigger propagateTrigger = propagateTriggers.next();
        Assert.assertTrue(!propagateTriggers.hasNext());
        try {
            propagateTrigger.trigger();
        }
        catch(final UndeclaredThrowableException caught ) {
            caught.getUndeclaredThrowable().printStackTrace();
            throw caught;
        }
        graph.commit();

        final Iterator<BackpropNeuron> outputNeurons = graph.getVertices("layer", "output", BackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        return outputNeuron.getSignal();

        /*
        final Iterator<BackpropNeuron> hiddenNeurons = graph.getVertices("layer", "hidden", BackpropNeuron.class).iterator();
        hiddenNeurons.next().propagate();
        hiddenNeurons.next().propagate();
        hiddenNeurons.next().propagate();
        Assert.assertTrue(!hiddenNeurons.hasNext());
        graph.commit();

        final Iterator<BackpropNeuron> outputNeurons = graph.getVertices("layer", "output", BackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        outputNeuron.propagate();
        graph.commit();
        return outputNeuron.getSignal();
        */
    }

    private static BackpropNeuron createNeuron(final FramedGraph<?> graph, final String layer) {
        final BackpropNeuron neuron = graph.addVertex(null, BackpropNeuron.class);
        neuron.asVertex().setProperty("layer", layer);
        return neuron;
    }

    private static PrioritySerialTrigger createPrioritySerialTrigger(final FramedGraph<?> graph) {
        return graph.addVertex(null, PrioritySerialTrigger.class);
    }
}