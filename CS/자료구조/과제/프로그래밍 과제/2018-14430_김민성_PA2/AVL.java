public class AVL extends BST {
  public AVL() {
    super();
  }

  private int balanceDiff(Node left, Node right) {
    if(left == null && right == null) {
      return 0;
    } else if (left == null) {
      return -1 * right.height;
    } else if (right == null) {
      return left.height;
    } else {
      return left.height - right.height;
    }
  }

  private int heightCheck(Node node) {
    int ans = 1;
    if (node.left == null && node.right==null) {
      ans = 1;
    } else if (node.left == null) {
      ans = node.right.height+1;
    } else if (node.right == null) {
      ans =  node.left.height+1;
    } else {
      ans =  1+Math.max(node.right.height,node.left.height);
    }
    return ans;
  }

  private Node leftRotate(Node node) {
    Node newRoot = node.right;
    node.right = node.right.left;
    newRoot.left = node;
    return newRoot;
  }

  private Node rightRotate(Node node) {
    Node newRoot = node.left;
    node.left = node.left.right;
    newRoot.right = node;
    return newRoot;
  }

  private Node insertedNode(Node node, String key) {
    if (node == null) {
      size++;
      frequencySum++;
      return new Node(key);
    }else if (key.compareTo(node.key) > 0) {
        node.right = insertedNode(node.right, key);
    }else if (key.compareTo(node.key) < 0) {
      node.left = insertedNode(node.left, key);
    }else if (key.compareTo(node.key) == 0) {
      node.frequency++;
      frequencySum++;
    }
    int diff = balanceDiff(node.left, node.right);
    if (diff == -2) {
      if (balanceDiff(node.right.left, node.right.right) < 0) {
        node = leftRotate(node);
      } else {
        node.right = rightRotate(node.right);
        node = leftRotate(node);
      }
    } else if (diff == 2) {
      if (balanceDiff(node.left.left, node.left.right) > 0) {
        node = rightRotate(node);
      } else {
        node.left = leftRotate(node.left);
        node = rightRotate(node);
      }
    }
    if (node.left != null) {
      node.left.height = heightCheck(node.left);
    }
    if (node.right != null) {
      node.right.height = heightCheck(node.right);
    }
    node.height = heightCheck(node);
    return node;
  }

  public void insert(String key) {
    root = insertedNode(root, key);
  }
}

