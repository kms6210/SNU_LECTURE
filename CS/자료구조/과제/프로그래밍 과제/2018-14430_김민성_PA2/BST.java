public class BST {
  protected Node root;
  protected int size;
  protected int frequencySum;
  protected int accessCountSum;

  public BST() {
    root = null;
    size = 0;
    frequencySum = 0;
    accessCountSum = 0;
  }

  private Node insertedNode(Node node, String key) {
    if (node == null) {
      Node newNode = new Node(key);
      node = newNode;
      size++;
      frequencySum++;
    }else if (key.compareTo(node.key) > 0) {
      node.right = insertedNode(node.right, key);
    }else if (key.compareTo(node.key) < 0) {
      node.left = insertedNode(node.left, key);
    }else if (key.compareTo(node.key) == 0) {
      node.frequency++;
      frequencySum++;
    }
    return node;
  }

  private Node findedNode(Node node, String key) {
    if (node == null) {
      return null;
    } else if (key.compareTo(node.key) > 0) {
      node.accessCount++;
      accessCountSum++;
      return findedNode(node.right, key);
    } else if (key.compareTo(node.key) < 0) {
      node.accessCount++;
      accessCountSum++;
      return findedNode(node.left, key);
    } else if (key.compareTo(node.key) == 0) {
      node.accessCount++;
      accessCountSum++;
      return node;
    }
    return null;
  }

  private void resetting(Node node) {
    if (node != null) {
      node.accessCount = 0;
      node.frequency = 1;
      resetting(node.left);
      resetting(node.right);
    }
  }

  private void printing(Node node) {
    if (node != null) {
      printing(node.left);
      System.out.println("[" + node.key + ":" + node.frequency + ":" + node.accessCount + "]");;
      printing(node.right);
    }
  }

  public void insert(String key) {
    root = insertedNode(root, key);
  }

  public boolean find(String key) {
    if (findedNode(root,key) == null) {
      return false;
    }
    return true;
  }

  public int size() {
    return size;
  }

  public int sumFreq() {
    return frequencySum;
  }

  public int sumProbes() {
    return accessCountSum;
  }

  public void resetCounters() {
    accessCountSum = 0;
    frequencySum = size;
    resetting(root);
  }

  public void print() {
    printing(root);
  }
}
