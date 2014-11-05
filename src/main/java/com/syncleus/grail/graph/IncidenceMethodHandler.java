package com.syncleus.grail.graph;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.annotations.*;
import com.tinkerpop.frames.modules.MethodHandler;

import java.lang.reflect.Method;

public class IncidenceMethodHandler implements MethodHandler<Incidence> {
    private final IncidenceAnnotationHandler rootHandler = new IncidenceAnnotationHandler();

    @Override
    public Class<Incidence> getAnnotationType() {
        return this.rootHandler.getAnnotationType();
    }

    @Override
    public Object processElement(final Object frame, final Method method, final Object[] arguments, final Incidence annotation, final FramedGraph<?> framedGraph, final Element element) {
        System.out.println("Incidence being handled!");
        return this.rootHandler.processElement(annotation, method, arguments, framedGraph, element, annotation.direction());
    }
}
