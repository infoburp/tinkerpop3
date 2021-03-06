package com.tinkerpop.gremlin.process.graph.traversal.step.filter;


import com.tinkerpop.gremlin.process.Traversal;
import com.tinkerpop.gremlin.process.traversal.step.Reversible;
import com.tinkerpop.gremlin.process.traverser.TraverserRequirement;

import java.util.Collections;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class SimplePathStep<S> extends FilterStep<S> implements Reversible {

    public SimplePathStep(final Traversal.Admin traversal) {
        super(traversal);
        this.setPredicate(traverser -> traverser.path().isSimple());
    }

    @Override
    public Set<TraverserRequirement> getRequirements() {
        return Collections.singleton(TraverserRequirement.PATH);
    }
}
