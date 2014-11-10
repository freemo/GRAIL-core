/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.grail.graph;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.frames.FramedGraph;
import org.junit.Test;

import java.lang.reflect.Method;

public class TypedIncidenceMethodHandlerTest {
    private static final TypedIncidenceMethodHandler HANDLER = new TypedIncidenceMethodHandler(null);
    private static final Method GET_SON_EDGE_METHOD;
    private static final TypedIncidence TYPED_ANNOTATION;
    private static final Object MOCK_FRAME = new Object();
    private static final Element MOCK_ELEMENT = new MockElement();
    private static final Element MOCK_VERTEX = new MockVertex();

    static {
        try {
            GET_SON_EDGE_METHOD = God.class.getMethod("getSonEdge", Class.class);
        }
        catch( final NoSuchMethodException caught ) {
            throw new IllegalStateException(caught);
        }

        TYPED_ANNOTATION = GET_SON_EDGE_METHOD.getAnnotation(TypedIncidence.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoVertex() {
        final FramedGraph framedGraph = BlankGraphFactory.makeTinkerGraph();

        HANDLER.processElement(MOCK_FRAME, GET_SON_EDGE_METHOD, new Object[]{God.class}, TYPED_ANNOTATION, framedGraph, MOCK_ELEMENT);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullArguments() {
        final FramedGraph framedGraph = BlankGraphFactory.makeTinkerGraph();

        HANDLER.processElement(MOCK_FRAME, GET_SON_EDGE_METHOD, null, TYPED_ANNOTATION, framedGraph, MOCK_VERTEX);
    }
}