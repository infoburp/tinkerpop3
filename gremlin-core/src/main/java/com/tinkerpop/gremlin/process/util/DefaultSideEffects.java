package com.tinkerpop.gremlin.process.util;

import com.tinkerpop.gremlin.process.Traversal;
import com.tinkerpop.gremlin.structure.Property;
import com.tinkerpop.gremlin.structure.Vertex;
import com.tinkerpop.gremlin.structure.util.StringFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class DefaultSideEffects implements Traversal.SideEffects {

    private Map<String, Object> sideEffectMap = new HashMap<>();

    public DefaultSideEffects() {

    }

    public DefaultSideEffects(final Vertex localVertex) {
        this.setLocalVertex(localVertex);
    }

    @Override
    public boolean exists(final String key) {
        return this.sideEffectMap.containsKey(key);
    }

    @Override
    public <V> void set(final String key, final V value) {
        SideEffectHelper.validateSideEffect(key, value);
        this.sideEffectMap.put(key, value);
    }

    @Override
    public <V> V get(final String key) throws IllegalArgumentException {
        final V value = (V) this.sideEffectMap.get(key);
        if (null == value)
            throw Traversal.SideEffects.Exceptions.sideEffectDoesNotExist(key);
        return value;
    }

    @Override
    public <V> V getOrCreate(final String key, final Supplier<V> orCreate) {
        if (this.sideEffectMap.containsKey(key))
            return (V) this.sideEffectMap.get(key);
        else {
            final V v = orCreate.get();
            this.sideEffectMap.put(key, v);
            return v;
        }
    }

    @Override
    public void remove(final String key) {
        this.sideEffectMap.remove(key);
    }

    @Override
    public Set<String> keys() {
        return this.sideEffectMap.keySet();
    }

    @Override
    public String toString() {
        return StringFactory.traversalSideEffectsString(this);
    }

    @Override
    public void setLocalVertex(final Vertex vertex) {
        final Property<Map<String, Object>> property = vertex.property(DISTRIBUTED_SIDE_EFFECTS_VERTEX_PROPERTY_KEY);
        if (property.isPresent()) {
            this.sideEffectMap = property.value();
        } else {
            this.sideEffectMap = new HashMap<>();
            vertex.property(DISTRIBUTED_SIDE_EFFECTS_VERTEX_PROPERTY_KEY, this.sideEffectMap);
        }
    }
}
