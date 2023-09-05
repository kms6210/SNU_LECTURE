import java.util.*;

public class LinearHash {
  private ArrayList<LinkedList<String>> hashTable;
  private int splitIndex;
  private int WordNum;
  private int HTSize;

  public LinearHash(int HTinitSize)	{
    splitIndex = 0;
    WordNum = 0;
    HTSize = HTinitSize;
    ArrayList<LinkedList<String>> table = new ArrayList<>();
    for (int i = 0; i < HTinitSize; i++) {
      table.add(null);
    }
    hashTable = table;
  }

  private int getIndex(String word) {
    int idx;
    if ((int)MyUtil.ELFhash(word, HTSize) >= splitIndex) {
      idx = (int)MyUtil.ELFhash(word, HTSize);
    } else {
      idx = (int)MyUtil.ELFhash(word, HTSize * 2);
    }
    return idx;
  }

  public int insertUnique(String word) {
    int idx = getIndex(word);
    if (hashTable.get(idx) == null) {
      LinkedList<String> linkedList = new LinkedList<>();
      linkedList.add(word);
      hashTable.set(idx, linkedList);
    } else {
      if (lookup(word) > 0) {
        return -1;
      }
      hashTable.get(idx).add(word);
      hashTable.add(null);
      LinkedList<String> oLinkedlist = new LinkedList<>();
      LinkedList<String> nLinkedlist = new LinkedList<>();
      if (hashTable.get(splitIndex) != null) {
        Iterator<String> iterator = hashTable.get(splitIndex).iterator();
        while(iterator.hasNext()) {
          String s = iterator.next();
          if ((int)MyUtil.ELFhash(s, HTSize * 2) < HTSize) {
            oLinkedlist.add(s);
            Collections.sort(oLinkedlist);
          }else {
            nLinkedlist.add(s);
            Collections.sort(nLinkedlist);
          }
        }
        if(oLinkedlist.isEmpty()) {
          hashTable.set(splitIndex, null);
        }else {
          hashTable.set(splitIndex, oLinkedlist) ;
        }
        if(nLinkedlist.isEmpty()) {
          hashTable.set(splitIndex + HTSize, null) ;
        }else {
          hashTable.set(splitIndex + HTSize, nLinkedlist) ;
        }
      }
      if (++splitIndex == HTSize) {
        HTSize *= 2;
        splitIndex = 0;
      }
      if(hashTable.get(idx) != null) {
        Collections.sort(hashTable.get(idx));
      }
    }
    WordNum++;
    return idx;
  }

  public int lookup(String word) {
    int idx = getIndex(word);
    if (hashTable.get(idx) == null) {
      return 0;
    }
    int size = hashTable.get(idx).size();
    if (hashTable.get(idx).contains(word)) {
      return size;
    }else {
      return -1 * size;
    }
  }

  public int wordCount() {
    return WordNum;
  }

  public int emptyCount() {
    Iterator<LinkedList<String>> iterator = hashTable.iterator();
    int emptycnt = 0;
    while(iterator.hasNext()) {
      if( iterator.next() == null ) {
        emptycnt++;
      }
    }
    return emptycnt;
  }

  public int size() {
    int size = HTSize + splitIndex;
    return size;
  }

  public void print() {
    Iterator<LinkedList<String>> listIterator = hashTable.iterator();
    int idx = 0;
    while(listIterator.hasNext()) {
      String wordList = "";
      LinkedList<String> linkedList = listIterator.next();
      if (linkedList == null) {
        System.out.println("[" + idx++ + ":" + wordList + "]");
        continue;
      }
      Iterator<String> stringIterator = linkedList.iterator();
      while(stringIterator.hasNext()) {
        String s = stringIterator.next();
        if (s != null) {
          wordList += s + " ";
        }
      }
      wordList = wordList.substring(0, wordList.length() - 1);
      System.out.println("[" + idx++ + ": " + wordList + "]");
    }
  }
}
