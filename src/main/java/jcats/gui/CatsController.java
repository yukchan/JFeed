package jcats.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import jcats.io.BidAskWriter;
import jcats.io.IGenericWriter;
import jcats.io.MultipleWriter;
import jcats.io.QueuedWriter;
import jcats.io.TickDispatcher;
import jcats.io.TickSeriesWriter;
import jcats.model.Tick;
import jcats.model.TickFileSystem;
import jcats.util.MockDataProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatsController.class);
	private static final Properties properties = new Properties();
	private final List<String> tickers = new ArrayList<String>();
	private final MultipleWriter<Tick> multiWriter = new MultipleWriter<Tick>(); 
	private final QueuedWriter<Tick> asyncTickWriter;
	private boolean active;
	private final MockDataProvider provider;
	
	public CatsController() {
		if (!loadProperties()) {
			asyncTickWriter = null;
			provider = null;
			return;
		}
		TimeZone.setDefault(TimeZone.getTimeZone(properties.getProperty("timezone")));
		Collections.addAll(tickers, properties.getProperty("tickers").split(","));
		String basePath = properties.getProperty("tick.basePath");
		
		// market data thread
		TickFileSystem.setBasePath(basePath);
		TickDispatcher tickSeriesDispatcher = new TickDispatcher();
		for (String ticker : tickers) {
			tickSeriesDispatcher.addWriter(ticker, new TickSeriesWriter(ticker, basePath, true));
		}
		asyncTickWriter = new QueuedWriter<Tick>(tickSeriesDispatcher, 1<<10);
		
		multiWriter.addWriter(new BidAskWriter(asyncTickWriter));
		
		provider = new MockDataProvider();
		provider.setTickers(tickers);
		provider.setWriter(multiWriter);
//		// historical data thread
//		HistoricalDataHandler hist = HistoricalDataHandler.getInstance();
//		TickSeriesDispatcher histWriter = new TickSeriesDispatcher(importPath, false);
//		asyncHistWriter = new QueuedWriter<Tick>(histWriter, 1<<14);
//		asyncHistWriter.start();
//		connectionHandler.addReconnectHandler(hist);
//		hist.request(controller, "AUD-USD", 
//				DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2015-01-21 00:00:00"), 
//				DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2015-01-21 06:12:00"),
//				asyncHistWriter);
	}
	public void addWriter(IGenericWriter<Tick> w) {
		multiWriter.addWriter(w);
	}
	public List<String> getTickers() {
		return Collections.unmodifiableList(tickers);
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	private boolean loadProperties() {
		try {
			properties.load(CatsController.class.getResourceAsStream("/cats.properties"));
			LOGGER.info(properties.getProperty("timezone"));
			LOGGER.info(properties.getProperty("tickers"));
			LOGGER.info(properties.getProperty("tick.basePath"));
			LOGGER.info(properties.getProperty("tick.importPath"));
			return true;
		} catch (IOException e) {
			LOGGER.error("Failed to load properties");
			LOGGER.warn(e.getMessage());
		}
		return false;
	}
	public void start() {
		if (provider == null || asyncTickWriter == null) return;
		asyncTickWriter.start();		
		provider.start();
		setActive(true);
	}
	public void stop() {
		setActive(false);
		if (provider == null || asyncTickWriter == null) return;
		try {
			provider.stop();
			asyncTickWriter.stop();
		} catch (InterruptedException e) {
			LOGGER.error("Failed to stop");
			LOGGER.warn(e.getMessage());
		}
	}
}
