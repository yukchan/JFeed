package jcats.util;

import java.util.List;

import jcats.io.IGenericWriter;
import jcats.model.Tick;

public interface IDataProvider {
	public void setTickers(List<String> tickers);
	public void setWriter(IGenericWriter<Tick> w);
	public void start();
	public void stop();

}
