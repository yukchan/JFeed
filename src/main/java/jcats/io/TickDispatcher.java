package jcats.io;

import java.io.IOException;
import java.util.HashMap;

import jcats.model.Tick;

public class TickDispatcher implements IGenericWriter<Tick> {
	private final HashMap<String, IGenericWriter<Tick>> writers = new HashMap<String, IGenericWriter<Tick>>();
	
	public TickDispatcher() {
	}
	
	public void addWriter(String ticker, IGenericWriter<Tick> writer) {
		writers.put(ticker, writer);
	}
	
	@Override
	public void write(Tick t) throws IOException {
		if (t == null) {
			close();
			return;
		}
		String ticker = t.getTicker();
		if (!writers.containsKey(ticker)) return;
		writers.get(ticker).write(t);
	}
	
	@Override
	public void close() throws IOException {
		for (IGenericWriter<Tick> w : writers.values()) {
			w.close();
		}
	}

}
