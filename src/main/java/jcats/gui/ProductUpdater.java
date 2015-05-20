package jcats.gui;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;





import jcats.model.Tick;
import jcats.model.TickFileSystem;
import jcats.util.RingPool;

public class ProductUpdater implements Runnable {
//	private static final Logger LOGGER = LoggerFactory.getLogger(ProductUpdater.class);
	private final DateTimeFormatter timeFormatter = TickFileSystem.getTimeFormatter();

	private final RingPool<Tick> pool;
	private final Product product;
	private final AtomicReference<Tick> lastTick = new AtomicReference<Tick>();
	public ProductUpdater(Product p) {
		product = p;
		pool = new RingPool<Tick>(2<<5);
	}

	@Override
	public void run() {
		Tick t = lastTick.getAndSet(null);
		if (t == null) return;
		
		product.setTimeString(timeFormatter.format(t.getTimestamp()));
		if ('B' == t.getType()){
			product.setBidString(String.format("%f", t.getPrice()));
		} else if ('A' == t.getType()) {
			product.setAskString(String.format("%f", t.getPrice()));
		} else if ('Y' == t.getType()) {
			product.setBidString(String.format("%f", t.getPrice()));
			product.setBidSizeString(String.format("%d", t.getSize()));
		} else if ('Z' == t.getType()) {
			product.setAskString(String.format("%f", t.getPrice()));
			product.setAskSizeString(String.format("%d", t.getSize()));
		} 
		pool.release(t);
	}
	
	public void setLastTick(Tick t) {
		if (!product.getTicker().equals(t.getTicker())) return;
		lastTick.set(pool.acquire(t));
	}

}
