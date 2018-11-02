package interpreter;

import java.util.LinkedList;
import java.util.Queue;

public class Operation {
	private Queue<Dot> vertQ;
	private Queue<Dot> horQ;
	private int posX;
	private int posY;
	boolean outHori;
	
	public enum OpName {ADD, SUB, MULT, DIV, LESSER, EQUAL, GREATER, LEQUAL, GEQUAL, IF_THEN_ELSE};
	
	OpName operation;
	
	Operation(boolean outH, OpName oper, int pX, int pY){
		this.outHori = outH;
		this.operation = oper;
		this.vertQ = new LinkedList<Dot>();
		this.horQ = new LinkedList<Dot>();
		this.posX = pX;
		this.posY = pY;
	}
	
	private boolean floatEqual(double f1, double f2){
		return Math.abs(f1 - f2) < 0.0001;
	}
	
	private Dot operate(Dot vDot, Dot hDot){
		double firstOperand, secondOperand, result = 0;
		Direction dir;
		
		if(outHori){
			firstOperand = hDot.getValue();
			secondOperand = vDot.getValue();
			dir = hDot.getDir();
		}
		else{
			firstOperand = vDot.getValue();
			secondOperand = hDot.getValue();
			dir = vDot.getDir();
		}
		
		switch(operation){
			case ADD:
				result = firstOperand + secondOperand;
				break;
			case SUB:
				result = firstOperand - secondOperand;
				break;
			case MULT:
				result = firstOperand * secondOperand;
				break;
			case DIV:
				result = firstOperand / secondOperand;
				break;
			case LESSER:
				result = (firstOperand < secondOperand) ? 1 : 0;
				break;
			case GREATER:
				result = (firstOperand > secondOperand) ? 1 : 0;
				break;
			case EQUAL:
				result = (floatEqual(firstOperand, secondOperand)) ? 1 : 0;
				break;
			case GEQUAL:
				result = (firstOperand > secondOperand || floatEqual(firstOperand, secondOperand)) ? 1 : 0;
				break;
			case LEQUAL:
				result = (firstOperand < secondOperand || floatEqual(firstOperand, secondOperand)) ? 1 : 0;
				break;
			case IF_THEN_ELSE:
				result = firstOperand;
				if(Math.abs(secondOperand) < 0.00001)
					dir = hDot.getDir();
				else
					dir = Direction.UP;
				break;
		}
		
		return new Dot(result, this.posX, this.posY, dir);
	}
	
	private Dot insertVert(Dot dot){
		if(horQ.isEmpty()){
			vertQ.add(dot);
			return null;
		}
		else{
			Dot dot2 = horQ.poll();
			return operate(dot, dot2);
		}
	}
	
	private Dot insertHori(Dot dot){
		if(vertQ.isEmpty()){
			horQ.add(dot);
			return null;
		}
		else{
			Dot dot2 = vertQ.poll();
			return operate(dot2, dot);
		}
	}
	
	public Dot insert(Dot curDot) {
		curDot.setFrozen(true);
		switch(curDot.getDir()){
			case UP:
			case DOWN:
				return insertVert(curDot);
			case RIGHT:
			case LEFT:
				return insertHori(curDot);
		}
		return null; //Will never happen
	}
	
	public int getPosX(){
		return posX;
	}
	
	public int getPosY(){
		return posY;
	}

	
}
