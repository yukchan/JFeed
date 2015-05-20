package jcats.util;


public class RingPool<T extends ICopiable<T>> {
	private final RingBuffer<T> queue;
	
	public RingPool(int size) {
		queue = new RingBuffer<T>(size);
	}
	
	public boolean release(T t) {
		return queue.push(t);
	}
	
	public T acquire(T t) {
		T p = queue.poll();
		if (p != null) {
			p.copyFrom(t);
		} else {
			p = t.clone();
		}
		return p;
	}
	
	public int size() {
		return queue.size();
	}
}
