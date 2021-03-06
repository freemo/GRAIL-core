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

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.typedgraph.*;

@TypeField("classType")
@TypeValue("God")
public interface ExceptionGod {
    @Property("name")
    String getName();

    @TypedAdjacency(label="")
    <N extends ExceptionGod> Iterable<? extends N> getNoLabel(Class<? extends N> type);

    @TypedAdjacency(label="Father", direction = Direction.IN)
    <N extends ExceptionGod> Iterable<? extends N> getSons();

    @TypedAdjacency(label="Father", direction = Direction.IN)
    <N extends ExceptionGod> Iterable<? extends N> getSons(Class<? extends N> type, String badStuff);

    @TypedAdjacency(label="Father", direction = Direction.IN)
    <N extends ExceptionGod> Iterable<? extends N> getSons(String badStuff);

    @TypedAdjacency(label="father", direction=Direction.IN)
    <N extends God> N addSon();

    @TypedAdjacency(label="father", direction=Direction.IN)
    <N extends God> N addSon(String badArg);

    @TypedAdjacency(label="father", direction=Direction.IN)
    <N extends God> N addSon(String badArg, String worseArg);

    @TypedAdjacency(label="Father", direction = Direction.IN)
    <N extends ExceptionGod> Iterable<? extends N> badSons(Class<? extends N> type);

    @TypedIncidence(label="")
    <N extends FatherEdge> Iterable<? extends N> getNoLabelEdges(Class<? extends N> type);

    @TypedIncidence(label="Father", direction = Direction.IN)
    <N extends FatherEdge> Iterable<? extends N> getSonEdges(Class<? extends N> type);

    @TypedIncidence(label="Father", direction = Direction.IN)
    <N extends FatherEdge> Iterable<? extends N> badSonEdges(Class<? extends N> type);
}
