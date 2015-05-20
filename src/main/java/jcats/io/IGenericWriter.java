package jcats.io;

import java.io.IOException;

public interface IGenericWriter<T> {
	void write(T t) throws IOException;
	void close() throws IOException;
}
