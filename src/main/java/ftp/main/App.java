package ftp.main;

import ftp.view.RootController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{
	
	public Stage mainStage;
	
	private RootController controller;

	@Override
	public void start(Stage stage) throws Exception {
		this.mainStage = stage;
		
		controller = new RootController();
		
		stage.setTitle("FTP");
		stage.setScene(new Scene(controller.getView()));
		stage.show();
		
		stage.setOnCloseRequest(e -> {
			controller.DesconectarAction(null);
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
