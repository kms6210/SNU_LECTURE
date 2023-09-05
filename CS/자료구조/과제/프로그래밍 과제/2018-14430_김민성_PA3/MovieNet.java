import java.lang.*;
import java.util.*;
import java.io.*;

class Node {
  public HashMap<String, HashMap<String, Integer>> node = new HashMap<>();
}

public class MovieNet {
  Node network = new Node();
  static final String KevinBacon = "Bacon, Kevin";
  HashMap<String, HashSet <String>> movies = new HashMap<>();


  public MovieNet(LinkedList<String[]> movielines) {
    for(String[] mvline : movielines) {
      String[] actors = new String[mvline.length - 1];
      for(int i = 0; i < mvline.length - 1; i++) {
        actors[i] = mvline[i + 1];
      }

      HashSet<String> movie_actors = new HashSet<>(Arrays.asList(actors));
      movies.put(mvline[0], movie_actors);

      for (int i = 0; i < actors.length; i++) {
        List<String> tempList = new LinkedList<>(Arrays.asList(actors));
        tempList.remove(actors[i]);
        String[] temparr = tempList.toArray(new String[tempList.size()]);
        if(network.node.get(actors[i]) == null ) {
          HashMap<String,Integer> edgemap = new HashMap<>();
          network.node.put(actors[i], edgemap);
        }
        for (String actor : temparr) {
          if(network.node.get(actors[i]).containsKey(actor)) {
            network.node.get(actors[i]).put(actor, network.node.get(actors[i]).get(actor) + 1);
          }else {
            network.node.get(actors[i]).put(actor, 1);
          }
        }
      }
    }
  }


  // [Q1]
  public String[] moviesby(String[] actors) {
    LinkedList<String> linkedlist = new LinkedList<>();

    for (String movie : movies.keySet()) {
      boolean boolflag = true;
      for (int i = 0; i < actors.length; i++) {
        if (!movies.get(movie).contains(actors[i])) {
          boolflag = false;
        }
      }
      if (boolflag == true) {
        linkedlist.add(movie);
      }
    }

    if(linkedlist.size() == 0) {
      return null;
    } else {
      return linkedlist.toArray(new String[linkedlist.size()]);
    }
  }


  // [Q2]
  public String[] castin(String[] titles) {
    HashSet<String> meeting = new HashSet<>();
    if(movies.get(titles[0]) == null) {
      return null;
    }
    meeting.addAll(movies.get(titles[0]));

    if(titles.length == 1) {
      return meeting.toArray(new String[meeting.size()]);
    }

    for (int i = 1; i < titles.length; i++) {
      HashSet<String> hashset = new HashSet<>();
      if(movies.get(titles[i]) == null) {
        continue;
      }
      hashset.addAll(movies.get(titles[i]));
      meeting.retainAll(hashset);
    }

    if(meeting.size() == 0) {
      return null;
    } else {
      return meeting.toArray(new String[meeting.size()]);
    }
  }


  // [Q3]
  public String[] pairmost(String[] actors) {
    int maxval = 0 ;
    String[] pair = new String[2];

    for (int i = 0; i < actors.length; i++) {
      for (int j = 0; j < actors.length; j++) {
        if (i <= j) {
          continue;
        }
        if (network.node.get(actors[i]).containsKey(actors[j]) && network.node.get(actors[i]).get(actors[j]) > maxval) {
          maxval = network.node.get(actors[i]).get(actors[j]);
          pair[0] = actors[i];
          pair[1] = actors[j];
        }
      }
    }

    if(maxval == 0) {
      return null;
    } else {
      return pair;
    }
  }


  // [Q4]
  public int Bacon(String actor) {
    if (network.node.containsKey(KevinBacon) == false || network.node.containsKey(actor) == false ) {
      return -1;
    } else {
      return BFS(KevinBacon, actor);
    }
  }


  // [Q5]
  public int distance(String src, String dst) {
    if (network.node.containsKey(src) == false | network.node.containsKey(dst) == false ) {
      return -1;
    } else {
      return BFS(src, dst);
    }
  }


