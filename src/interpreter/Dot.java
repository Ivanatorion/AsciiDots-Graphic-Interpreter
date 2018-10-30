package interpreter;

import interpreter.Program.Direction;

public class Dot {

	private double value;
	private int posX;
	private int posY;
	private Direction dir;
	private boolean frozen;
	private boolean printValueNextCicle;
	
	Dot(double v, int px, int py, Direction d){
		this.posX = px;
		this.posY = py;
		this.value = v;
		this.dir = d;
		this.frozen = false;
		this.printValueNextCicle = false;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}
	
	public boolean isFrozen() {
		return frozen;
	}
	
	public void setFrozen(boolean frz) {
		this.frozen = frz;
	}
	
	public boolean isPrintingValue() {
		return printValueNextCicle;
	}
	
	public void setPrintValueNextCicle(boolean ptvnc) {
		this.printValueNextCicle = ptvnc;
	}
	
}
