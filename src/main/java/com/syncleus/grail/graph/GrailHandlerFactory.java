package com.syncleus.grail.graph;

import com.tinkerpop.frames.modules.javahandler.JavaHandlerFactory;

public class GrailHandlerFactory implements JavaHandlerFactory {
    @Override
    public <T> T create(final Class<T> handlerClass) throws InstantiationException, IllegalAccessException {
        return handlerClass.newInstance();
    }
}
