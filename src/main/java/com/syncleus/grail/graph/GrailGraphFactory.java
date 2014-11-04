package com.syncleus.grail.graph;

import com.syncleus.grail.neural.Synapse;
import com.syncleus.grail.neural.backprop.*;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule;
import com.tinkerpop.frames.modules.javahandler.*;
import com.tinkerpop.frames.modules.typedgraph.TypedGraphModuleBuilder;

import java.util.*;

public class GrailGraphFactory extends FramedGraphFactory {
    public GrailGraphFactory() {
        super(GrailGraphFactory.constructModules(Collections.<Module>emptySet()));
    }

    public GrailGraphFactory(final Collection<? extends Module> modules) {
        super(GrailGraphFactory.constructModules(modules));
    }

    public GrailGraphFactory(final Collection<? extends Module> modules, final Collection<? extends Class<?>> types) {
        super(GrailGraphFactory.constructModules(modules, types));
    }

    protected static Module constructTypedModule(final Collection<? extends Class<?>> additionalClasses) {
        final TypedGraphModuleBuilder typedModuleBuilder = new TypedGraphModuleBuilder()
                                           .withClass(Synapse.class)
                                           .withClass(BackpropSynapse.class)
                                           .withClass(BackpropNeuron.class);

        for(final Class<?> additionalClass : additionalClasses)
            typedModuleBuilder.withClass(additionalClass);
        return typedModuleBuilder.build();
    }

    private static Module[] constructModules(final Collection<? extends Module> modules) {
        return GrailGraphFactory.combineModules(modules, new GremlinGroovyModule(), GrailGraphFactory.constructHandlerModule());
    }

    private static Module[] constructModules(final Collection<? extends Module> modules, final Collection<? extends Class<?>> types) {
        return GrailGraphFactory.combineModules(modules, GrailGraphFactory.constructTypedModule(types), new GremlinGroovyModule(), GrailGraphFactory.constructHandlerModule());
    }

    private static JavaHandlerModule constructHandlerModule() {
        final JavaHandlerModule module = new JavaHandlerModule();
        module.withFactory(new GrailHandlerFactory());
        return module;
    }

    private static Module[] combineModules(final Collection<? extends Module> modules, Module... additionalModules) {
        final Module[] allModules = Arrays.copyOf(additionalModules, modules.size() + additionalModules.length);
        int moduleIndex = additionalModules.length;
        for(final Module module : modules ) {
            allModules[moduleIndex] = module;
            moduleIndex++;
        }
        return allModules;
    }


}
