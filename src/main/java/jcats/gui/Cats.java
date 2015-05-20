package jcats.gui;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jcats.gui.CatsController;

public class Cats extends Application {
	volatile boolean active = true;
	final CatsController cc = new CatsController(); 
	
    @Override
    public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("JCats");
		List<String> tickers = cc.getTickers();
		
		ProductListController plc = new ProductListController();
		plc.addTickers(tickers);
		cc.addWriter(plc);

		Scene myScene = new Scene(plc.getPane(),800.0,600.0);;
		primaryStage.setScene(myScene);
		
		cc.start();

		primaryStage.show();
    }
    
    @Override
    public void stop() {
    	active = false;
    	cc.stop();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
