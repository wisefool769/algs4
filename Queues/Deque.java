import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private DoubleNode first;
    private DoubleNode last;
    private int size;
    
    private class DoubleNode {
        Item item;
        DoubleNode next;
        DoubleNode prev;   
    }
   
    private class DequeIterator implements Iterator<Item> {
        private DoubleNode current = first;
        public boolean hasNext() {
            return current != null;
        }
        
        public Item next() {
            if (current == null)
                throw new NoSuchElementException("called next on empty Deque");
            
            Item item = current.item;
            current = current.next;
            return item;
        }
        
        public void remove() {
            throw new UnsupportedOperationException
                ("operation remove on Deque not supported");
        }
    }
        
    public Deque() {
        first = null;
        last = null;
    }
    
    public boolean isEmpty() {
        return first == null;
    }
    
    public int size() {
        return size;
    }
    public void addFirst(Item item){
        if (item == null)
            throw new NullPointerException("cannot add null to Deque");
        
        DoubleNode node = new DoubleNode();
        node.item = item;
        node.prev = null;
        node.next = first;
            
        if(first == null){
            last = node;
        } else {
            first.prev = node;
        }
        
        first = node;
        size++;
    }
    
    public void addLast(Item item) {
        if (item == null)
            throw new NullPointerException("cannot add null to Deque");
        
        DoubleNode node = new DoubleNode();
        node.item = item;
        node.prev = last;
        node.next = null;
            
        if(last == null){
            first = node;
        } else {
            last.next = node;
        }
        
        last = node;
        size++;
    }
    
    public Item removeFirst() {
        if (first == null)
            throw new NoSuchElementException(
              "Cannot remove item from empty deque.");
        Item item = first.item;
        first = first.next;
        size--;
        if(first != null){
            first.prev = null;
        } else {
            last = null;
        }

        return item;
    }
    
    public Item removeLast() {
        if (last == null)
            throw new NoSuchElementException(
              "Cannot remove item from empty deque.");
        Item item = last.item;
        last = last.prev;
        size--;
        
        if(last != null){
            last.next = null;
        } else {
            first = null;
        }
        
        return item;
    }
    

    
    // convenient for use in main method tests
    private DoubleNode getFirst() {
        return first;
    }
    
    // convenient for use in main method tests
    private DoubleNode getLast() {
        return last;
    }
    
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    public static void main(String[] args) {
        Deque<Integer> dd = new Deque<Integer>();
        assert dd.isEmpty();
        assert dd.size() == 0;
        
        try {
            dd.addFirst(null);
            System.out.println("null failure");
        } catch (NullPointerException e) {
            System.out.println("addFirst exception thrown correctly");
        }
        
        dd.addFirst(2);
        assert dd.getFirst().item == 2;
        assert dd.getFirst().prev == null;
        assert dd.getFirst().next == null;
        assert dd.getLast().item == 2;
        assert dd.getLast().prev == null;
        assert dd.getLast().next == null;
        assert dd.size() == 1;
        
        Deque<Integer> dd2 = new Deque<Integer>();
        
        try {
            dd2.addFirst(null);
            System.out.println("null failure");
        } catch (NullPointerException e) {
            System.out.println("addLast exception thrown correctly");
        }
        dd2.addLast(3);
        assert dd2.getFirst().item == 3;
        assert dd2.getFirst().prev == null;
        assert dd2.getFirst().next == null;
        assert dd2.getLast().item == 3;
        assert dd2.getLast().prev == null;
        assert dd2.getLast().next == null;
        assert dd2.size() == 1;
        
        
        dd.addLast(4);
        assert dd.getFirst().item == 2;
        assert dd.getFirst().next.item == 4;
        assert dd.getFirst().prev == null;
        assert dd.getLast().item == 4;
        assert dd.getLast().prev.item == 2;
        assert dd.getLast().next == null;
        assert dd.size() == 2;
        
        Deque<Integer> dd3 = new Deque<Integer>();
        try{
            dd3.removeFirst();
        } catch (NoSuchElementException e) {
            System.out.println("removeFirst exception thrown correctly");
        }
        
        try{
            dd3.removeLast();
        } catch (NoSuchElementException e) {
            System.out.println("removeLast exception thrown correctly");
        }
        
        int ddpop = dd.removeFirst();
        assert ddpop == 2;
        assert dd.getFirst().item == 4;
        assert dd.getLast().item == 4;
        assert dd.getFirst().next == null;
        assert dd.getLast().next == null;
        assert dd.getFirst().prev == null;
        assert dd.getLast().prev == null;
        assert dd.size() == 1;
        
        dd.addFirst(2);
        
        int ddpoplast = dd.removeLast();
        assert ddpoplast == 4;
        assert dd.getFirst().item == 2;
        assert dd.getLast().item == 2;
        assert dd.getFirst().next == null;
        assert dd.getLast().next == null;
        assert dd.getFirst().prev == null;
        assert dd.getLast().prev == null;
        assert dd.size() == 1;
        
        dd.addLast(4);
        
        for(int ii : dd)
            System.out.println(ii);
        
        for(int ii : dd){   
            for(int jj : dd) {
                System.out.println("outer: " + ii + ", inner: " + jj);
            }
        }
        
        Iterator<Integer> iter = dd.iterator();
        try{    
            iter.remove();
        } catch (UnsupportedOperationException e) {
            System.out.println("remove exception thrown correctly");
        }
        
        try{
            while(iter.hasNext()) {
                int ii = iter.next();
            }
            int err_int = iter.next();
            assert false;
        } catch (NoSuchElementException e) {
            System.out.println("iterator next exception thrown correctly");
        }
        
        // see what happens when data structure becomes empty, then non-empty
        assert dd2.size() == 1;
        dd2.removeFirst();
        assert dd2.isEmpty();
        dd2.addLast(3);
        assert dd2.getFirst().item == 3;
        assert dd2.getFirst().prev == null;
        assert dd2.getFirst().next == null;
        assert dd2.getLast().item == 3;
        assert dd2.getLast().prev == null;
        assert dd2.getLast().next == null;
        assert dd2.size() == 1;
        
        System.out.println("done");
    }
}
    