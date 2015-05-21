package jcats.util;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Random;

import jcats.io.IGenericWriter;
import jcats.model.Tick;
import jcats.model.TickType;

public class MockDataProvider implements IDataProvider {
	private List<String> tickers;
	private IGenericWriter<Tick> writer;
	private Thread thread;
	private boolean active;

	@Override
	public void setTickers(List<String> tickers) {
		this.tickers = tickers;
	}

	@Override
	public void setWriter(IGenericWriter<Tick> w) {
		this.writer = w;
	}
	
	@Override
	public void start() {
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Random r = new Random();
				Tick t = new Tick();
				try {
					while (active) {
						String ticker = tickers.get(r.nextInt(tickers.size()));
						t.initWith(ticker, 
								Instant.now().toEpochMilli(),
								r.nextBoolean() ? r.nextBoolean() ? TickType.BID : TickType.ASK : r.nextBoolean() ? TickType.BID_SIZE : TickType.ASK_SIZE,
								r.nextFloat() + 0.5, 
								(r.nextInt(100)+1) * 10000);
						writer.write(t);
						Thread.sleep(r.nextInt(10));
					}
				} catch (IOException e) {
					active = false;
				} catch (InterruptedException e) {
					active = false;
				}
			}
			
		});
		active = true;
		thread.start();
	}
	
	@Override
	public void stop() {
		active = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
