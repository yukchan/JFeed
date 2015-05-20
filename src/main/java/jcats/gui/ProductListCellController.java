package jcats.gui;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ProductListCellController extends ListCell<Product>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductListCellController.class);
	
	@FXML
	private HBox hBox;
	@FXML
	private Label ticker;
	@FXML
	private Label time;
	@FXML
	private Label bid;
	@FXML
	private Label ask;
	@FXML
	private Label bidSize;
	@FXML
	private Label askSize;
	
	private Pane pane;
	
	public ProductListCellController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductListCell.fxml"));
		fxmlLoader.setController(this);
		try {
			pane = (Pane)fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("Failed to load fxml");
			LOGGER.warn(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void updateItem(Product item, boolean empty){
		super.updateItem(item, empty);
		ticker.textProperty().unbind();
		time.textProperty().unbind();
		bid.textProperty().unbind();
		ask.textProperty().unbind();
		bidSize.textProperty().unbind();
		askSize.textProperty().unbind();
		if (item == null || empty) {
			setGraphic(null);
		} else if ("HEADER".equals(item.getTicker())) {
			ticker.setText("Ticker");
			time.setText("Time");
			bid.setText("Bid");
			ask.setText("Ask");
			bidSize.setText("Bid Size");
			askSize.setText("Ask Size");
			setGraphic(getBox());
		} else {
			ticker.textProperty().bind(item.tickerProperty());
			time.textProperty().bind(item.timeStringProperty());
			bid.textProperty().bind(item.bidStringProperty());
			ask.textProperty().bind(item.askStringProperty());
			bidSize.textProperty().bind(item.bidSizeStringProperty());
			askSize.textProperty().bind(item.askSizeStringProperty());
			setGraphic(getBox());
		}
	}
	
	public HBox getBox() {
		return hBox;
	}

    public Pane getPane() {
    	return pane;
    }
    
}