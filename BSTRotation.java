public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {

  public BSTRotation() {
    this.root = null;
  }

  /**
   * Performs the rotation operation on the provided nodes within this tree. When the provided child
   * is a left child of the provided parent, this method will perform a right rotation. When the
   * provided child is a right child of the provided parent, this method will perform a left
   * rotation. When the provided nodes are not related in one of these ways, this method will either
   * throw a NullPointerException: when either reference is null, or otherwise will throw an
   * IllegalArgumentException.
   *
   * @param child  is the node being rotated from child to parent position
   * @param parent is the node being rotated from parent to child position
   * @throws NullPointerException     when either passed argument is null
   * @throws IllegalArgumentException when the provided child and parent nodes are not initially
   *                                  (pre-rotation) related that way
   */
  protected void rotate(BinaryTreeNode<T> child, BinaryTreeNode<T> parent)
      throws NullPointerException, IllegalArgumentException {

    // Check if either child or parent is null
    if (child == null || parent == null) {
      throw new NullPointerException("Child and parent nodes cannot be null.");
    }

    // Ensure that child is a direct left or right child of the parent
    if (parent.childLeft() != child && parent.childRight() != child) {
      throw new IllegalArgumentException(
          "Child is not a direct left or right child of the parent.");
    }

    // Grandparent is used to maintain the upper-level structure during rotation
    BinaryTreeNode<T> grandparent = null;
    boolean isRootRotation = (root == parent); // Check if rotating the root

    // If it's not a root rotation, get the grandparent node
    if (!isRootRotation) {
      grandparent = parent.parent();
    }

    // Right Rotation (if child is left of parent)
    if (parent.childLeft() == child) {
      // Parent's left child becomes the child's right child
      parent.setChildLeft(child.childRight());

      // If the child's right child exists, update its parent to be the parent
      if (child.childRight() != null) {
        child.childRight().setParent(parent);
      }
      // Child becomes the new parent, setting the parent as the right child of the child
      child.setChildRight(parent);
      parent.setParent(child);
      // Set the grandparent's child reference to the child if applicable
      child.setParent(grandparent);

      if (grandparent != null) {
        if (grandparent.childLeft() == parent) {
          grandparent.setChildLeft(child);
        } else {
          grandparent.setChildRight(child);
        }
      }

      // If rotation involves the root, update the root reference
      if (isRootRotation) {
        root = child; // Update root
      }
    }

    // Left Rotation (if child is right of parent)
    if (parent.childRight() == child) {
      // Parent's right child becomes the child's left child
      parent.setChildRight(child.childLeft());
      // If the child's left child exists, update its parent to be the parent
      if (child.childLeft() != null) {
        child.childLeft().setParent(parent);
      }

      // Child becomes the new parent, setting the parent as the left child of the child
      child.setChildLeft(parent);
      parent.setParent(child);
      // Set the grandparent's child reference to the child if applicable
      child.setParent(grandparent);

      if (grandparent != null) {
        if (grandparent.childLeft() == parent) {
          grandparent.setChildLeft(child);
        } else {
          grandparent.setChildRight(child);
        }
      }

      // If rotation involves the root, update the root reference
      if (isRootRotation) {
        root = child; // Update root
      }
    }
  }

  /**
   * Test 1: Right rotation and left rotation on basic tree structure
   * 
   * @return true if the test passes, false otherwise.
   */

  // Test 1: Right rotation where the child is the left child of the parent
  public static boolean test1() {
    // Create nodes manually
    BinaryTreeNode<Integer> node1 = new BinaryTreeNode<>(1);
    BinaryTreeNode<Integer> node2 = new BinaryTreeNode<>(2);
    BinaryTreeNode<Integer> node3 = new BinaryTreeNode<>(3);
    BinaryTreeNode<Integer> node4 = new BinaryTreeNode<>(4);
    BinaryTreeNode<Integer> node6 = new BinaryTreeNode<>(6);
    BinaryTreeNode<Integer> node7 = new BinaryTreeNode<>(7);
    BinaryTreeNode<Integer> node8 = new BinaryTreeNode<>(8);

    // Manually set relationships between nodes (building the tree)
    node4.setChildLeft(node2);
    node4.setChildRight(node6);

    node2.setChildLeft(node1);
    node2.setChildRight(node3);
    node6.setChildRight(node7);
    node7.setChildRight(node8);

    // Set parent references
    node2.setParent(node4);
    node6.setParent(node4);
    node1.setParent(node2);
    node3.setParent(node2);
    node7.setParent(node6);
    node8.setParent(node7);

    // The tree should now bebst:
    // 4
    // / \
    // 2 6
    // / \ \
    // 1 3 7
    // \
    // 8

    BSTRotation<Integer> bst = new BSTRotation<>();
    bst.root = node4;

    System.out.println("Before Rotation: " + bst.root.toLevelOrderString());

    bst.rotate(node8, node7);

    String levelOrderAfterRotation = bst.root.toLevelOrderString(); // Expected: [4, 2, 6, 1, 3, 8,
                                                                    // 7]
    System.out.println("After Rotation: " + levelOrderAfterRotation);

    // Expected tree structure after left rotation:
    // 4
    // / \
    // 2 6
    // / \ \
    // 1 3 8
    // /
    // 7

    if (!levelOrderAfterRotation.equals("[ 4, 2, 6, 1, 3, 8, 7 ]")) {
      return false;
    }


    bst.rotate(node1, node2);
    // Expected tree structure after right rotation:
    // 4
    // / \
    // 1 6
    // \ \
    // 2 8
    // / /
    // 3 7

    String expectedAfterRightRotation = "[ 4, 1, 6, 2, 8, 3, 7 ]";
    String actualAfterRightRotation = bst.root.toLevelOrderString();

    if (!expectedAfterRightRotation.equals(actualAfterRightRotation)) {
      return false;
    }

    return true;
  }


  /**
   * Test 2: Right rotation and left rotation on nodes that involve the root
   * 
   * @return true if the test passes, false otherwise.
   */
  public static boolean test2() {
    // Create nodes manually
    BinaryTreeNode<Integer> node1 = new BinaryTreeNode<>(1);
    BinaryTreeNode<Integer> node2 = new BinaryTreeNode<>(2);
    BinaryTreeNode<Integer> node3 = new BinaryTreeNode<>(3);
    BinaryTreeNode<Integer> node4 = new BinaryTreeNode<>(4); // Root initially
    BinaryTreeNode<Integer> node5 = new BinaryTreeNode<>(5);
    BinaryTreeNode<Integer> node6 = new BinaryTreeNode<>(6);
    BinaryTreeNode<Integer> node7 = new BinaryTreeNode<>(7);

    node4.setChildLeft(node2);
    node4.setChildRight(node6);
    node2.setChildLeft(node1);
    node2.setChildRight(node3);
    node6.setChildLeft(node5);
    node6.setChildRight(node7);

    // Set parent references
    node2.setParent(node4);
    node6.setParent(node4);
    node1.setParent(node2);
    node3.setParent(node2);
    node5.setParent(node6);
    node7.setParent(node6);

    // Initial tree structure:
    // 4
    // / \
    // 2 6
    // / \ / \
    // 1 3 5 7

    BSTRotation<Integer> bst = new BSTRotation<>();
    bst.root = node4;
    bst.rotate(node2, node4);


    String expectedAfterRightRotation = "[ 2, 1, 4, 3, 6, 5, 7 ]";
    String actualAfterRightRotation = bst.root.toLevelOrderString();

    if (!expectedAfterRightRotation.equals(actualAfterRightRotation)) {
      return false;
    }

    bst.rotate(node4, node2);
    String expectedAfterLeftRotation = "[ 4, 2, 6, 1, 3, 5, 7 ]";
    String actualAfterLeftRotation = bst.root.toLevelOrderString();

    if (!expectedAfterLeftRotation.equals(actualAfterLeftRotation)) {
      return false;
    }

    return true;
  }


  /**
   * Test 3: Right rotation and left rotation on nodes that have 0,1,2,3 shared children
   * 
   * @return true if the test passes, false otherwise.
   */
  public static boolean test3() {
    // Create nodes manually
    BinaryTreeNode<Integer> node1 = new BinaryTreeNode<>(1);
    BinaryTreeNode<Integer> node2 = new BinaryTreeNode<>(2);
    BinaryTreeNode<Integer> node3 = new BinaryTreeNode<>(3);
    BinaryTreeNode<Integer> node4 = new BinaryTreeNode<>(4); // Root initially
    BinaryTreeNode<Integer> node5 = new BinaryTreeNode<>(5);
    BinaryTreeNode<Integer> node6 = new BinaryTreeNode<>(6);
    BinaryTreeNode<Integer> node7 = new BinaryTreeNode<>(7);
    BinaryTreeNode<Integer> node8 = new BinaryTreeNode<>(8);
    BinaryTreeNode<Integer> node9 = new BinaryTreeNode<>(9);

    // Build initial tree structure
    node4.setChildLeft(node2);
    node4.setChildRight(node6);
    node2.setChildLeft(node1);
    node2.setChildRight(node3);
    node6.setChildLeft(node5);
    node6.setChildRight(node7);
    node7.setChildRight(node8);
    node8.setChildRight(node9);

    node2.setParent(node4);
    node6.setParent(node4);
    node1.setParent(node2);
    node3.setParent(node2);
    node5.setParent(node6);
    node7.setParent(node6);
    node8.setParent(node7);
    node9.setParent(node8);

    // Initial tree structure:
    // 4
    // / \
    // 2 6
    // / \ / \
    // 1 3 5 7
    // \
    // 8
    // \
    // 9

    BSTRotation<Integer> bst = new BSTRotation<>();
    bst.root = node4;
    // **Case 1: 0 Shared Children**
    bst.rotate(node9, node8);
    String expectedAfterRotation1 = "[ 4, 2, 6, 1, 3, 5, 7, 9, 8 ]";
    if (!bst.root.toLevelOrderString().equals(expectedAfterRotation1)) {
      return false;
    }

    // AFTER ROATION ONE tree structure:
    // 4
    // / \
    // 2 6
    // / \ / \
    // 1 3 5 7
    // \
    // 9
    // /
    // 8


    // **Case 2: 1 Shared Child**
    // Rotate node7 with node9 (shared child: node8)
    bst.rotate(node9, node7);
    String expectedAfterRotation2 = "[ 4, 2, 6, 1, 3, 5, 9, 7, 8 ]";
    if (!bst.root.toLevelOrderString().equals(expectedAfterRotation2)) {
      return false;
    }

    // AFTER ROATION ONE tree structure:
    // 4
    // / \
    // 2 6
    // / \ / \
    // 1 3 5 9
    // /
    // 7
    // \
    // 8


    // **Case 3: 2 Shared Child**
    // Rotate node6 with node9 (shared child: node5,7)
    bst.rotate(node9, node6);
    String expectedAfterRotation3 = "[ 4, 2, 9, 1, 3, 6, 5, 7, 8 ]";
    if (!bst.root.toLevelOrderString().equals(expectedAfterRotation3)) {
      return false;
    }

    // AFTER ROATION ONE tree structure:
    // 4
    // / \
    // 2 9
    // / \ /
    // 1 3 6
    // / \
    // 5 7
    // \
    // 8

    // **Case 4: 3 Shared Child**
    // Rotate node4 with node2 (shared child: node1,3,9)
    bst.rotate(node2, node4);
    String expectedAfterRotation4 = "[ 2, 1, 4, 3, 9, 6, 5, 7, 8 ]";
    if (!bst.root.toLevelOrderString().equals(expectedAfterRotation4)) {
      return false;
    }
    // after final rotation
    // 2
    // / \
    // 1 4
    // / \
    // 3 9
    // /
    // 6
    // / \
    // 5 7
    // \
    // 8
    return true;
  }



  public static void main(String[] args) {
    System.out.println("Test 1 : " + (test1() ? "Passed" : "Failed"));

    System.out.println("Test 2 : " + (test2() ? "Passed" : "Failed"));

    System.out.println("Test 3 : " + (test3() ? "Passed" : "Failed"));
  }



}


