package interpreter;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainClass extends Application {

	private static final int DEFAULT_DELAY = 300;
	private static int curDelay;
	
	static boolean isRunning;
	
	Stage wind;
	static Program prog;
	
	BorderPane layout;
	
	ScrollPane sp;
	GridPane gp;
	
	//Menu Bar
	MenuBar menuBar;
	Menu fileMenu;
	
	MenuItem openFile;
	static MenuItem stepProgram;
	static MenuItem stopProgram;
	static MenuItem runProgram;
	
	Menu settingsMenu;
	MenuItem mDelay;
	MenuItem mClearOutput;
	
	static Timer progLoop;
	
	//KeyComb
	final KeyCodeCombination stepProgKCC = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
	final KeyCodeCombination stopProgKCC = new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN);
	final KeyCodeCombination runProgKCC = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
	final KeyCodeCombination changeDelayKCC = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
	
	static VBox outputBox;
	static Label curLineLabel;
	private static final Font outputFont = new Font("Verdana", 14);
	
	//Controls
	VBox controlBox;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	//Keyboard event handler 
	/*
    EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() { 
       @Override 
       public void handle(KeyEvent e) { 
          if(stepProgKCC.match(e)){
        	  if(prog != null) stepProgram();
          }
          if(runProgKCC.match(e)){
        	  if(prog != null) runProgram();
          }
       } 
    }; */

	private void readProgram(Stage wind, GridPane gp){
		prog = null;
		runProgram.setDisable(true);
		stepProgram.setDisable(true);
		stopProgram.setDisable(true);
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
			//e.printStackTrace();
		}
		
	}
	
	public static void outputStringLine(String line){
		Label newLineLabel = new Label(line);
		newLineLabel.setFont(outputFont);
		outputBox.getChildren().add(newLineLabel);
		curLineLabel = null;
	}
	
	public static void outputStringCurLine(String line){
		if(curLineLabel == null){
			curLineLabel = new Label(line);
			curLineLabel.setFont(outputFont);
			outputBox.getChildren().add(curLineLabel);
			return;
		}
		else{
			if(line.equals("\n")){
				curLineLabel = null;
				return;
			}
			curLineLabel.setText(curLineLabel.getText() + line);
		}
	}
	
	public void changeDelayWindow(Stage wind){
		TextInputDialog tid = new TextInputDialog(Integer.toString(curDelay));
		tid.setTitle("Change Delay");
		tid.setContentText("Enter the new delay (ms): ");
		tid.setHeaderText(null);
		tid.setGraphic(null);
		Optional<String> input = tid.showAndWait();
		if (input.isPresent()){
			try{
			    int newDelay = Integer.parseInt(input.get());
			    if(newDelay > 0)
			    	curDelay = newDelay;
			    if(isRunning){
			    	progLoop.cancel();
			    	progLoop = new Timer();
					progLoop.scheduleAtFixedRate(new TimerTask() {
				        @Override
				        public void run() {
				        	javafx.application.Platform.runLater(new Runnable() {
				                @Override
				                public void run() {
				                    stepProgram();
				                }
				            });
				        }
				    }, 0, curDelay);
			    }
			}
			catch(NumberFormatException e){
				//TODO: Something...
			}
		}
	}
	
	private void clearOutput() {
		outputBox.getChildren().clear();
		Label outputLabel = new Label("Output:");
		outputLabel.setStyle("-fx-font-weight: bold");
		outputLabel.setFont(new Font("Verdana", 18));
		outputBox.getChildren().add(outputLabel);
	}
	
	private void runProgram(){
		if(isRunning)
			return;
		
		runProgram.setDisable(true);
		stepProgram.setDisable(true);
		isRunning = true;
		progLoop = new Timer();
		progLoop.scheduleAtFixedRate(new TimerTask() {
	        @Override
	        public void run() {
	        	javafx.application.Platform.runLater(new Runnable() {
	                @Override
	                public void run() {
	                    stepProgram();
	                }
	            });
	        }
	    }, 0, curDelay);
		stopProgram.setDisable(false);
	}
	
	private void stopProgram() {
		if(!isRunning)
			return;
		progLoop.cancel();
		isRunning = false;
		stopProgram.setDisable(true);
		runProgram.setDisable(false);
		stepProgram.setDisable(false);
	}
	
	private static void stepProgram(){
		if(!prog.step()) {
			if(isRunning){
				progLoop.cancel();
				isRunning = false;
			}
			runProgram.setDisable(false);
			stopProgram.setDisable(true);
			stepProgram.setDisable(false);
			
			prog.reset();
			
			Alert alert = new Alert(AlertType.NONE, "Program Ended", ButtonType.OK);
			alert.setTitle("End");
			alert.showAndWait();
		}
	}
	
	public static int getUserInputValue(){
		boolean wasRunning = false;
		int newValue = 0;
		if(isRunning){
			wasRunning = true;
			progLoop.cancel();
		}
		TextInputDialog tid = new TextInputDialog(Integer.toString(0));
		tid.setTitle("Enter Value");
		tid.setContentText("Enter the new dot value: ");
		tid.setHeaderText(null);
		tid.setGraphic(null);
		Optional<String> input = tid.showAndWait();
		if (input.isPresent()){
			try{
			    newValue = Integer.parseInt(input.get());
			}
			catch(NumberFormatException e){
				//TODO: Something...
			}
		}
		if(wasRunning){
			isRunning = true;
	    	progLoop = new Timer();
			progLoop.scheduleAtFixedRate(new TimerTask() {
		        @Override
		        public void run() {
		        	javafx.application.Platform.runLater(new Runnable() {
		                @Override
		                public void run() {
		                    stepProgram();
		                }
		            });
		        }
		    }, 0, curDelay);
	    }
		return newValue;
	}
	
	@Override
	public void start(Stage arg) throws Exception {
		isRunning = false;
		curDelay = DEFAULT_DELAY;
		
		wind = arg;
		wind.setTitle("AsciiDots Interpreter");
		
		layout = new BorderPane();
		
		//Left
		outputBox = new VBox();
		outputBox.setMinWidth(300);
		clearOutput();
				
		
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
		runProgram.setOnAction(e -> runProgram());
		stopProgram = new MenuItem("Stop");
		stopProgram.setAccelerator(stopProgKCC);
		stopProgram.setDisable(true);
		stopProgram.setOnAction(e -> stopProgram());
		stopProgram.setDisable(true);
		
		fileMenu.getItems().add(openFile);
		fileMenu.getItems().add(new SeparatorMenuItem());
		fileMenu.getItems().add(stepProgram);
		fileMenu.getItems().add(runProgram);
		fileMenu.getItems().add(stopProgram);
		
		settingsMenu = new Menu("Options");
		mDelay = new MenuItem("Run Delay");
		mDelay.setOnAction(e -> changeDelayWindow(wind));
		mDelay.setAccelerator(changeDelayKCC);
		mClearOutput = new MenuItem("Clear Output");
		mClearOutput.setOnAction(e -> clearOutput());
		//mDelay.setAccelerator(changeDelayKCC);
		
		settingsMenu.getItems().add(mDelay);
		settingsMenu.getItems().add(mClearOutput);
		
		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(settingsMenu);
		
		sp.setContent(gp);
		
		layout.setTop(menuBar);
		layout.setCenter(sp);
		layout.setLeft(outputBox);
		layout.setRight(controlBox);
		
		Scene scene = new Scene(layout, 800, 600);
		wind.setScene(scene);
		
		wind.getIcons().add(new Image(MainClass.class.getResourceAsStream("/images/AsciiDotsIcon.png")));
		wind.show();
		
	}

}
