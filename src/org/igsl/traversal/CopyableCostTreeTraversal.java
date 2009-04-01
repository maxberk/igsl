package org.igsl.traversal;

/**
 * Extension for CostTreeTraversal with copy functionality
 *
 * @param <T> - type of node
 * @param <C> - type of cost
 * @see CostTreeTraversal
 * @see Copyable
 */

public interface CopyableCostTreeTraversal<T,C>
	extends CostTreeTraversal<T,C>, Copyable<CostTreeTraversal<T,C>>
{
}
