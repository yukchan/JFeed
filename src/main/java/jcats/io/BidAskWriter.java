package jcats.io;

import java.io.IOException;

import jcats.model.Tick;
import jcats.model.TickType;

public class BidAskWriter implements IGenericWriter<Tick> {

	private final IGenericWriter<Tick> writer;
	public BidAskWriter(IGenericWriter<Tick> w) {
		this.writer = w;
	}
	@Override
	public void write(Tick t) throws IOException {
		if (writer == null) return;
		if (TickType.BID.equals(t.getType()) || TickType.ASK.equals(t.getType())) {
			writer.write(t);
		}
		
	}

	@Override
	public void close() throws IOException {
		if (writer == null) return;
		writer.close();
	}

}
