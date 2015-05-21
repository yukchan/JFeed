package jcats.gui;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

import jcats.model.Tick;
import jcats.model.TickType;
import jcats.model.TickFileSystem;
import javafx.beans.property.StringProperty;

public class ProductUpdater implements Runnable {
	private final DateTimeFormatter timeFormatter = TickFileSystem.getTimeFormatter();

	private final Product buffer;
	private final Product product;
	private final AtomicBoolean changed = new AtomicBoolean(false);
	private final AtomicBoolean bidChanged = new AtomicBoolean(false);
	private final AtomicBoolean askChanged = new AtomicBoolean(false);
	private final AtomicBoolean bidSizeChanged = new AtomicBoolean(false);
	private final AtomicBoolean askSizeChanged = new AtomicBoolean(false);
	
	public ProductUpdater(Product p) {
		product = p;
		buffer = new Product(p.getTicker());
	}

	@Override
	public void run() {
		if (!changed.getAndSet(false)) return;
		product.setTimeString(buffer.getTimeString());
		setChangedProperty(bidChanged, buffer.bidStringProperty(), product.bidStringProperty());
		setChangedProperty(askChanged, buffer.askStringProperty(), product.askStringProperty());
		setChangedProperty(bidSizeChanged, buffer.bidSizeStringProperty(), product.bidSizeStringProperty());
		setChangedProperty(askSizeChanged, buffer.askSizeStringProperty(), product.askSizeStringProperty());
	}
	
	private void setChangedProperty(AtomicBoolean fieldChanged, StringProperty source, StringProperty target) {
		if (fieldChanged.getAndSet(false)) target.set(source.get());
	}
	
	public void setLastTick(Tick t) {
		if (!buffer.getTicker().equals(t.getTicker())) return;
		
		buffer.setTimeString(timeFormatter.format(t.getTimestamp()));
		if (TickType.BID.equals(t.getType())){
			buffer.setBidString(String.format("%f", t.getPrice()));
			bidChanged.set(true);
		} else if (TickType.ASK.equals(t.getType())) {
			buffer.setAskString(String.format("%f", t.getPrice()));
			askChanged.set(true);
		} else if (TickType.BID_SIZE.equals(t.getType())) {
			buffer.setBidSizeString(String.format("%d", t.getSize()));
			bidSizeChanged.set(true);
		} else if (TickType.ASK_SIZE.equals(t.getType())) {
			buffer.setAskSizeString(String.format("%d", t.getSize()));
			askSizeChanged.set(true);
		} 

		changed.set(true);
	}

}
