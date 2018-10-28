package interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LoadSave {
	public static Program readProgramFromFileSystem(Stage wind) throws IOException{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open AsciiDots Program");
		//File file = fileChooser.showOpenDialog(wind);
		File file = new File("C:\\Users\\Peter\\Documents\\Devcpp\\AsciiDots\\ex1.txt");
		
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		
		Label[][] l;
		
		List<Warp> warps = new ArrayList<Warp>();
		
		ArrayList<String> lines = new ArrayList<String>();
		
		String aux;
		aux = br.readLine();
		
		while(aux.length() >= 3 && aux.charAt(0) == '%' && aux.charAt(1) == '$'){
			warps.add(new Warp(aux.charAt(2), -1, -1, -1, -1));
			aux = br.readLine();
		}
		
		while(aux != null){
			lines.add(aux);
			aux = br.readLine();
		}
		
		int maior = 0;
		for(String ls : lines){
			if(ls.length() > maior)
				maior = ls.length();
		}
		
		l = new Label[lines.size()][maior];
		for(int y = 0; y < lines.size(); y++){
			for(int x = 0; x < lines.get(y).length(); x++){
				l[y][x] = new Label(Character.toString(lines.get(y).charAt(x)));
				l[y][x].setMinHeight(16);
				l[y][x].setMinWidth(10);
				l[y][x].setMaxHeight(16);
				l[y][x].setMaxWidth(10);
				//l[y][x].setFont(new Font("SansSerif", 15));
				
				for(Warp w : warps){
					if(w.getC() == lines.get(y).charAt(x)){
						if(w.getX1() == -1){
							w.setX1(x);
							w.setY1(y);
						}
						else if(w.getX2() == -1){
							w.setX2(x);
							w.setY2(y);
						}
					}
				}
			}
			
			for(int x = lines.get(y).length(); x < maior; x++){
				l[y][x] = new Label(" ");
				l[y][x].setMinHeight(16);
				l[y][x].setMinWidth(9);
				l[y][x].setMaxHeight(16);
				l[y][x].setMaxWidth(9);
				l[y][x].setFont(new Font("SansSerif", 15));
			}
		}
		
	
		Program result = new Program(l, warps);
	
		br.close();
	
		return result;
	}
}
