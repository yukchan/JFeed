package jcats.util;

import jcats.util.PaddedAtomicInteger;

public class RingBuffer<T> {
	private final int capacity;
	private final int mask;
	private final T[] buffer;
	
	private final PaddedAtomicInteger head = new PaddedAtomicInteger(0);
	private final PaddedAtomicInteger tail = new PaddedAtomicInteger(0);
	
	@SuppressWarnings("unchecked")
	public RingBuffer(int capacity){
		this.capacity = getNextPowerOfTwo(capacity);
		this.mask = this.capacity - 1;
		this.buffer = (T[]) new Object[this.capacity];
	}
	
	public int getNextPowerOfTwo(int i) {
		if (i < 1) return 1;
		return 1 << (32 - Integer.numberOfLeadingZeros(i - 1));
	}
	
	public boolean push(T t) {
		final int currentTail = tail.get();
		final int minHead = currentTail - capacity;
		if (minHead >= head.get()) {
			return false;
		}
		
		buffer[currentTail & mask] = t;
		tail.lazySet(currentTail + 1);

		return true;
	}
	
	public T poll() {
		final int currentHead = head.get();
		if (currentHead >= tail.get()) {
			return null;
		}
		
		final int index = currentHead & mask;
		final T t = buffer[index];
		buffer[index] = null;
		head.lazySet(currentHead + 1);
		
		return t;
	}
	
	public int size() {
		return tail.get() - head.get();
	}
	
}
