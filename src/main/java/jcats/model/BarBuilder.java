package jcats.model;

public class BarBuilder extends Bar {
	private long periodInMillis;
	public BarBuilder(String ticker, long timeInMillis, long periodInMillis) {
		super(ticker);
		this.timeInMillis = timeInMillis;
		this.periodInMillis = periodInMillis;
		// TODO Auto-generated constructor stub
	}

	public void addPrice(double price) {
		if (price > high) {
			high = price;
		}
		if (price < low) {
			low = price;
		}
		close = price; 
	}
	
	public void addSize(long size) {
		this.size += size;
	}
	
	public boolean contains(long millis) {
		return ((millis < timeInMillis) && (millis >= (timeInMillis - periodInMillis))) ;
	}
	
	public long convertToBarTime(long millis) {
		//return (millis / periodInMillis + 1) * periodInMillis;	
		return millis - millis % periodInMillis + periodInMillis;
	}
}
