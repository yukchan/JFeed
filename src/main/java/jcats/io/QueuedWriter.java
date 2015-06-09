package jcats.io;

import java.io.IOException;

import jcats.util.ICopiable;
import jcats.util.RingPool;
import jcats.util.RingBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueuedWriter<T extends ICopiable<T>> implements Runnable, IGenericWriter<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueuedWriter.class);
	private static final int SPIN_TRIES = 100;
	private final IGenericWriter<T> writer;
	private final RingBuffer<T> queue;
	private final RingPool<T> pool;
	private volatile boolean active;
	private Thread thread;
	
	public QueuedWriter(IGenericWriter<T> w, int size) {
		writer = w;
		queue = new RingBuffer<T>(size);
		pool = new RingPool<T>(size);
	}
	
	@Override
	public void write(T t) throws IOException {
		if (active) {
			T p = pool.acquire(t);
			queue.push(p);
		}
	}
	
	@Override
	public void close() throws IOException {
		
	}

	public void start() {
		if (thread != null) return;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() throws InterruptedException {
		active = false;
		Thread t = thread;
		if (t != null) {
			t.interrupt();
			t.join();
		}
		thread = null;
	}

	@Override
	public synchronized void run() {
		active = true;
		int count = SPIN_TRIES;
		try {
			while (active) {
				T t = null;
				while (null != (t = queue.poll())) {
					writer.write(t);
					pool.release(t);
				}
				if (--count <= 0) {
					Thread.yield();
					count = SPIN_TRIES;
				}
			}
		} catch (IOException e) {
			LOGGER.error("Failed to process tick from queue", e);
			active = false;
		}
		thread = null;
	}
}
