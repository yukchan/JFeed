package jcats.model;

public class Bar {
	protected String ticker;
	protected long timeInMillis;
	protected double open;
	protected double high;
	protected double low;
	protected double close;
	protected long size;
	
	public Bar(String ticker) {
		init(ticker, 0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0);
	}
	
	public Bar(String ticker, long timeInMillis, double open, double high, double low, double close, long size) {
		init(ticker, timeInMillis, open, high, low, close, size);
	}
	
	private void init(String ticker, long timeInMillis, double open, double high, double low, double close, long size) {
		this.ticker = ticker;
		this.timeInMillis = timeInMillis;
		this.open = open; 
		this.high = high; 
		this.low = low; 
		this.close = close; 
		this.size = size;
	}
	
	public long getTimeInMillis() {
		return timeInMillis;
	}
	public double getOpen() {
		return open;
	}
	public double getHigh() {
		return high;
	}
	public double getLow() {
		return low;
	}
	public double getClose() {
		return close;
	}
	public long getSize() {
		return size;
	}
}
