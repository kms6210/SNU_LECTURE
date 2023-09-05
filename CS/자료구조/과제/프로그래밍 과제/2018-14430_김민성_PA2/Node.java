public class Node {
    public Node left; public Node right; public String key; public int accessCount; public int frequency; public int height;
    public Node(String key) { left = null; right = null; this.key = key; frequency = 1; accessCount = 0; height =  1; }
}