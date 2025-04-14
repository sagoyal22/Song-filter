import java.util.ArrayList;
import java.util.List;

/**
 * A generic Binary Search Tree (BST) implementation.
 * 
 * @param <T> The type of elements stored in the BST. Must be comparable.
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {

  protected BinaryTreeNode<T> root;
  private int size;

  /**
   * Constructs an empty Binary Search Tree.
   */
  public BinarySearchTree() {
    this.root = null;
  }

  /**
   * Inserts a new element into the BST.
   * 
   * @param data the value to insert
   */
  @Override
  public void insert(T data) throws NullPointerException {
    if (data == null) {
      throw new NullPointerException("Cannot insert null value.");
    }

    BinaryTreeNode<T> newNode = new BinaryTreeNode<>(data);
    if (root == null) {
      root = newNode;
    } else {
      insertHelper(newNode, root);
    }
  }

  /**
   * Helper method to recursively insert a node into the tree.
   * 
   * @param newNode the node to insert
   * @param subtree the current subtree being considered
   */
  protected void insertHelper(BinaryTreeNode<T> newNode, BinaryTreeNode<T> subtree) {
    // negetive if new node is less than subtree, hence if subtree is bigger
    if (newNode.data.compareTo(subtree.data) <= 0) {
      if (subtree.childLeft() == null) {
        // if new node is smaller than we need to look at the left tree
        subtree.setChildLeft(newNode); // Set as left child
        newNode.setParent(subtree); // Set parent\
      } else {
        insertHelper(newNode, subtree.childLeft());
      }
    } else {
      if (subtree.childRight() == null) {
        subtree.setChildRight(newNode);
        newNode.setParent(subtree);
      } else {
        insertHelper(newNode, subtree.childRight());
      }
    }
  }

  /**
   * Checks if a given element exists in the BST.
   * 
   * @param data the value to search for
   * @return true if the value exists, false otherwise
   */
  @Override
  public boolean contains(Comparable<T> data) {
    if (data == null) {
      throw new NullPointerException("Cannot search for null value.");
    }
    return containsHelper(data, root);
  }

  /**
   * Helper method to recursively check if the tree contains the data.
   * 
   * @param data    the value to search for
   * @param subtree the current subtree being considered
   * @return true if data is found, false otherwise
   */

  protected boolean containsHelper(Comparable<T> data, BinaryTreeNode<T> subtree) {
    if (subtree == null) {
      return false;
    }
    int comparison = data.compareTo(subtree.data);
    // data matches
    if (comparison == 0) {
      return true;
    }
    if (comparison < 0) {
      return containsHelper(data, subtree.childLeft());
    } else {
      return containsHelper(data, subtree.childRight());
    }
  }

  /**
   * Returns the number of elements in the BST.
   * 
   * @return the size of the tree
   */
  @Override
  public int size() {
    if (root == null) {
      return 0;
    }
    return calculateSize(root);
  }

  /**
   * Helper method to calculate the size of the tree by traversing all nodes.
   * 
   * @param node the root of the subtree
   * @return the number of nodes in the subtree
   */
  private int calculateSize(BinaryTreeNode<T> node) {
    // Base case: if the node is null, it contributes 0 to the size
    if (node == null) {
      return 0;
    }
    // Recursively count the size of left and right subtrees, plus 1 for the current node
    return 1 + calculateSize(node.childLeft()) + calculateSize(node.childRight());
  }

  /**
   * Checks if the tree is empty.
   * 
   * @return true if the tree has no elements, false otherwise.
   */
  @Override
  public boolean isEmpty() {
    if (root == null) {
      return true;
    }
    return false;
    // Return true if size is 0, otherwise false
  }

  /**
   * Removes all elements from the BST, making it empty.
   */
  @Override
  public void clear() {
    root = null; // Set the root to null, which removes all nodes
    size = 0; // Reset the size to 0, indicating the tree is empty
  }

  // testers;
  /**
   * Tests insertion to see if the node is inserted in the right place with different types of trees
   * and values
   * 
   * @return true if all tests pass, false otherwise.
   */

  public static boolean test1() {
    // Test 1: Insert values in increasing order - creating a right-skewed tree
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    bst.insert(1);
    bst.insert(2);
    bst.insert(3);
    bst.insert(4);
    bst.insert(5);

    String levelOrder = bst.root.toLevelOrderString();
    String inOrder = bst.root.toInOrderString();

    if (!levelOrder.equals("[ 1, 2, 3, 4, 5 ]")) {
      return false;
    }
    if (!inOrder.equals("[ 1, 2, 3, 4, 5 ]")) {
      return false;
    }


    // Test 2: Insert values in decreasing order - creating a left-skewed tree
    bst = new BinarySearchTree<>();
    bst.insert(5);
    bst.insert(4);
    bst.insert(3);
    bst.insert(2);
    bst.insert(1);

    levelOrder = bst.root.toLevelOrderString();
    inOrder = bst.root.toInOrderString();

    if (!levelOrder.equals("[ 5, 4, 3, 2, 1 ]")) {
      return false;
    }
    if (!inOrder.equals("[ 1, 2, 3, 4, 5 ]")) {
      return false;
    }

    // Test 3: Insert in random order - creating a balanced tree
    bst = new BinarySearchTree<>();
    bst.insert(10);
    bst.insert(5);
    bst.insert(15);
    bst.insert(3);
    bst.insert(7);
    bst.insert(12);
    bst.insert(18);

    levelOrder = bst.root.toLevelOrderString();
    inOrder = bst.root.toInOrderString();
    if (!levelOrder.equals("[ 10, 5, 15, 3, 7, 12, 18 ]")) {
      return false;
    }
    if (!inOrder.equals("[ 3, 5, 7, 10, 12, 15, 18 ]")) {
      return false;
    }


    // Test 4: Tree holding strings
    BinarySearchTree<String> stringBst = new BinarySearchTree<>();
    stringBst.insert("delta");
    stringBst.insert("alpha");
    stringBst.insert("charlie");
    stringBst.insert("bravo");
    stringBst.insert("echo");

    levelOrder = stringBst.root.toLevelOrderString();
    inOrder = stringBst.root.toInOrderString();

    if (!levelOrder.equals("[ delta, alpha, echo, charlie, bravo ]")) {
      return false;
    }
    if (!inOrder.equals("[ alpha, bravo, charlie, delta, echo ]")) {
      return false;
    }
    return true;
  }



  // test 2
  /**
   * Tests the contains() method in different tree structures.
   * 
   * @return true if all tests pass, false otherwise.
   */
  public static boolean test2() {
    // Test 1: Tree with integer values (balanced tree)
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    bst.insert(10); // Root
    bst.insert(5); // Left child of root
    bst.insert(15); // Right child of root
    bst.insert(3); // Left leaf
    bst.insert(7); // Right leaf of left subtree
    bst.insert(12); // Left leaf of right subtree
    bst.insert(20); // Right leaf

    // Check for left leaf
    if (!bst.contains(3)) {
      return false;
    }

    // Check for right leaf
    if (!bst.contains(20)) {
      return false;
    }

    // Check for interior node
    if (!bst.contains(15)) {
      return false;
    }

    // Check for root node
    if (!bst.contains(10)) {
      return false;
    }

    // Check for non-existent value
    if (bst.contains(99)) {
      return false;
    }

    try {
      bst.contains(null); // Should throw an exception
      return false;
    } catch (NullPointerException e) {
    }

    // Test 2: Left-skewed tree
    BinarySearchTree<Integer> leftSkewed = new BinarySearchTree<>();
    leftSkewed.insert(5);
    leftSkewed.insert(4);
    leftSkewed.insert(3);
    leftSkewed.insert(2);
    leftSkewed.insert(1);

    if (!leftSkewed.contains(1)) {
      return false;
    }
    if (leftSkewed.contains(10)) {
      return false;
    }

    // Test 3: Right-skewed tree
    BinarySearchTree<Integer> rightSkewed = new BinarySearchTree<>();
    rightSkewed.insert(1);
    rightSkewed.insert(2);
    rightSkewed.insert(3);
    rightSkewed.insert(4);
    rightSkewed.insert(5);

    if (!rightSkewed.contains(5)) {
      return false;
    }
    if (rightSkewed.contains(0)) {
      return false;
    }

    // Test 4: Tree with string values
    BinarySearchTree<String> stringBst = new BinarySearchTree<>();
    stringBst.insert("delta");
    stringBst.insert("alpha");
    stringBst.insert("charlie");
    stringBst.insert("bravo");
    stringBst.insert("echo");

    if (!stringBst.contains("delta")) {
      return false;
    }
    if (!stringBst.contains("bravo")) {
      return false;
    }
    if (stringBst.contains("zulu")) {
      return false;
    }
    return true;
  }

  /**
   * Tests size() and clear() methods for different tree structures.
   * 
   * @return true if all tests pass, false otherwise.
   */
  public static boolean test3() {
    // Test 1: Tree with integer values (balanced tree)
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    bst.insert(10); // Root
    bst.insert(5); // Left child of root
    bst.insert(15); // Right child of root
    bst.insert(3); // Left leaf
    bst.insert(7); // Right leaf of left subtree
    bst.insert(12); // Left leaf of right subtree
    bst.insert(20); // Right leaf

    if (bst.size() != 7) {
      return false;
    }
    bst.clear();
    if (bst.size() != 0) {
      return false;
    }

    // Test 2: Left-skewed tree
    BinarySearchTree<Integer> leftSkewed = new BinarySearchTree<>();
    leftSkewed.insert(5);
    leftSkewed.insert(4);
    leftSkewed.insert(3);
    leftSkewed.insert(2);
    leftSkewed.insert(1);

    // Check size of the left-skewed tree
    if (leftSkewed.size() != 5) {
      return false;
    }

    leftSkewed.clear();
    if (leftSkewed.size() != 0) {
      return false;
    }

    // Test 3: Right-skewed tree
    BinarySearchTree<Integer> rightSkewed = new BinarySearchTree<>();
    rightSkewed.insert(1);
    rightSkewed.insert(2);
    rightSkewed.insert(3);
    rightSkewed.insert(4);
    rightSkewed.insert(5);

    // Check size of the right-skewed tree
    if (rightSkewed.size() != 5) {
      return false;
    }

    // Clear the right-skewed tree and check the size
    rightSkewed.clear();
    if (rightSkewed.size() != 0) {
      return false;
    }

    // Test 4: Tree with string values
    BinarySearchTree<String> stringBst = new BinarySearchTree<>();
    stringBst.insert("delta");
    stringBst.insert("alpha");
    stringBst.insert("charlie");
    stringBst.insert("bravo");
    stringBst.insert("echo");

    // Check size of the string tree
    if (stringBst.size() != 5) {
      return false;
    }
    // Clear the string tree and check the size
    stringBst.clear();
    if (stringBst.size() != 0) {
      return false;
    }
    return true;
  }

  // main method
  /**
   * Main method to run all test cases.
   * 
   */
  public static void main(String[] args) {
    if (test1()) {
      System.out.println("Test 1 Passed");
    } else {
      System.out.println("Test 1 Failed");
    }
    if (test2()) {
      System.out.println("Test 2 Passed");
    } else {
      System.out.println("Test 2 Failed");
    }

    if (test3()) {
      System.out.println("Test 3 Passed");
    } else {
      System.out.println("Test 3 Failed");
    }


  }

}
