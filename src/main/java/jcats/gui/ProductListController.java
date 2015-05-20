package jcats.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import jcats.io.IGenericWriter;
import jcats.model.Tick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductListController implements IGenericWriter<Tick> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductListController.class);

    @FXML 
    private ListView<Product> listView;

    private Pane pane;
    
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private final HashMap<String, ProductUpdater> productMap = new HashMap<String, ProductUpdater>();
    
    public ProductListController() {
    	productList.add(new Product("HEADER"));
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductListView.fxml"));
		fxmlLoader.setController(this);
		try {
			pane = (Pane)fxmlLoader.load();
		} catch (IOException e) {
			LOGGER.error("Failed to load fxml");
			LOGGER.warn(e.getMessage());
			throw new RuntimeException(e);
		}
    }

    public void setListView(){

        listView.setItems(productList);

        listView.setCellFactory(
            new Callback<ListView<Product>, ListCell<Product>>() {
                @Override
                public ListCell<Product> call(ListView<Product> listView) {
                    return new ProductListCellController();
                }
            });
    }

    @FXML
    void initialize() {
        assert listView != null : "fx:id=\"listView\" was not injected: check your FXML file 'ProductListView.fxml'.";

        setListView();
    }

    public Pane getPane() {
    	return pane;
    }
    
    public void addTicker(String ticker) {
    	if (productMap.containsKey(ticker)) return;
    	Product p = new Product(ticker);
    	productList.add(p);
    	productMap.put(ticker, new ProductUpdater(p));
    }
    
    public void addTickers(Collection<String> tickers) {
		for(String ticker : tickers) {
			addTicker(ticker);
		}
    }
    
    public void setLastTick(Tick tick) {
    	ProductUpdater p = productMap.get(tick.getTicker());
    	if (p == null) return;
    	p.setLastTick(tick);
    	Platform.runLater(p);
    }

	@Override
	public void write(Tick t) throws IOException {
		setLastTick(t);
	}

	@Override
	public void close() throws IOException {
	}
}