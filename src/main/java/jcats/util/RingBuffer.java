package jcats.util;

public class RingBuffer<T> {
	private final int capacity;
	private final int mask;
	private final T[] buffer;
	private volatile int head;
	private volatile int tail;
	
	@SuppressWarnings("unchecked")
	public RingBuffer(int capacity){
		this.capacity = getNextPowerOfTwo(capacity);
		this.mask = this.capacity - 1;
		this.buffer = (T[]) new Object[this.capacity];
		this.head = 0;
		this.tail = 0;
	}
	
	public int getNextPowerOfTwo(int i) {
		if (i < 1) return 1;
		return 1 << (32 - Integer.numberOfLeadingZeros(i - 1));
	}
	
	public boolean push(T t) {
		if (isFull()) return false;
		buffer[(++tail) & mask] = t;
		return true;
	}
	
	public T poll() {
		if (isEmpty()) return null;		
		return buffer[(++head) & mask];
	}
	
	public int size() {
		int q = tail - head;
		return q < 0 ? q + capacity : q;
	}
	
	public boolean isEmpty() {
		return head == tail;
	}
	
	public boolean isFull() {
		return getNextIndex(tail) == head;
	}
	
	private int getNextIndex(int i) {
		return (i+1) & mask;
	}
}
