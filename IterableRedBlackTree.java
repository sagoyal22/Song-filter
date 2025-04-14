import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it stores in
 * sorted, ascending order.
 */
public class IterableRedBlackTree<T extends Comparable<T>> extends RedBlackTree<T>
    implements IterableSortedCollection<T> {

  private Comparable<T> iteratorMin = null;
  private Comparable<T> iteratorMax = null;

  /**
   * Allows setting the start (minimum) value of the iterator. When this method is called, every
   * iterator created after it will use the minimum set by this method until this method is called
   * again to set a new minimum value.
   * 
   * @param min the minimum for iterators created for this tree, or null for no minimum
   */
  public void setIteratorMin(Comparable<T> min) {
    this.iteratorMin = min;
  }

  /**
   * Allows setting the stop (maximum) value of the iterator. When this method is called, every
   * iterator created after it will use the maximum set by this method until this method is called
   * again to set a new maximum value.
   * 
   * @param min the maximum for iterators created for this tree, or null for no maximum
   */
  public void setIteratorMax(Comparable<T> max) {
    this.iteratorMax = max;
  }

  /**
   * Returns an iterator over the values stored in this tree. The iterator uses the start (minimum)
   * value set by a previous call to setIteratorMin, and the stop (maximum) value set by a previous
   * call to setIteratorMax. If setIteratorMin has not been called before, or if it was called with
   * a null argument, the iterator uses no minimum value and starts with the lowest value that
   * exists in the tree. If setIteratorMax has not been called before, or if it was called with a
   * null argument, the iterator uses no maximum value and finishes with the highest value that
   * exists in the tree.
   */
  public Iterator<T> iterator() {
    // create iterator
    return new RBTIterator<>(this.root, iteratorMin, iteratorMax);
  }

  /**
   * Nested class for Iterator objects created for this tree and returned by the iterator method.
   * This iterator follows an in-order traversal of the tree and returns the values in sorted,
   * ascending order.
   */
  protected static class RBTIterator<R> implements Iterator<R> {

    // stores the start point (minimum) for the iterator
    Comparable<R> min = null;
    // stores the stop point (maximum) for the iterator
    Comparable<R> max = null;
    // stores the stack that keeps track of the inorder traversal
    Stack<BinaryTreeNode<R>> stack = null;

    /**
     * Constructor for a new iterator if the tree with root as its root node, and min as the start
     * (minimum) value (or null if no start value) and max as the stop (maximum) value (or null if
     * no stop value) of the new iterator.
     * 
     * @param root root node of the tree to traverse
     * @param min  the minimum value that the iterator will return
     * @param max  the maximum value that the iterator will return
     */
    public RBTIterator(BinaryTreeNode<R> root, Comparable<R> min, Comparable<R> max) {
      this.min = min;
      this.max = max;
      this.stack = new Stack<>();
      buildStackHelper(root); // root = node for first iteration of build helper
    }


    /**
     * Helper method for initializing and updating the stack. This method both - finds the next data
     * value stored in the tree (or subtree) that is between start(minimum) and stop(maximum) point
     * (including start and stop points themselves), and - builds up the stack of ancestor nodes
     * that contain values between start(minimum) and stop(maximum) values (including start and stop
     * values themselves) so that those nodes can be visited in the future.
     * 
     * @param node the root node of the subtree to process
     */
    private void buildStackHelper(BinaryTreeNode<R> node) {
      // base case
      if (node == null) {
        return;
      }
      // If node's value is smaller than min, search the right subtree
      if (min != null && ((Comparable<? super R>) min).compareTo(node.getData()) > 0) {
        buildStackHelper(node.childRight());
      } else {
        if (min == null || ((Comparable<? super R>) min).compareTo(node.getData()) <= 0) {
          stack.push(node);
        }
        buildStackHelper(node.childLeft());
      }
    }

    /**
     * Returns true if the iterator has another value to return, and false otherwise.
     */
    public boolean hasNext() {
      // If the stack is empty, there are no more nodes to visit
      if (stack.isEmpty()) {
        return false;
      }

      BinaryTreeNode<R> nextNode = stack.peek();

      if (max == null || ((Comparable<? super R>) nextNode.getData()).compareTo((R) max) <= 0) {
        return true;
      }

      return false;
    }

    /**
     * Returns the next value of the iterator.
     * 
     * @throws NoSuchElementException if the iterator has no more values to return
     */
    @Override
    public R next() {
      // If there are no more elements, throw an exception
      if (!hasNext()) {
        throw new NoSuchElementException("No more elements in the iterator.");
      }

      BinaryTreeNode<R> nextNode;

      // Continue popping nodes until we find one within the valid range
      do {
        nextNode = stack.pop();

        // If this node has a right child, process it to find the next valid value
        if (nextNode.childRight() != null) {
          buildStackHelper(nextNode.childRight());
        }

      } while (min != null && ((Comparable<? super R>) nextNode.getData()).compareTo((R) min) < 0);

      // Return the valid data
      return nextNode.getData();
    }
  }

  /**
   * Test method to check integer iteration with start and stop constraints.
   */
  @Test
  public void testIntegerIterationWithBounds() {
    // Create a new IterableRedBlackTree instance
    IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();

    // Insert multiple nodes to create a more complex tree
    tree.insert(5);
    tree.insert(10);
    tree.insert(15);
    tree.insert(18);
    tree.insert(20);
    tree.insert(25);
    tree.insert(30);
    tree.insert(35);
    tree.insert(40);

    // Set iterator bounds: min = 15, max = 30
    tree.setIteratorMin(15);
    tree.setIteratorMax(30);

    // Create an iterator with these bounds
    Iterator<Integer> iterator = tree.iterator();

    // Ensure the iterator starts correctly
    assertTrue(iterator.hasNext());

    // Check that all expected values within the range are returned
    assertEquals(15, iterator.next());
    assertEquals(18, iterator.next());
    assertEquals(20, iterator.next());
    assertEquals(25, iterator.next());
    assertEquals(30, iterator.next());

    // Ensure no more values are returned beyond max (30)
    assertFalse(iterator.hasNext());
  }

  /**
   * Test method to check string iteration with only a specified start point (min), but no stop
   * point (max).
   */
  // string input and only min
  @Test
  public void testStringIterationWithOnlyStartPoint() {
    // Create a new IterableRedBlackTree instance for Strings
    IterableRedBlackTree<String> tree = new IterableRedBlackTree<>();

    // Insert multiple string values, including duplicates
    tree.insert("africa");
    tree.insert("boston");
    tree.insert("chicago");
    tree.insert("dalas");
    tree.insert("finland");
    tree.insert("georgia");

    // Set only the min value (iterator should start from "cherry")
    tree.setIteratorMin("chicago");

    // Create an iterator
    Iterator<String> iterator = tree.iterator();

    // Ensure the iterator starts at "cherry" and iterates correctly
    assertTrue(iterator.hasNext());
    assertEquals("chicago", iterator.next());
    assertEquals("dalas", iterator.next());
    assertEquals("finland", iterator.next());
    assertEquals("georgia", iterator.next());

    // Ensure no more values remain in the iterator
    assertFalse(iterator.hasNext());
  }

  /**
   * Test method to check integer iteration with duplicate values and only a specified stop point
   * (max), but no start point (min).
   */
  @Test
  public void testIntegerIterationWithDuplicatesOnlyMaxSpecified() {
    // Create a new IterableRedBlackTree instance for Integers
    IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();

    // Insert multiple integer values, including duplicates
    tree.insert(5);
    tree.insert(10);
    tree.insert(10); // Duplicate
    tree.insert(15);
    tree.insert(20);
    tree.insert(25);
    tree.insert(30);

    // Set only the max value (iterator should stop at 20)
    tree.setIteratorMax(20);

    // Create an iterator
    Iterator<Integer> iterator = tree.iterator();

    // Ensure the iterator starts correctly and stops at 20
    assertTrue(iterator.hasNext());
    assertEquals(5, iterator.next());
    assertEquals(10, iterator.next());
    assertEquals(10, iterator.next());
    assertEquals(15, iterator.next());
    assertEquals(20, iterator.next());

    // Ensure no more values remain in the iterator beyond max (20)
    assertFalse(iterator.hasNext());
  }
}
