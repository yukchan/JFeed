package jcats.io;

import java.io.IOException;
import java.util.ArrayList;

public class MultipleWriter<T> implements IGenericWriter<T> {
	private final ArrayList<IGenericWriter<T>> writers = new ArrayList<IGenericWriter<T>>(); 
	
	public void addWriter(IGenericWriter<T> w) {
		writers.add(w);
	}
	
	@Override
	public void write(T t) throws IOException {
		for (IGenericWriter<T> w : writers) {
			w.write(t);
		}
	}

	@Override
	public void close() throws IOException {
		for (IGenericWriter<T> w : writers) {
			w.close();
		}
		writers.clear();
	}

}
