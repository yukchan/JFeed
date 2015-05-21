package jcats.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import jcats.util.ICopiable;

public class Tick implements ICopiable<Tick> {
	
	public Tick() {
		initWith(null, 0, TickType.NULL, Double.NaN, 0);
	}
	
	public Tick(String ticker, long time, TickType type, double price, long size) {
		initWith(ticker, time, type, price, size);
	}
	
	private String ticker;
	private LocalDateTime timestamp;
	private long timeInMillis;
	private TickType type;
	private double price;
	private long size;
	
	public void initWith(String ticker, long timeInMillis, TickType type, double price, long size) {
		this.ticker = ticker;
		this.timeInMillis = timeInMillis;
		this.timestamp = null;
		this.type = type;
		this.price = price;
		this.size = size;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public long getTimeInMillis() {
		return timeInMillis;
	}
	
	public LocalDateTime getTimestamp() {
		if (timestamp == null) {
			timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMillis), TimeZone.getDefault().toZoneId());
		}
		return timestamp;
	}
	
	public TickType getType() {
		return type;
	}
	
	public double getPrice() {
		return price;
	}
	
	public long getSize() {
		return size;
	}
	
	public String toFileString() {
		StringBuilder b = new StringBuilder(); 
		b.append(TickFileSystem.getTimeFormatter().format(getTimestamp()))
		.append(',').append(getType())
		.append(',').append(getPrice())
		.append(',').append(getSize());
		return b.toString();
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder(); 
		b.append(ticker)
		.append(',').append(TickFileSystem.getDateFormatter().format(getTimestamp()))
		.append(',').append(TickFileSystem.getTimeFormatter().format(getTimestamp()))
		.append(',').append(getType())
		.append(',').append(getPrice())
		.append(',').append(getSize());
		return b.toString();
	}
	
	@Override
	public void copyFrom(Tick t) {
		this.initWith(t.ticker, t.timeInMillis, t.type, t.price, t.size);
	}
	
	@Override
	public Tick clone() {
		return new Tick(ticker, timeInMillis, type, price, size);
	}
}
