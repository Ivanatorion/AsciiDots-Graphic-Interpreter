package interpreter;

import interpreter.Program.Direction;

public class Dot {

	private double value;
	private int posX;
	private int posY;
	private Direction dir;
	
	Dot(double v, int px, int py, Direction d){
		this.posX = px;
		this.posY = py;
		this.value = v;
		this.dir = d;
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
	
}
