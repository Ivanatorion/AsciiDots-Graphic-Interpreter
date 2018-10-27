package interpreter;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainClass extends Application {

	Stage wind;
	Program prog;
	
	BorderPane layout;
	
	MenuBar menuBar;
	Menu fileMenu;
	MenuItem openFile;
	
	public static void main(String[] args) {
		launch(args);
	}

	private void readProgram(Stage wind, GridPane gp){
		try {
			prog = LoadSave.readProgramFromFileSystem(wind);
			gp.getChildren().clear();
			Label[][] labelMatrix = prog.getLabelMatrix();
			for(int y = 0; y < labelMatrix.length; y++){
				for(int x = 0; x < labelMatrix[0].length; x++){
					gp.add(labelMatrix[y][x], x, y);
					if(prog.warpAt(x, y) != null){
						labelMatrix[y][x].setTextFill(Color.DARKBLUE);
						labelMatrix[y][x].setStyle("-fx-font-weight: bold");
					}
				}
			}
		} catch (Exception e) {
			
		}
	}
	
	@Override
	public void start(Stage arg) throws Exception {
		wind = arg;
		wind.setTitle("AsciiDots Interpreter");
		
		layout = new BorderPane();
		
		ScrollPane sp = new ScrollPane();
		GridPane gp = new GridPane();
		
		menuBar = new MenuBar();
		fileMenu = new Menu("File");
		openFile = new MenuItem("Open...");
		openFile.setOnAction(e -> readProgram(wind, gp));
		
		fileMenu.getItems().add(openFile);
		
		menuBar.getMenus().add(fileMenu);
		
		sp.setContent(gp);
		
		layout.setTop(menuBar);
		layout.setCenter(sp);
		
		Scene scene = new Scene(layout, 200, 200);
		wind.setScene(scene);
		
		wind.show();
	}

}
