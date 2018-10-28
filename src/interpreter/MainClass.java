package interpreter;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainClass extends Application {

	Stage wind;
	Program prog;
	
	BorderPane layout;
	
	ScrollPane sp;
	GridPane gp;
	
	//Menu Bar
	MenuBar menuBar;
	Menu fileMenu;
	
	MenuItem openFile;
	MenuItem stepProgram;
	MenuItem runProgram;
	
	Menu settingsMenu;
	MenuItem mDelay;
	
	//KeyComb
	final KeyCodeCombination stepProgKCC = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
	final KeyCodeCombination runProgKCC = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
	final KeyCodeCombination changeDelayKCC = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
	
	//Output
	VBox outputBox;
	private static final Font outputFont = new Font("Verdana", 14);
	
	//Controls
	VBox controlBox;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	//Keyboard event handler 
    EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() { 
       @Override 
       public void handle(KeyEvent e) { 
          if(stepProgKCC.match(e)){
        	  if(prog != null) stepProgram();
          }
       } 
    };

	private void readProgram(Stage wind, GridPane gp){
		prog = null;
		runProgram.setDisable(true);
		stepProgram.setDisable(true);
		try {
			prog = LoadSave.readProgramFromFileSystem(wind);
			gp.getChildren().clear();
			Label[][] labelMatrix = prog.getCurrentLabelMatrix();
			for(int y = 0; y < labelMatrix.length; y++){
				for(int x = 0; x < labelMatrix[0].length; x++){
					gp.add(labelMatrix[y][x], x, y);
					if(prog.warpAt(x, y) != null){
						labelMatrix[y][x].setTextFill(Color.DARKBLUE);
						labelMatrix[y][x].setStyle("-fx-font-weight: bold");
					}
				}
			}
			runProgram.setDisable(false);
			stepProgram.setDisable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void outputStringLine(String line){
		Label newLineLabel = new Label(line);
		newLineLabel.setFont(outputFont);
		outputBox.getChildren().add(newLineLabel);
	}
	
	public void changeDelayWindow(Stage wind){
		
	}
	
	public void stepProgram(){
		prog.step();
	}
	
	@Override
	public void start(Stage arg) throws Exception {
		wind = arg;
		wind.setTitle("AsciiDots Interpreter");
		
		layout = new BorderPane();
		
		//Left
		outputBox = new VBox();
		outputBox.setMinWidth(300);
		Label outputLabel = new Label("Output:");
		outputLabel.setStyle("-fx-font-weight: bold");
		outputLabel.setFont(new Font("Verdana", 18));
		outputBox.getChildren().add(outputLabel);
		
		
		//Center
	    sp = new ScrollPane();
		gp = new GridPane();
	
		//Menu
		menuBar = new MenuBar();
		fileMenu = new Menu("File");
		openFile = new MenuItem("Open...");
		openFile.setOnAction(e -> readProgram(wind, gp));
		stepProgram = new MenuItem("Step");
		stepProgram.setAccelerator(stepProgKCC);
		stepProgram.setDisable(true);
		stepProgram.setOnAction(e -> stepProgram());
		runProgram = new MenuItem("Run");
		runProgram.setAccelerator(runProgKCC);
		runProgram.setDisable(true);
		
		fileMenu.getItems().add(openFile);
		fileMenu.getItems().add(new SeparatorMenuItem());
		fileMenu.getItems().add(stepProgram);
		fileMenu.getItems().add(runProgram);
		
		settingsMenu = new Menu("Options");
		mDelay = new MenuItem("Run Delay");
		mDelay.setOnAction(e -> changeDelayWindow(wind));
		mDelay.setAccelerator(changeDelayKCC);
		
		settingsMenu.getItems().add(mDelay);
		
		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(settingsMenu);
		
		sp.setContent(gp);
		
		layout.setTop(menuBar);
		layout.setCenter(sp);
		layout.setLeft(outputBox);
		layout.setRight(controlBox);
		
		Scene scene = new Scene(layout, 200, 200);
		wind.setScene(scene);
		
		wind.show();
		
	}

}