  // [Q6]
  public int npath(String src, String dst) {
    int dist = distance(src, dst);
    int count = 0;
    if (network.node.containsKey(src) == false || network.node.containsKey(dst) == false || dist == -1) {
      return 0;
    }

    Queue<String> queue = new LinkedList<>();
    HashMap<String,Integer> hashmap = new HashMap<>();
    for(String key : network.node.keySet()) {
      hashmap.put(key, -1);
    }
    queue.offer(src);
    hashmap.put(src, 0);

    while(!queue.isEmpty()) {
      String present = queue.poll();
      if(hashmap.get(present) == dist - 1) {
        break;
      }
      Set<String> nodes = network.node.get(present).keySet();

      for(String presentNode : nodes) {
        if (hashmap.get(presentNode) == -1) {
          queue.offer(presentNode);
          hashmap.put(presentNode, hashmap.get(present) + 1);
        }
      }
    }

    HashMap<String,Integer> hashmapBFS = hashmap;
    HashMap<String,Integer> hashmapDistance = new HashMap<>();

    for(String key : hashmapBFS.keySet()){
      if(hashmapBFS.get(key) >= 0){
        hashmapDistance.put(key, hashmapBFS.get(key));
      };
    }

    HashMap<Integer, LinkedList<String>> hashmapTemp = new HashMap<>();
    for(int i = 0; i < dist; i++) {
      LinkedList<String> linkedlist = new LinkedList<>();
      hashmapTemp.put(i, linkedlist);
    }

    for (String key : hashmapDistance.keySet()){
      hashmapTemp.get(hashmapDistance.get(key)).add(key);
    }

    for(int i = 2; i < dist; i++) {
      for(String present : hashmapTemp.get(i)) {
        int preCnt = 0;
        for(String presentNode : hashmapTemp.get(i-1)) {
          if(network.node.get(present).containsKey(presentNode)) {
            preCnt += hashmapDistance.get(presentNode);
          }
        }
        hashmapDistance.put(present, preCnt);
      }
    }

    for(String s : hashmapTemp.get(dist - 1)) {
      if(network.node.get(dst).keySet().contains(s)) {
        count += hashmapDistance.get(s);
      }
    }

    if(count == 0) {
      count++;
    }

    return count;
  }


  // [Q7]
  public String[] apath(String src, String dst) {
    if (network.node.containsKey(src) == false || network.node.containsKey(dst) == false || distance(src,dst) == -1 ) {
      return null;
    } else {
      Queue<String> queue = new LinkedList<>();
      int count = 0;
      HashMap<String,Integer> dmap = new HashMap<>();
      HashMap<String,String> pmap = new HashMap<>();
      for(String key : network.node.keySet()) {
        dmap.put(key, -1);
        pmap.put(key, "NA");
      }

      dmap.put(src, 0);
      pmap.put(src, "start");
      queue.offer(src);

      while(!queue.isEmpty()) {
        String present = queue.poll();
        Set<String> nodes = network.node.get(present).keySet();
        for(String presentNode : nodes) {
          if (dmap.get(presentNode) == -1) {
            queue.offer(presentNode);
            dmap.put(presentNode, dmap.get(present) + 1);
            pmap.put(presentNode, present);
            if(presentNode.equals(dst)) {
              count = dmap.get(presentNode);
              break;
            }
          }
        }
      }

      String[] road = new String[count + 1];
      String presentNode = dst;
      for (int i = count; i >= 0; i--) {
        road[i] = presentNode;
        presentNode = pmap.get(presentNode);
      }
      return road;
    }
  }


  // [Q8]
  public int eccentricity(String actor) {
    if(network.node.containsKey(actor) == false) {
      return -1;
    } else {
      int[] bfs = BFS2(actor);
      int maximum = bfs[0];
      for (int i = 1; i < bfs.length; i++) {
        if(maximum < bfs[i] ) {
          maximum = bfs[i];
        }
      }
      return maximum;
    }
  }


  // [Q9]
  public float closeness(String actor) {
    if(network.node.containsKey(actor) == false) {
      return -1;
    } else {
      int[] bfs2 = BFS2(actor);
      float result =0;
      for (int i = 0; i < bfs2.length; i++) {
        if(bfs2[i] == -1) {
          continue;
        }
        result += 1 / Math.pow(2, bfs2[i]);
      }
      return result - 1;
    }
  }


  public int BFS(String start, String end) {
    Queue<String> queue = new LinkedList<>();
    HashMap<String, Integer> hashmap = new HashMap<>();
    for(String key : network.node.keySet()) {
      hashmap.put(key, -1);
    }
    hashmap.put(start, 0);
    queue.offer(start);

    while(!queue.isEmpty()) {
      String present = queue.poll();
      Set<String> nodes = network.node.get(present).keySet();
      for(String presentNode : nodes) {
        if (hashmap.get(presentNode)==-1) {
          queue.offer(presentNode);
          hashmap.put(presentNode, hashmap.get(present) + 1);
          if(presentNode.equals(end)) { return hashmap.get(presentNode);}
        }
      }
    }
    return -1;
  }


  public int[] BFS2(String src) {
    Queue<String> que = new LinkedList<String>();
    HashMap<String,Integer> d = new HashMap<>();
    Iterator<String> keys = network.node.keySet().iterator();
    while(keys.hasNext()) {
      String key = keys.next();
      d.put(key,-1);
    }
    que.offer(src);
    d.put(src,0);

    while(!que.isEmpty()) {
      String current = que.poll();

      Set<String> connectedNodes = network.node.get(current).keySet();
      Iterator<String> it = connectedNodes.iterator();
      while(it.hasNext()) {
        String crnt = it.next();
        if (d.get(crnt)==-1) {
          que.offer(crnt);
          d.put(crnt,d.get(current)+1);
        }
      }
    }

    int[] output = new int[d.size()];
    int count =0;

    for(String k : d.keySet()){
      int temp = d.get(k);
      output[count] = temp;
      count++;
    }
    return output;
  }
}


