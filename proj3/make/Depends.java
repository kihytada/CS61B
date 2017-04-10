package make;

import graph.LabeledGraph;

/** A directed, labeled subtype of Graph that describes dependencies between
 *  targets in a Makefile.
 *  @author Leslie Yang
 */
class Depends extends LabeledGraph<Rule, Void> {
    /** An empty dependency graph. */
    Depends() {
        super(new graph.DirectedGraph());
    }
}
