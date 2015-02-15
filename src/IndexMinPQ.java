import java.util.Iterator;
import java.util.NoSuchElementException;


public class IndexMinPQ<Type extends Comparable<Type>> implements Iterable<Integer>{
	
	private int[] pq;
	private int[] invpq;
	private Type[] keys;
	private int n;
	
	@SuppressWarnings("unchecked")
	public IndexMinPQ(int capacity){
		pq = new int[capacity];
		invpq = new int[capacity];
		keys = (Type[]) new Comparable[capacity];
		for (int i = 0; i < capacity; i++) 
			invpq[i] = -1;
	}
	
	public boolean isEmpty(){
		return n == 0;
	}
	
	public void insert(int i, Type key){
		keys[i] = key;
		pq[n] = i;
		invpq[i] = n;
		swim(++n);
	}
	
	public int delMin(){
		int min = pq[0];
		exch(1, n--);
		sink(1);
		invpq[min] = -1;           
        keys[pq[n]] = null;   
        pq[n] = -1;            
        return min; 
	}
	
	private void swim(int k){
		while (k > 1 && greater(k / 2, k)){
			exch(k, k / 2);
			k = k / 2;
		}
	}
	
	private void sink(int k){
		while(2 * k <= n){
			int j = 2 * k;
			if (j < n && greater(j, j + 1)) j++;
			if (!greater(k, j)) break;
			exch(k, j);
			k=j;
		}
	}
	
	private boolean greater(int i, int j){
		return keys[pq[i - 1]].compareTo(keys[pq[j - 1]]) > 0;
	}
	
	private void exch(int i, int j) {
    	int swap = pq[i - 1]; 
    	pq[i - 1] = pq[j - 1]; 
    	pq[j - 1] = swap;
        invpq[pq[i - 1]] = i - 1; 
        invpq[pq[j - 1]] = j - 1;
    }
	
	public boolean contains(int i) {
        return invpq[i] != -1;
    }
	
	public void changeKey(int i, Type key) {
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        keys[i] = key;
        swim(invpq[i] + 1);
        sink(invpq[i] + 1);
    }
	
	
	public void delete(int i) {
		if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        int index = invpq[i] + 1;
        exch(index, n--);
        swim(index);
        sink(index);
        keys[i] = null;
        invpq[i] = -1;
    }

	@Override
	public Iterator<Integer> iterator() {
		return new HeapIterator(); 
	}
	

    private class HeapIterator implements Iterator<Integer> {
        private IndexMinPQ<Type> copy;

        public HeapIterator() {
            copy = new IndexMinPQ<Type>(pq.length);
            for (int i = 0; i < n; i++)
                copy.insert(pq[i], keys[pq[i]]);
        }

        public boolean hasNext()  {
        	return !copy.isEmpty();                     
    	}
        
        public void remove()      { 
        	throw new UnsupportedOperationException();
    	}

        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }
	
	 
}
