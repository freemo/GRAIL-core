package com.syncleus.titangraph.example.titangods;

import com.syncleus.grail.graph.TypedAdjacency;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.annotations.gremlin.GremlinGroovy;
import com.tinkerpop.frames.modules.javahandler.*;
import com.tinkerpop.frames.modules.typedgraph.*;

@TypeField("classType")
@TypeValue("God")
public interface God {
    @Property("name")
    public String getName();

    @Property("age")
    public Integer getAge();

    @Property("type")
    public String getType();

    @Adjacency(label="father")
    public God getFather();

    @GremlinGroovy("it.in('father')")
    public God getSon();

    @TypedAdjacency(label="father", direction= Direction.IN)
    public Iterable<? extends God> getSons(Class<? extends God> type);

    @Adjacency(label="lives")
    public Location getHome();

    @JavaHandler
    public boolean isAgeEven();

    public abstract class Impl implements JavaHandlerContext<Vertex>, God {

        public boolean isAgeEven() {
            return this.getAge() % 2 == 0;
        }
    }
}
