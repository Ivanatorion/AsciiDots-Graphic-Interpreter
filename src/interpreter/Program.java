package interpreter;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class Program {
	
	private char[][] charMatrix;
	private Label[][] matrix;
	private List<Warp> warps;
	private List<Dot> dots;
	
	
	public enum Direction {UP, DOWN, LEFT, RIGHT};
	
	public Label[][] getCurrentLabelMatrix(){
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
	
	private char[][] getCharMatrix(){
		char[][] cmtx = new char[matrix.length][matrix[0].length];
		for(int y = 0; y < matrix.length; y++){
			for(int x = 0; x < matrix[0].length; x++){
				cmtx[y][x] = matrix[y][x].getText().charAt(0);
			}
		}
		return cmtx;
	}
	
	public void reset(){
		dots.clear();
		
		for(int y = 0; y < matrix.length; y++){
			for(int x = 0; x < matrix[0].length; x++){
				if(matrix[y][x].getText().charAt(0) == '.'){
					if(y != 0 && matrix[y-1][x].getText().charAt(0) == '|'){
						dots.add(new Dot(0.0, x, y, Direction.UP));
						matrix[y][x].setTextFill(Color.RED);
					}
					else if(y != matrix.length && matrix[y+1][x].getText().charAt(0) == '|'){
						dots.add(new Dot(0.0, x, y, Direction.UP));
						matrix[y][x].setTextFill(Color.RED);
					}
					else if(x != 0 && matrix[y][x-1].getText().charAt(0) == '-'){
						dots.add(new Dot(0.0, x, y, Direction.LEFT));
						matrix[y][x].setTextFill(Color.RED);
					}
					else if(x != matrix[0].length-1 && matrix[y][x+1].getText().charAt(0) == '-'){
						dots.add(new Dot(0.0, x, y, Direction.RIGHT));
						matrix[y][x].setTextFill(Color.RED);
					}
				}
			}
		}
		
	}
	
	//Returns false if program ended
	public boolean step(){
		int cx, cy, nextX = 0, nextY = 0;
		Dot curDot;
		
		for(int x = 0; x < dots.size(); x++){
			curDot = dots.get(x);
			cx = curDot.getPosX();
			cy = curDot.getPosY();
			switch(curDot.getDir()){
				case UP:
					nextX = cx;
					nextY = cy - 1;
					break;
				case DOWN:
					nextX = cx;
					nextY = cy + 1;
					break;
				case LEFT:
					nextX = cx - 1;
					nextY = cy;
					break;
				case RIGHT:
					nextX = cx + 1;
					nextY = cy;
					break;
			}
			if(nextY < 0 || nextY == charMatrix.length || nextX < 0 || nextX == charMatrix[0].length){
				dots.remove(x);
				x--;
				continue;
			}
			switch(charMatrix[nextY][nextX]){
				case '-':
					if(curDot.getDir() == Direction.LEFT || curDot.getDir() == Direction.RIGHT){
						curDot.setPosX(nextX);
						curDot.setPosY(nextY);
						matrix[cy][cx].setTextFill(Color.BLACK);
						matrix[nextY][nextX].setTextFill(Color.RED);
					}
					break;
				case '|':
					if(curDot.getDir() == Direction.UP || curDot.getDir() == Direction.UP){
						curDot.setPosX(nextX);
						curDot.setPosY(nextY);
						matrix[cy][cx].setTextFill(Color.BLACK);
						matrix[nextY][nextX].setTextFill(Color.RED);
					}
					break;
				default:
					dots.remove(x);
					matrix[cy][cx].setTextFill(Color.BLACK);
					x--;
					continue;
			}
		}
		
		return true;
	}
	
	Program(Label[][] m, List<Warp> w){
		dots = new ArrayList<Dot>();
		matrix = m;
		warps = w;
		charMatrix = this.getCharMatrix();
		this.reset();
	}
}
