package interpreter;

import java.util.List;

import javafx.scene.control.Label;

public class Program {
	private Label[][] matrix;
	private List<Warp> warps;
	
	public Label[][] getLabelMatrix(){
		return matrix;
	}
	
	public List<Warp> getWarps(){
		return warps;
	}
	
	public Warp warpAt(int x, int y){
		int i = 0;
		while(i < warps.size()){
			if((warps.get(i).getX1() == x && warps.get(i).getY1() == y) || (warps.get(i).getX2() == x && warps.get(i).getY2() == y)){
				return warps.get(i);
			}
			i++;
		}
		return null;
	}
	
	Program(Label[][] m, List<Warp> w){
		matrix = m;
		warps = w;
	}
}
