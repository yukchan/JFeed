package jcats.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;

import jcats.model.Tick;
import jcats.model.TickFileSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TickSeriesWriter implements IGenericWriter<Tick> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TickSeriesWriter.class);
	private String ticker;
	private String basePath;
	private boolean append;
	private LocalDate today;
	
	Writer writer;
	
	public TickSeriesWriter(String ticker, String basePath, boolean append) {
		this.ticker = ticker;
		this.basePath = basePath;
		this.append = append;
		this.today = null;
	}
	
	@Override
	public void write(Tick t) throws IOException {
		if (!ticker.equals(t.getTicker())) return;
		Writer w = getWriter(LocalDate.from(t.getTimestamp()));
		w.write(t.toFileString());
		w.write('\n');
		w.flush();
	}
	
	public void close() throws IOException {
		if (writer != null) {
			writer.close();
			writer = null;
			today = null;
		}		
	}

	private Writer getWriter(LocalDate date) throws IOException {
		if (writer == null || today == null || !today.equals(date)){
			close();
			LOGGER.info("Output tick to " + getPath(date));
			writer = new FileWriter(getPath(date), append);
			today = date;
		}
		return writer;
	}
	
	private String getPath(LocalDate date) { 
		return TickFileSystem.getPath(ticker, date, basePath);
	}
}
