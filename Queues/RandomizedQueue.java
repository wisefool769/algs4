import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item>{
    private Item[] items;
    private int n;
    
    private class RQIterator implements Iterator<Item> {
        private Item[] shuffled;
        private int index;
        
        public RQIterator(){
            shuffled = (Item[]) new Object[n];
            for(int i = 0; i < n; i ++){
                shuffled[i] = items[i];
            }
            StdRandom.shuffle(shuffled);
            index = 0;
   
        }
        
        public boolean hasNext(){
            
            return (index < shuffled.length);
        }
        
        public Item next(){
            if (index >= shuffled.length)
                throw new NoSuchElementException("");
            Item ret = shuffled[index];
            index++;
           
            return ret;

        }
        
        public void remove(){
            throw new UnsupportedOperationException("");
        }
    }
    
    public RandomizedQueue(){
        items = (Item[]) new Object[1];
    }
    
    public boolean isEmpty(){
        return (n == 0);
    }
    
    public int size(){
        return n; 
    }
    
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = items[i];
        }
        items= temp;
    }
    
    public void enqueue(Item item){
        if (item == null)
            throw new NullPointerException("can't add null to random queue");
       
        if(n == items.length)
            resize(2 * items.length);
        items[n++] = item;
    }
    
    public Item dequeue(){
        if(n == 0)
            throw new NoSuchElementException("cannot dequeue from empty queue");
        int index = StdRandom.uniform(n);
        Item ret = items[index];
        items[index] = items[n-1];
        items[n-1] = null;
        n--;
        
        if (n > 0 && n == items.length / 4)
            resize(items.length/2);
        
        return ret;
    }

    public Item sample() {
        if(n == 0)
            throw new NoSuchElementException("cannot sample from empty queue");
        return items[StdRandom.uniform(n)];
    }
    
    public Iterator<Item> iterator(){
        return new RQIterator();
    }
    
    private Item getIndex(int idx){
        return items[idx];
    }
    
    private static int runDequeTest(){
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        return rq.dequeue();
    }
    
    private static void testDeque(){
        int ntrials = 10000;
        double[] results = new double[ntrials];
        for(int i = 0; i < ntrials; i ++){
            results[i] = (double) runDequeTest();
        }
        double mean = StdStats.mean(results);
        double stddev = StdStats.stddev(results);
        assert Math.abs(mean - 2.5) < 0.1;
        System.out.println("mean of 1,2,3,4 deque: " + mean);
    }
        
    public static void main(String[] args){
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        assert rq.size() == 0;
        assert rq.isEmpty();
        rq.enqueue(1);
        assert rq.size() == 1;
        assert (!rq.isEmpty());
        
        rq.enqueue(2);
        assert rq.size() == 2;

        testDeque();
        
        for(int i = 3; i != 6; i++)
            rq.enqueue(i);
        
        Iterator<Integer> iter = rq.iterator();
        while(iter.hasNext()){
            System.out.println(iter.next());
        }
        
        for(int i : rq){
            for(int j : rq){
                System.out.println("outer: " + i + ", inner: " + j);
            }
        }
        
        System.out.println("done");
    }
}