package interpreter;

import java.util.ArrayList;
import java.util.List;

import interpreter.Operation.OpName;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class Program {
	
	private char[][] charMatrix;
	private Label[][] matrix;
	private List<Warp> warps;
	private List<Dot> dots;
	private List<Operation> opList;
	
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
	
	private void checkOperationCell(int x, int y){
		char c;
		if(x < charMatrix[0].length-1 && y < charMatrix.length-1 && y > 0 && x > 0 && (charMatrix[y][x-1] == '[' && charMatrix[y][x+1] == ']' || charMatrix[y][x-1] == '{' && charMatrix[y][x+1] == '}')){
			c = charMatrix[y][x];
			OpName op;
			switch(c){
				case '+':
					op = OpName.ADD;
					break;
				case '-':
					op = OpName.SUB;
					break;
				case '*':
					op = OpName.MULT;
					break;
				case '/':
					op = OpName.DIV;
					break;
				case '=':
					op = OpName.EQUAL;
					break;
				case '<':
					op = OpName.LESSER;
					break;
				case '>':
					op = OpName.GREATER;
					break;
				case 'L':
					op = OpName.LEQUAL;
					break;
				case 'G':
					op = OpName.GEQUAL;
					break;
				default:
					return;
			}
			Operation newOp = new Operation(charMatrix[y][x-1] == '{', op, x, y);
			opList.add(newOp);
		}
	}
	
	public void reset(){
		dots.clear();
		opList.clear();
	
		for(int y = 0; y < matrix.length; y++){	
			for(int x = 0; x < matrix[0].length; x++){
				if(matrix[y][x].getText().charAt(0) == '.'){
					if(y != 0 && matrix[y-1][x].getText().charAt(0) == '|'){
						dots.add(new Dot(0.0, x, y, Direction.UP));
						matrix[y][x].setTextFill(Color.RED);
					}
					else if(y != matrix.length-1 && matrix[y+1][x].getText().charAt(0) == '|'){
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
				else {
					matrix[y][x].setTextFill(Color.BLACK);
					checkOperationCell(x,y);
				}
			}
			
		}
		
	}
	
	private boolean isOperationAt(int x, int y){
		for(int i = 0; i < opList.size(); i++){
			if(opList.get(i).getPosX() == x && opList.get(i).getPosY() == y)
				return true;
		}
		return false;
	}
	
	/*
	Flows the dot in pipes
	nextX and nextY must be pipes! (-, |, /, \)
	Return true if killed dot
	*/
	private boolean stepDot(Dot curDot, int nextX, int nextY) {
		char c = charMatrix[nextY][nextX];
		//int cy = curDot.getPosY();
		//int cx = curDot.getPosX();
		curDot.setPosX(nextX);
		curDot.setPosY(nextY);
		switch(c) {
			case '-':
				if(!isOperationAt(curDot.getPosX(), curDot.getPosY())){
					if(!(curDot.getDir() == Direction.LEFT || curDot.getDir() == Direction.RIGHT)){
						dots.remove(curDot);
						return true;
					}
				}
				break;
			case '|':
				if(!(curDot.getDir() == Direction.UP || curDot.getDir() == Direction.DOWN)){
					dots.remove(curDot);
					return true;
				}
				break;
			case '\\':
				switch(curDot.getDir()) {
					case UP:
						curDot.setDir(Direction.LEFT);
						break;
					case DOWN:
						curDot.setDir(Direction.RIGHT);
						break;
					case LEFT:
						curDot.setDir(Direction.UP);
						break;
					case RIGHT:
						curDot.setDir(Direction.DOWN);
						break;
				}
				break;
			case '/':
				if(!isOperationAt(curDot.getPosX(), curDot.getPosY())){
					switch(curDot.getDir()) {
						case UP:
							curDot.setDir(Direction.RIGHT);
							break;
						case DOWN:
							curDot.setDir(Direction.LEFT);
							break;
						case LEFT:
							curDot.setDir(Direction.DOWN);
							break;
						case RIGHT:
							curDot.setDir(Direction.UP);
							break;
					}
				}
				break;
			case ' ':
				dots.remove(curDot);
				return true;
				//break;
		}
		return false;
	}
	
	private void setDotPrinting(Dot curDot) {
		int nextX = 0, nextY = 0;
		int cx = curDot.getPosX();
		int cy = curDot.getPosY();
		switch(curDot.getDir()) {	
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
		if(!(nextY < 0 || nextY == charMatrix.length || nextX < 0 || nextX == charMatrix[0].length)){
			switch(charMatrix[nextY][nextX]) {
				case '#':
					curDot.setPrintValueNextCicle(true);
					break;
			}
		}
		
	}
	
	private void setDotValue(Dot curDot) {
		int nextX = 0, nextY = 0;
		int newValue = 0;
		int cx = curDot.getPosX();
		int cy = curDot.getPosY();
		int incX = 0, incY = 0;
		switch(curDot.getDir()) {	
			case UP:
				nextX = cx;
				nextY = cy - 1;
				incY = -1;
				break;
			case DOWN:
				nextX = cx;
				nextY = cy + 1;
				incY = 1;
				break;
			case LEFT:
				nextX = cx - 1;
				nextY = cy;
				incX = -1;
				break;
			case RIGHT:
				nextX = cx + 1;
				nextY = cy;
				incX = 1;
				break;
		}
		while(!(nextY < 0 || nextY == charMatrix.length || nextX < 0 || nextX == charMatrix[0].length) && charMatrix[nextY][nextX] >= '0' && charMatrix[nextY][nextX] <= '9') {
			newValue = newValue * 10;
			newValue = newValue + charMatrix[nextY][nextX] - '0';
			nextX = nextX + incX;
			nextY = nextY + incY;
		}
		curDot.setValue(newValue);
	}
	
	//Returns false if program ended
	public boolean step(){
		int cx, cy, nextX = 0, nextY = 0;
		char c;
		Dot curDot;
		List<Dot> dotsToAddAtEnd = new ArrayList<Dot>();
		
		for(int x = 0; x < dots.size(); x++){
			curDot = dots.get(x);
			
			if(curDot.isFrozen())
				continue;
			
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
			
			matrix[cy][cx].setTextFill(Color.BLACK);
			if(nextY < 0 || nextY == charMatrix.length || nextX < 0 || nextX == charMatrix[0].length){
				dots.remove(x);
				x--;
				continue;
			}
			
			if(stepDot(curDot, nextX, nextY)) {
				x--;
				continue;
			}
			else {
				matrix[nextY][nextX].setTextFill(Color.RED);
			}
			
			c = charMatrix[nextY][nextX];
			switch(c){
				case '#':
					if(curDot.isPrintingValue()) {
						curDot.setPrintValueNextCicle(false);
						String line;
						if(Math.abs((curDot.getValue() - (int) curDot.getValue())) < 0.00001) {
							line = Integer.toString((int) curDot.getValue());
						}
						else {
							line = Double.toString(curDot.getValue());
						}
						MainClass.outputStringLine(line);
					}
					else {
						setDotValue(curDot);
					}
					break;
				case '$':
					setDotPrinting(curDot);
					break;
				case '&':
					dots.clear();
					return false;
			}
			
			for(int i = 0; i < opList.size(); i++){
				if(nextX == opList.get(i).getPosX() && nextY == opList.get(i).getPosY()){
					Dot newD = opList.get(i).insert(curDot);
					dots.remove(x);
					x--;
					if(newD != null) dotsToAddAtEnd.add(newD);
				}
			}
			
		}
		
		dots.addAll(dotsToAddAtEnd);
		
		if(dots.isEmpty())
			return false;
		else
			return true;
	}
	
	Program(Label[][] m, List<Warp> w){
		dots = new ArrayList<Dot>();
		opList = new ArrayList<Operation>();
		matrix = m;
		warps = w;
		charMatrix = this.getCharMatrix();
		this.reset();
	}
}
