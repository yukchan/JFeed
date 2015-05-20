package jcats.model;

public class Product {
	private String ticker;

	public Product(String ticker) {
		this.ticker = ticker;
	}

	public String getName() {
		return ticker;
	}

}
