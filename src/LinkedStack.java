import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedStack<Type> implements Iterable<Type> {
	protected Node first;
	protected int count = 0;
	
	private class Node{
		Type data;
		Node next;
	}
	
	public void push(Type data) {
		Node oldFirst = first;
		first = new Node();
		first.data = data;
		first.next = oldFirst;
		count++;
	}

	public Type pop() {
		if (isEmpty()) throw new RuntimeException("Stack underflow");
		Type data = first.data;
		first = first.next;
		count--;
		return data;
	}

	public boolean isEmpty() {
		return first == null;
	}
	
	public int size() {
		return count;
	}

	@Override
	public Iterator<Type> iterator() {
		return new LinkedStackIterator();
	}
	
	private class LinkedStackIterator implements Iterator<Type>{
		private Node current = first;
		
		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public Type next() {
			if(!hasNext()) throw new NoSuchElementException();
			Type data = current.data;
			current = current.next;
			return data;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();	
		}
		
	}
	
}
